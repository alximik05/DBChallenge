package info.grishaev;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.applet.AppletContext;
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
    AtomicInteger atomicInteger = new AtomicInteger();


    @Setup
    public void setUp() {
        System.out.println("set up");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        jdbcTemplate = context.getBean(JdbcTemplate.class);
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
    public void jdbcInsert() {
        jdbcTemplate.update("insert into room values(" + atomicInteger.get() + " , 'room " + atomicInteger.get() + "')");
        jdbcTemplate.update("INSERT INTO \"user\" VALUES (" + atomicInteger.get() + " , 'name " + atomicInteger.get() + "' , " + atomicInteger.getAndIncrement() + ")");
    }

}
