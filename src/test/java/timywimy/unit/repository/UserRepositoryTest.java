package timywimy.unit.repository;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import timywimy.model.security.User;
import timywimy.model.security.converters.Role;
import timywimy.repository.UserRepository;
import timywimy.util.UserTestData;
import timywimy.util.exception.RepositoryException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@ContextConfiguration({
        "classpath:spring/spring-db-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = {
        "classpath:db/postgresql/0-delete-all-data.sql",
        "classpath:db/postgresql/1-init-users.sql"},
        config = @SqlConfig(encoding = "UTF-8"))
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
    public void getValidById() throws Exception {
        //all users exist
        User root = repository.get(UserTestData.ROOT_ID);
        Assert.assertNotNull(root);
        Assert.assertEquals(root.getId(), UserTestData.ROOT_ID);

        User admin = repository.get(UserTestData.ADMIN_ID);
        Assert.assertNotNull(admin);
        Assert.assertEquals(admin.getId(), UserTestData.ADMIN_ID);
        Assert.assertEquals(admin.getRole(), Role.ADMIN);

        User user = repository.get(UserTestData.USER_ID);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getId(), UserTestData.USER_ID);
        Assert.assertEquals(user.getRole(), Role.USER);
        //user is not in DB
        Assert.assertNull(repository.get(UserTestData.NO_EXISTENT_USER_ID));
//        //user is deleted (deleted_ts not null)
//        Assert.assertNull(repository.get(UserTestData.INACTIVE_USER_ID));
        //unknown role
        User unknown = repository.get(UserTestData.INVALID_ROLE_USER_ID);
        Assert.assertNotNull(unknown);
        Assert.assertEquals(unknown.getRole(), Role.UNKNOWN);
    }

    @Test
    public void getInvalidNullId() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("entityId should be provided");
        repository.get(null);
    }

    @Test
    public void getValidByEmail() throws Exception {
        User user = repository.get(UserTestData.USER_ID);
        User byEmail = repository.getByEmail(user.getEmail());
        Assert.assertNotNull(byEmail);
        Assert.assertEquals(user.getId(), byEmail.getId());
        Assert.assertEquals(user.getEmail(), byEmail.getEmail());
    }

    @Test
    public void getInvalidNoEmail() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("email should be provided");
        repository.getByEmail(null);
    }

    //save
    @Test
    public void saveValid() throws Exception {
        //create user
        User createdUser = repository.save(UserTestData.getNewUser(), UserTestData.ROOT_ID);
        Assert.assertNotNull(createdUser);
        Assert.assertNotNull(repository.get(createdUser.getId()));
        //update user
        createdUser.setName("WILLY BILLY");
        User updatedUser = repository.save(createdUser, UserTestData.ROOT_ID);
        Assert.assertNotNull(updatedUser);
        Assert.assertNotNull(repository.get(updatedUser.getId()));
        Assert.assertEquals(repository.get(updatedUser.getId()).getName(), "WILLY BILLY");
    }

    @Test
    public void saveInvalidNoEntity() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("entity should be provided");
        repository.save(null, UserTestData.ROOT_ID);
    }

    @Test
    public void saveInvalidNoUserId() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("user should be provided");
        repository.save(UserTestData.getNewUser(), null);
    }

    @Test
    public void saveInvalidNoEmail() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("user email should be provided");
        User newUser = UserTestData.getNewUser();
        newUser.setEmail("");
        repository.save(newUser, UserTestData.ROOT_ID);
    }

    @Test
    public void saveInvalidNoPassword() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("user password should be provided");
        User newUser = UserTestData.getNewUser();
        newUser.setPassword("");
        repository.save(newUser, UserTestData.ROOT_ID);
    }

    @Test
    public void saveInvalidNoName() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("user name should be provided");
        User newUser = UserTestData.getNewUser();
        newUser.setName("");
        repository.save(newUser, UserTestData.ROOT_ID);
    }

    @Test
    public void saveInvalidDuplicateEmail() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("User with this email already registered");
        User newUser = UserTestData.getNewUser();
        newUser.setEmail(repository.get(UserTestData.USER_ID).getEmail());
        repository.save(newUser, UserTestData.ROOT_ID);
    }

//    @Test
//    public void saveInvalidWithDeletedTs() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("deleteTs can't be used with update");
//        User newUser = UserTestData.getNewUser();
//        newUser.setDeletedTs(ZonedDateTime.now());
//        repository.save(newUser, UserTestData.ROOT_ID);
//    }

    //save deleted user
    @Test
    public void deleteValid() throws Exception {
        //exists
        boolean existed = repository.delete(UserTestData.USER_ID);
        Assert.assertEquals(existed, true);
        Assert.assertNull(repository.get(UserTestData.USER_ID));
        //not exists
        boolean notExisted = repository.delete(UserTestData.NO_EXISTENT_USER_ID);
        Assert.assertEquals(notExisted, false);
        Assert.assertNull(repository.get(UserTestData.NO_EXISTENT_USER_ID));
//        //deleted
//        boolean deleted = repository.delete(UserTestData.INACTIVE_USER_ID, UserTestData.ROOT_ID);
//        Assert.assertEquals(deleted, false);
//        Assert.assertNull(repository.get(UserTestData.INACTIVE_USER_ID));
    }

    @Test
    public void deleteInvalidNoEntityId() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("entity id should be provided");
        repository.delete((UUID)null);
    }

//    @Test
//    public void deleteInvalidNoUser() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("user id should be provided");
//        repository.delete(UserTestData.USER_ID);
//    }

    @Test
    public void getAllValid() throws Exception {
        List<User> all = repository.getAll();
        Assert.assertNotNull(all);
        Assert.assertEquals(all.size(), 5);
        for (int i = 0; i < all.size(); i++) {

        }
    }
}
