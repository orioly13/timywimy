package timywimy.unit.service;

import org.junit.AfterClass;
import org.junit.Assert;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import timywimy.model.security.User;
import timywimy.repository.UserRepository;
import timywimy.service.SHAPasswordWorker;
import timywimy.util.PositiveTestData;

import java.util.concurrent.TimeUnit;

@ContextConfiguration({
        "classpath:spring/spring-db-test.xml", "classpath:spring/spring-app-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = {"classpath:properties/api-test.properties"})
@Sql(scripts = {
        "classpath:db/postgresql/0-generate-all.sql"},
        config = @SqlConfig(encoding = "UTF-8"))
public class PasswordWorkerTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    @Autowired
    private SHAPasswordWorker worker;
    @Autowired
    private UserRepository userRepository;

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
    public void passwordTest() {
        //assert with itself
        String hash1 = worker.generatePasswordHash("1");
        Assert.assertEquals(true, worker.validatePassword("1", hash1));
        //assert that salt is random
        String hash2 = worker.generatePasswordHash("1");
        Assert.assertNotEquals(hash1, hash2);
        //assert current stored passwords
        User root = userRepository.get(PositiveTestData.USER_ROOT);
        Assert.assertEquals(true, worker.validatePassword("t1me@LORD", root.getPassword()));
        User admin = userRepository.get(PositiveTestData.USER_ADMIN);
        Assert.assertEquals(true, worker.validatePassword("#tTy13ALF", admin.getPassword()));
        User user = userRepository.get(PositiveTestData.USER_USER);
        Assert.assertEquals(true, worker.validatePassword("P@1ui$$pass", user.getPassword()));
    }
}
