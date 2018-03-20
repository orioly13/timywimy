package timywimy.unit.service;

import org.junit.*;
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
import timywimy.service.RestService;
import timywimy.util.UserTestData;
import timywimy.web.dto.security.Session;
import timywimy.web.dto.security.User;

import java.util.concurrent.TimeUnit;

@ContextConfiguration({
        "classpath:spring/spring-db-test.xml", "classpath:spring/spring-app-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = {"classpath:properties/api-test.properties"})
@Sql(scripts = {
        "classpath:db/postgresql/0-delete-all-data.sql",
        "classpath:db/postgresql/1-init-users.sql"},
        config = @SqlConfig(encoding = "UTF-8"))
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

    //todo  no email,no pass, no name
    //todo  invalid email, invalid pass
    //todo  already exists
    @Test
    public void validRegister() {
        service.register(UserTestData.getNewUserDTO());
    }

    //todo no email,no pass, no name
    //todo invalid email, invalid pass
    //todo user not found
    @Test
    public void validOpenSession() {
        service.openSession(UserTestData.getExistingUserDTO());
    }

    @Test
    public void validCloseSession() {
        Session session = service.openSession(UserTestData.getExistingUserDTO());
        service.closeSession(session.getSession());
    }

    @Test
    public void validUpdateProfile() {
        User existingUserDTO = UserTestData.getExistingUserDTO();
        Session session = service.openSession(existingUserDTO);
        existingUserDTO.setName("ALFRED");
        existingUserDTO.setPassword(null);
        existingUserDTO.setEmail(null);
        service.updateProfile(session.getSession(), existingUserDTO);
    }


    @Test
    public void validGetUserBySession() {
        Session session = service.openSession(UserTestData.getExistingUserDTO());
        service.getUserBySession(session.getSession());
    }

    @Test
    @Ignore //invoce only if testing by hand
    public void validEvictUsers() throws InterruptedException {
        Session session = service.openSession(UserTestData.getExistingUserDTO());
        timywimy.model.security.User userBySession = service.getUserBySession(session.getSession());
        Assert.assertNotEquals(null, userBySession);
        Thread.sleep(125000);
        userBySession = service.getUserBySession(session.getSession());
        Assert.assertEquals(null, userBySession);

    }
}
