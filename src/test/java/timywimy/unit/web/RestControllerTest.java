package timywimy.unit.web;

import org.junit.AfterClass;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import timywimy.util.RequestUtil;
import timywimy.util.UserTestData;
import timywimy.web.controllers.RestAppController;
import timywimy.web.dto.Session;
import timywimy.web.dto.User;
import timywimy.web.dto.common.Response;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@ContextConfiguration({
        "classpath:spring/spring-db-test.xml",
        "classpath:spring/spring-app-test.xml",
        "classpath:spring/spring-mvc-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestPropertySource(locations = {"classpath:properties/api-test.properties"})
@Sql(scripts = {"classpath:db/postgresql/1-init-users.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class RestControllerTest {
    private static final Logger log = LoggerFactory.getLogger("result");


    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @Autowired
    private RestAppController restController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

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

//    @PostConstruct
//    private void postConstruct() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .addFilter(CHARACTER_ENCODING_FILTER)
//                .build();
//    }


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
    public void validInfo() {
        Integer requestId = RequestUtil.getRandomRequestId();
        restController.info(requestId);
    }

    //todo  no email,no pass, no name
    //todo  invalid email, invalid pass
    //todo  already exists
    @Test
    public void validRegister() {
        Integer requestId = RequestUtil.getRandomRequestId();
        restController.register(requestId, UserTestData.getNewUserDTO());
    }

    //todo no email,no pass, no name
    //todo invalid email, invalid pass
    //todo user not found
    @Test
    public void validOpenSession() {
        Integer requestId = RequestUtil.getRandomRequestId();
        restController.openSession(requestId, UserTestData.getExistingUserDTO());
    }

    @Test
    public void validCloseSession() {
        Integer requestId = RequestUtil.getRandomRequestId();
        Response<Session> sessionResponse = restController.openSession(requestId, UserTestData.getExistingUserDTO());
        restController.closeSession(requestId, sessionResponse.getResponse().getSession());
    }

    @Test
    public void validUpdateProfile() {
        Integer requestId = RequestUtil.getRandomRequestId();
        User existingUserDTO = UserTestData.getExistingUserDTO();
        Response<Session> sessionResponse = restController.openSession(requestId, existingUserDTO);
        existingUserDTO.setName("ALFRED");
        existingUserDTO.setPassword(null);
        existingUserDTO.setEmail(null);
        restController.updateProfile(requestId, sessionResponse.getResponse().getSession(), sessionResponse.getResponse().getUser());
    }
}
