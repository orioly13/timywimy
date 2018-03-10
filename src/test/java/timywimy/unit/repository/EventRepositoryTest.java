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
import timywimy.model.bo.tasks.Task;
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
        List<AbstractEventExtension> abstractEventExtensions = repository.addExtensions(event.getId(), tickers, UserTestData.USER_ID);
        Assert.assertEquals(2, abstractEventExtensions.size());
//        Event event2 = repository.get(event.getId(), parameters);
    }

    @Test
    public void deleteExtensions() throws Exception {
//        repository.get
        Event event = EventTestData.getEventExistingWithExtensions();
        Set<String> parameters = new HashSet<>();
        parameters.add("extensions");

        Event event1 = repository.get(event.getId(), parameters);
        Assert.assertEquals(2, event1.getExtensions().size());

        List<AbstractEventExtension> extensionsToDelete = new ArrayList<>();
        extensionsToDelete.add(event1.getExtensions().get(0));
        List<AbstractEventExtension> abstractEventExtensions =
                repository.deleteExtensions(event.getId(), extensionsToDelete, UserTestData.USER_ID);
        Assert.assertEquals(1, abstractEventExtensions.size());
    }

    @Test
    public void updateExtensions() throws Exception {
//        repository.get
        Event event = EventTestData.getEventExistingWithExtensions();
        Set<String> parameters = new HashSet<>();
        parameters.add("extensions");

        Event event1 = repository.get(event.getId(), parameters);
        Assert.assertEquals(2, event1.getExtensions().size());

        List<AbstractEventExtension> extensionsToUpdate = new ArrayList<>();
//        AbstractEventExtension extension = ;
        ((CounterExtension) event1.getExtensions().get(0)).setCounter(15);
        ((TickBoxExtension) event1.getExtensions().get(1)).setActive(true);

        extensionsToUpdate.addAll(event1.getExtensions());
        List<AbstractEventExtension> abstractEventExtensions = repository.updateExtensions(event.getId(), extensionsToUpdate, UserTestData.USER_ID);
//        Event event2 = repository.get(event.getId(), parameters);
        Assert.assertEquals(abstractEventExtensions.size(), 2);

        for (AbstractEventExtension extension : abstractEventExtensions) {
            if (extension instanceof CounterExtension) {
                Assert.assertEquals(15, ((CounterExtension) extension).getCounter());
            } else if (extension instanceof TickBoxExtension) {
                Assert.assertEquals(true, ((TickBoxExtension) extension).isActive());
            } else {
                Assert.fail("Unknown extension type");
            }
        }
    }

    //
    @Test
    public void cascadeRemove() throws Exception {
        Event eventExistingWithExtensions = EventTestData.getEventExistingWithExtensions();
        //does not work because i remove stuff using ID
        repository.delete(eventExistingWithExtensions);
        Event event = repository.get(eventExistingWithExtensions.getId());
        Assert.assertEquals(null, event);
    }

    @Test
    public void removeOrphanTest() throws Exception {
        Event eventExistingWithExtensions = EventTestData.getEventExistingWithExtensions();
        Set<String> parameters = new HashSet<>();
        parameters.add("extensions");

        Event event = repository.get(eventExistingWithExtensions.getId(), parameters);
        event.getExtensions().remove(0);
        repository.save(event, UserTestData.USER_ID);

        List<AbstractEventExtension> extensions = repository.get(eventExistingWithExtensions.getId(), parameters).getExtensions();
        Assert.assertEquals(1, extensions.size());
    }

    //
    @Test
    public void addNewTasks() throws Exception {
//        repository.get
        Event event = EventTestData.getEventExisting();
        Set<String> parameters = new HashSet<>();
        parameters.add("tasks");

        Task task1 = new Task();
        task1.setName("task1");
        task1.setOwner(UserTestData.getExistingUser());
        task1.setDescription("task1_desc");
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        List<Task> tasks1 = repository.addTasks(event.getId(), tasks, UserTestData.USER_ID);
        Assert.assertEquals(1, tasks1.size());
//        Event event2 = repository.get(event.getId(), parameters);
    }

    @Test
    public void deleteTasks() throws Exception {
//        repository.get
        Event event = EventTestData.getEventExistingWithTask();
        Set<String> parameters = new HashSet<>();
        parameters.add("tasks");

        Event event1 = repository.get(event.getId(), parameters);
        Assert.assertEquals(1, event1.getTasks().size());

        List<Task> extensionsToDelete = new ArrayList<>();
        extensionsToDelete.add(event1.getTasks().get(0));
        List<Task> tasks = repository.deleteTasks(event.getId(), extensionsToDelete, UserTestData.USER_ID);
        Assert.assertEquals(0, tasks.size());
    }

    @Test
    public void updateTasks() throws Exception {
//        repository.get
        Event event = EventTestData.getEventExistingWithTask();
        Set<String> parameters = new HashSet<>();
        parameters.add("tasks");

        Event event1 = repository.get(event.getId(), parameters);
        Assert.assertEquals(1, event1.getTasks().size());

        List<Task> tasks = new ArrayList<>();
//        AbstractEventExtension extension = ;
        event1.getTasks().get(0).setCompleted(true);

        tasks.addAll(event1.getTasks());
        List<Task> tasks1 = repository.updateTasks(event.getId(), tasks, UserTestData.USER_ID);
//        Event event2 = repository.get(event.getId(), parameters);
        Assert.assertEquals(tasks1.size(), 1);

        for (Task task : tasks1) {
            Assert.assertEquals(true, task.isCompleted());
        }
    }

}
