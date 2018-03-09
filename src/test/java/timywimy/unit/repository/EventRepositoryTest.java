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
import timywimy.model.bo.events.Event;
import timywimy.model.bo.events.extensions.CounterExtension;
import timywimy.model.bo.events.extensions.TickBoxExtension;
import timywimy.model.bo.events.extensions.common.AbstractEventExtension;
import timywimy.repository.EventRepository;
import timywimy.util.EventTestData;
import timywimy.util.UserTestData;
import timywimy.util.exception.RepositoryException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@ContextConfiguration({
        "classpath:spring/spring-db-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = {
        "classpath:db/postgresql/0-delete-all-data.sql",
        "classpath:db/postgresql/1-init-users.sql",
        "classpath:db/postgresql/2-init-events.sql"},
        config = @SqlConfig(encoding = "UTF-8"))
public class EventRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    @Autowired
    private EventRepository repository;

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
        Event event1 = repository.get(EventTestData.EVENT_1);
        Assert.assertNotNull(event1);
        Assert.assertEquals(EventTestData.EVENT_1, event1.getId());

        Event event2 = repository.get(EventTestData.EVENT_2);
        Assert.assertNotNull(event2);
        Assert.assertEquals(EventTestData.EVENT_2, event2.getId());

        Event event3 = repository.get(EventTestData.EVENT_3);
        Assert.assertNotNull(event3);
        Assert.assertEquals(EventTestData.EVENT_3, event3.getId());

        Event event4 = repository.get(EventTestData.EVENT_4);
        Assert.assertNotNull(event4);
        Assert.assertEquals(EventTestData.EVENT_4, event4.getId());
    }

    //
    @Test
    public void saveValidEvent() throws Exception {
        Event event = EventTestData.getEventNew();
        Event save = repository.save(event, UserTestData.USER_ID);
        Assert.assertEquals(event.getId(), save.getId());

    }

    //
    @Test
    public void saveInvalidNullOwner() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("owner should be provided");
        Event event = EventTestData.getEventExisting();
        event.setOwner(null);
        repository.save(event, UserTestData.USER_ID);
    }

    //
    @Test
    public void addNewExtensions() throws Exception {
//        repository.get
        Event event = EventTestData.getEventExisting();
        Set<String> parameters = new HashSet<>();
        parameters.add("extensions");

        AbstractEventExtension extension1 = new CounterExtension();
        List<AbstractEventExtension> counters = new ArrayList<>();
        counters.add(extension1);
        repository.addExtensions(event.getId(), counters, UserTestData.USER_ID);
        Event event1 = repository.get(event.getId(), parameters);
        Assert.assertEquals(1, event1.getExtensions().size());

        AbstractEventExtension extension2 = new TickBoxExtension();
        List<AbstractEventExtension> tickers = new ArrayList<>();
        tickers.add(extension2);
        repository.addExtensions(event.getId(), tickers, UserTestData.USER_ID);
        Event event2 = repository.get(event.getId(), parameters);
        Assert.assertEquals(2, event2.getExtensions().size());


    }

    @Test
    public void deleteExtensions() throws Exception {
//        repository.get
        Event event = EventTestData.getEventExisting();
        Set<String> parameters = new HashSet<>();
        parameters.add("extensions");

        AbstractEventExtension extension1 = new CounterExtension();
        AbstractEventExtension extension2 = new TickBoxExtension();
        List<AbstractEventExtension> extensions = new ArrayList<>();
        extensions.add(extension1);
        extensions.add(extension2);
        repository.addExtensions(event.getId(), extensions, UserTestData.USER_ID);
        Event event1 = repository.get(event.getId(), parameters);
        Assert.assertEquals(2, event1.getExtensions().size());

        List<AbstractEventExtension> extensionsToDelete = new ArrayList<>();
        extensionsToDelete.add(event1.getExtensions().get(0));
        repository.deleteExtensions(event.getId(), extensionsToDelete, UserTestData.USER_ID);
        Event event2 = repository.get(event.getId(), parameters);
        Assert.assertEquals(1, event2.getExtensions().size());
    }

    @Test
    public void updateExtensions() throws Exception {
//        repository.get
        Event event = EventTestData.getEventExisting();
        Set<String> parameters = new HashSet<>();
        parameters.add("extensions");

        AbstractEventExtension extension1 = new CounterExtension();
        ((CounterExtension) extension1).setOrder(0);
        AbstractEventExtension extension2 = new TickBoxExtension();
        ((TickBoxExtension) extension2).setOrder(1);
        List<AbstractEventExtension> extensions = new ArrayList<>();
        extensions.add(extension1);
        extensions.add(extension2);
        repository.addExtensions(event.getId(), extensions, UserTestData.USER_ID);
        Event event1 = repository.get(event.getId(), parameters);
        Assert.assertEquals(2, event1.getExtensions().size());

        List<AbstractEventExtension> extensionsToUpdate = new ArrayList<>();
        AbstractEventExtension extension = event1.getExtensions().get(0);
        if (extension instanceof CounterExtension) {
            ((CounterExtension) extension).setCounter(15);
        } else if (extension instanceof TickBoxExtension) {
            ((TickBoxExtension) extension).setActive(true);
        }
        extensionsToUpdate.add(extension);
        repository.updateExtensions(event.getId(), extensionsToUpdate, UserTestData.USER_ID);
        Event event2 = repository.get(event.getId(), parameters);
        Assert.assertEquals(event2.getExtensions().size(), 2);

        if (extension instanceof CounterExtension) {
            for (AbstractEventExtension extension3 : event2.getExtensions()) {
                if (extension3 instanceof CounterExtension) {
                    Assert.assertEquals(15, ((CounterExtension) event2.getExtensions().get(0)).getCounter());
                }
            }
        } else if (extension instanceof TickBoxExtension) {
            for (AbstractEventExtension extension3 : event2.getExtensions()) {
                if (extension3 instanceof TickBoxExtension) {
                    Assert.assertEquals(true, ((TickBoxExtension) event2.getExtensions().get(0)).isActive());
                }
            }
        }
    }
