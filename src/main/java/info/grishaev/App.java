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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.applet.AppletContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
@Warmup(iterations = 0)
@Measurement(iterations = 1)
@BenchmarkMode({Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
public class App {

    static JdbcTemplate jdbcTemplate;
    static AtomicInteger atomicInteger = new AtomicInteger(0);
    static AtomicInteger atomicInteger2 = new AtomicInteger(0);
    static SessionFactory sessionFactory;


    @Setup
    public static void setUp() {
        System.out.println("set up");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setActiveProfiles("postgres");
        context.refresh();
        jdbcTemplate = context.getBean(JdbcTemplate.class);
        LocalSessionFactoryBean bean = context.getBean(LocalSessionFactoryBean.class);
        sessionFactory = bean.getObject();

    }


    public static void main(String[] args) throws RunnerException {
//        Options options = new OptionsBuilder()
//                .include(App.class.getSimpleName())
//                .threads(1)
//                .forks(1)
//                .build();
//        new Runner(options).run();

        setUp();
        long l = System.currentTimeMillis();
        batch();
        System.out.println(System.currentTimeMillis() - l);

//        Jedis jedis = new Jedis("localhost", 6379, false);
//        System.out.println(jedis.ping());
//
//        long l = System.currentTimeMillis();
//        for (int i = 0; i < 10_000; i++) {
//            jedis.lpush("room-list", "" + i, "room " + i);
//            jedis.lpush("user-list", "" + i, "name " + i, "" + i);
//        }
//        System.out.println(System.currentTimeMillis() - l);


    }

//    @Benchmark
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
        for (int i = 0; i < 10_000; i++) {
            jdbcTemplate.batchUpdate("insert into ROOM values(" + atomicInteger.get() + " , 'room " + atomicInteger.get() + "')");
            jdbcTemplate.batchUpdate("INSERT INTO \"USER\" VALUES (" + atomicInteger.get() + " , 'name " + atomicInteger.get() + "' , " + atomicInteger.getAndIncrement() + ")");
        }
    }



    public static void batch() {
        final int batchSize = 10_000;

        jdbcTemplate.batchUpdate("insert into ROOM values( ? ,  ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, atomicInteger.get());
                        ps.setString(2, "room " + atomicInteger.getAndIncrement());
                    }

                    @Override
                    public int getBatchSize() {
                        return batchSize;
                    }
                });

        jdbcTemplate.batchUpdate("insert into \"USER\" values( ? , ? , ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, atomicInteger2.get());
                        ps.setString(2, "name " + atomicInteger2.get());
                        ps.setInt(3, atomicInteger2.getAndIncrement());
                    }

                    @Override
                    public int getBatchSize() {
                        return batchSize;
                    }
                });
    }
}
