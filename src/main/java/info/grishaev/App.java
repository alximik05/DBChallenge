package info.grishaev;

import info.grishaev.model.Room;
import info.grishaev.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.applet.AppletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
@Warmup(iterations = 0)
@Measurement(iterations = 10000)
@BenchmarkMode({Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
public class App {

    JdbcTemplate jdbcTemplate;
    AtomicInteger atomicInteger = new AtomicInteger(0);
    SessionFactory sessionFactory;


    @Setup
    public void setUp() {
        System.out.println("set up");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setActiveProfiles("h2");
        context.refresh();
        jdbcTemplate = context.getBean(JdbcTemplate.class);
        LocalSessionFactoryBean bean = context.getBean(LocalSessionFactoryBean.class);
        sessionFactory = bean.getObject();

    }


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(App.class.getSimpleName())
                .threads(1)
                .forks(1)
                .build();
        new Runner(options).run();

    }

    @Benchmark
    public void ormInsert() {
        Session session = sessionFactory.openSession();
        Room room = new Room();
        room.setId(atomicInteger.get());
        room.setRoomName("name " + atomicInteger.get());

        User user = new User();
        user.setId(atomicInteger.get());
        user.setName("name " + atomicInteger.get());
        user.setRoomNumber(atomicInteger.getAndIncrement());
        Transaction transaction = session.beginTransaction();
        session.save(room);
        session.save(user);
        transaction.commit();
        session.close();
    }

//    @Benchmark
    public void jdbcInsert() {
        jdbcTemplate.update("insert into ROOM values(" + atomicInteger.get() + " , 'room " + atomicInteger.get() + "')");
        jdbcTemplate.update("INSERT INTO \"USER\" VALUES (" + atomicInteger.get() + " , 'name " + atomicInteger.get() + "' , " + atomicInteger.getAndIncrement() + ")");
    }
}