//
//    @Test
//    public void getInvalidNoEmail() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("email should be provided");
//        repository.getByEmail(null);
//    }
//
//    //save
//    @Test
//    public void saveValid() throws Exception {
////        User newUser = UserTestData.getNewUser();
//        //update not existent user (results in creating new instance)
////        newUser.setId(UserTestData.NO_EXISTENT_USER_ID);
////        User notExisted = repository.save(newUser, UserTestData.ROOT_ID);
////        Assert.assertNull(notExisted);
////        Assert.assertNull(repository.get(UserTestData.NO_EXISTENT_USER_ID));
//        //update deleted user
////        newUser.setId(UserTestData.INACTIVE_USER_ID);
////        User deletedUser = repository.save(newUser, UserTestData.ROOT_ID);
////        Assert.assertNull(deletedUser);
////        Assert.assertNull(repository.get(UserTestData.INACTIVE_USER_ID));
//
//        //create user
//        User createdUser = repository.save(UserTestData.getNewUser(), UserTestData.ROOT_ID);
//        Assert.assertNotNull(createdUser);
//        Assert.assertNotNull(repository.get(createdUser.getId()));
//        //update user
//        createdUser.setName("WILLY BILLY");
//        User updatedUser = repository.save(createdUser, UserTestData.ROOT_ID);
//        Assert.assertNotNull(updatedUser);
//        Assert.assertNotNull(repository.get(updatedUser.getId()));
//        Assert.assertEquals(repository.get(updatedUser.getId()).getName(), "WILLY BILLY");
//    }
//
//    @Test
//    public void saveInvalidNoEntity() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("entity should be provided");
//        repository.save(null, UserTestData.ROOT_ID);
//    }
//
//    @Test
//    public void saveInvalidNoUserId() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("user should be provided");
//        repository.save(UserTestData.getNewUser(), null);
//    }
//
//    @Test
//    public void saveInvalidNoEmail() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("user email should be provided");
//        User newUser = UserTestData.getNewUser();
//        newUser.setEmail("");
//        repository.save(newUser, UserTestData.ROOT_ID);
//    }
//
//    @Test
//    public void saveInvalidNoPassword() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("user password should be provided");
//        User newUser = UserTestData.getNewUser();
//        newUser.setPassword("");
//        repository.save(newUser, UserTestData.ROOT_ID);
//    }
//
//    @Test
//    public void saveInvalidNoName() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("user name should be provided");
//        User newUser = UserTestData.getNewUser();
//        newUser.setName("");
//        repository.save(newUser, UserTestData.ROOT_ID);
//    }
//
//    @Test
//    public void saveInvalidDuplicateEmail() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("User with this email already registered");
//        User newUser = UserTestData.getNewUser();
//        newUser.setEmail(repository.get(UserTestData.USER_ID).getEmail());
//        repository.save(newUser, UserTestData.ROOT_ID);
//    }
//
////    @Test
////    public void saveInvalidWithDeletedTs() throws Exception {
////        thrown.expect(RepositoryException.class);
////        thrown.expectMessage("deleteTs can't be used with update");
////        User newUser = UserTestData.getNewUser();
////        newUser.setDeletedTs(ZonedDateTime.now());
////        repository.save(newUser, UserTestData.ROOT_ID);
////    }
//
//    //save deleted user
//    @Test
//    public void deleteValid() throws Exception {
//        //exists
//        boolean existed = repository.delete(UserTestData.USER_ID, UserTestData.ROOT_ID);
//        Assert.assertEquals(existed, true);
//        Assert.assertNull(repository.get(UserTestData.USER_ID));
//        //not exists
//        boolean notExisted = repository.delete(UserTestData.NO_EXISTENT_USER_ID, UserTestData.ROOT_ID);
//        Assert.assertEquals(notExisted, false);
//        Assert.assertNull(repository.get(UserTestData.NO_EXISTENT_USER_ID));
////        //deleted
////        boolean deleted = repository.delete(UserTestData.INACTIVE_USER_ID, UserTestData.ROOT_ID);
////        Assert.assertEquals(deleted, false);
////        Assert.assertNull(repository.get(UserTestData.INACTIVE_USER_ID));
//    }
//
//    @Test
//    public void deleteInvalidNoEntityId() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("entity id should be provided");
//        repository.delete(null, UserTestData.ROOT_ID);
//    }
//
//    @Test
//    public void deleteInvalidNoUser() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("user id should be provided");
//        repository.delete(UserTestData.USER_ID, null);
//    }
//
//    @Test
//    public void getAllValid() throws Exception {
//        List<User> all = repository.getAll();
//        Assert.assertNotNull(all);
//        Assert.assertEquals(all.size(), 5);
//        //todo check sort
//    }
}
