package timywimy.unit.service;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import timywimy.service.RestService;

import java.util.concurrent.TimeUnit;

@ContextConfiguration({
       "classpath:spring/spring-db-test.xml","classpath:spring/spring-app-test.xml"
})
@RunWith(SpringRunner.class)
@TestPropertySource(locations = {"classpath:properties/api-test.properties"})
@Sql(scripts = {"classpath:db/postgresql/1-init-users.sql"}, config = @SqlConfig(encoding = "UTF-8"))
@Ignore
public class RestServiceTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    @Autowired
    private RestService service;

    private static StringBuilder results = new StringBuilder();

    //assert exceptions
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    //time check
    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }
    @Test
    public  void validRegister(){
        log.info("got here");
    }
}
