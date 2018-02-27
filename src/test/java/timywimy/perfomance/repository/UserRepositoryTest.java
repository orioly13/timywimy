package timywimy.perfomance.repository;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import timywimy.model.security.User;
import timywimy.repository.UserRepository;
import timywimy.util.UserTestData;

import java.util.concurrent.TimeUnit;


@ContextConfiguration({
        "classpath:spring/spring-db-test.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/postgresql/1-init-users.sql"}, config = @SqlConfig(encoding = "UTF-8"))
@Ignore
public class UserRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    @Autowired
    private UserRepository repository;

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

    //GET
    @Test
    public void create() throws Exception {
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setName("User " + i);
            user.setPassword("user_pass_" + i);
            user.setEmail("user" + i + "@user.test");
            repository.save(user, UserTestData.ROOT_ID);
        }
    }

//    @Before
//    public void beforeGetAndUpdate() throws Exception {
//        create();
//    }

    @Test
    public void getAll() throws Exception {
        log.info("getAll");
//        List<User>
//        for (int i = 0; i < 1000; i++) {
//            User user = new User();
//            user.setName("User " + i);
//            user.setPassword("user_pass_" + i);
//            user.setEmail("user" + i + "@user.test");
//            repository.save(user, UserTestData.ROOT_ID);
//        }
    }

    @Test
    public void getByEmailALot() throws Exception {
        log.info("byEmail");
//        for (int i = 0; i < 1000; i++) {
//            User user = new User();
//            user.setName("User " + i);
//            user.setPassword("user_pass_" + i);
//            user.setEmail("user" + i + "@user.test");
//            repository.save(user, UserTestData.ROOT_ID);
//        }
    }

    @Test
    public void deleteALot() throws Exception {
        log.info("byEmail");
//        for (int i = 0; i < 1000; i++) {
//            User user = new User();
//            user.setName("User " + i);
//            user.setPassword("user_pass_" + i);
//            user.setEmail("user" + i + "@user.test");
//            repository.save(user, UserTestData.ROOT_ID);
//        }
    }



}
