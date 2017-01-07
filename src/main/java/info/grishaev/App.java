package info.grishaev;

import org.openjdk.jmh.annotations.*;
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
public class App {

    static JdbcTemplate jdbcTemplate;


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Warmup(iterations = 0)
    public static void main(String[] args) {
        System.out.println("Hello World!");

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        jdbcTemplate = context.getBean(JdbcTemplate.class);
        jdbcInsert();
    }



    public static void jdbcInsert() {

        for (int step = 0; step < 10; step++) {
            jdbcTemplate.update("insert into room values(" + step + " , 'room " + step + "')");
            jdbcTemplate.update("INSERT INTO \"user\" VALUES (" + step + " , 'name " + step + "' , " + step + ")");

        }
    }

}
