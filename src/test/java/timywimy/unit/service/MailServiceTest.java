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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import timywimy.service.MailService;

import java.util.concurrent.TimeUnit;

@ContextConfiguration({
        "classpath:spring/spring-db-test.xml", "classpath:spring/spring-app-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = {"classpath:properties/api-test.properties"})
@Ignore
public class MailServiceTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    @Autowired
    private MailService service;

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
    public void sendEmail() {
        service.sendRegisterEmail("predigerwork@gmail.com",1234);
    }
}
