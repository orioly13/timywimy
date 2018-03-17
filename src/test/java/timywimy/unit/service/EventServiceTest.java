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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import timywimy.model.bo.tasks.Task;
import timywimy.service.RestService;
import timywimy.service.entities.EventService;
import timywimy.service.entities.TaskService;
import timywimy.util.EventTestData;
import timywimy.util.TaskTestData;
import timywimy.util.UserTestData;
import timywimy.util.exception.RepositoryException;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.extensions.CounterExtension;
import timywimy.web.dto.events.extensions.EventExtension;
import timywimy.web.dto.security.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@ContextConfiguration({
        "classpath:spring/spring-db-test.xml", "classpath:spring/spring-app-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = {
        "classpath:db/postgresql/0-delete-all-data.sql",
        "classpath:db/postgresql/1-init-users.sql",
        "classpath:db/postgresql/2-init-events.sql"},
        config = @SqlConfig(encoding = "UTF-8"))
public class EventServiceTest {
    private static final Logger log = LoggerFactory.getLogger("result");

    @Autowired
    private EventService service;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RestService restService;

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
        Session session = restService.openSession(UserTestData.getExistingUserDTO());
        //all users exist
        Event event1 = service.get(EventTestData.EVENT_1, session.getSession());
        Assert.assertNotNull(event1);
        Assert.assertEquals(EventTestData.EVENT_1, event1.getId());

        Event event2 = service.get(EventTestData.EVENT_2, session.getSession());
        Assert.assertNotNull(event2);
        Assert.assertEquals(EventTestData.EVENT_2, event2.getId());

        Event event3 = service.get(EventTestData.EVENT_3, session.getSession());
        Assert.assertNotNull(event3);
        Assert.assertEquals(EventTestData.EVENT_3, event3.getId());

        Event event4 = service.get(EventTestData.EVENT_4, session.getSession());
        Assert.assertNotNull(event4);
        Assert.assertEquals(EventTestData.EVENT_4, event4.getId());
    }

    //
    @Test
    public void saveValidEvent() throws Exception {
        Event event = EventTestData.getEventNewDTO();
        Event save = service.save(event,
                restService.openSession(UserTestData.getExistingUserDTO()).getSession());
        Assert.assertNotNull(save.getId());

    }

    //
//    @Test
//    public void saveInvalidNullOwner() throws Exception {
//        thrown.expect(RepositoryException.class);
//        thrown.expectMessage("owner should be provided");
//        Event event = EventTestData.getEventExisting();
//        event.setOwner(null);
//        service.save(event, UserTestData.USER_ID);
//    }

//    @Test
//    public void deleteWithTasks() throws Exception {
//        Event event = EventTestData.getEventExistingWithTask();
//        Set<String> parameters = new HashSet<>();
//        parameters.add("tasks");
//
//        Event event1 = service.get(event.getId(), parameters);
//        Assert.assertEquals(1, event1.getTasks().size());
//        Task task = event1.getTasks().get(0);
//
//        service.delete(event1.getId());
//        Set<String> parameters1 = new HashSet<>();
//        parameters.add("event");
//        Task task1 = taskService.get(task.getId(), parameters1);
//        Assert.assertNotNull(task1);
//        Assert.assertNull(task1.getEvent());
//    }

    //
//    @Test
//    public void addNewExtensions() throws Exception {
////        service.get
//        Event event = EventTestData.get();
//        Set<String> parameters = new HashSet<>();
//        parameters.add("extensions");
//
//        EventExtension extension1 = new CounterExtension();
//        List<EventExtension> counters = new ArrayList<>();
//        counters.add(extension1);
//        service.addExtensions(event.getId(), counters, UserTestData.USER_ID);
//        Event event1 = service.get(event.getId(), parameters);
//        Assert.assertEquals(1, event1.getExtensions().size());
//
//        AbstractEventExtension extension2 = new TickBoxExtension();
//        List<AbstractEventExtension> tickers = new ArrayList<>();
//        tickers.add(extension2);
//        Event event2 = service.addExtensions(event.getId(), tickers, UserTestData.USER_ID);
//        Assert.assertEquals(2, event2.getExtensions().size());
////        Event event2 = service.get(event.getId(), parameters);
//    }

//    @Test
//    public void deleteExtensions() throws Exception {
////        service.get
//        Event event = EventTestData.getEventExistingWithExtensions();
//        Set<String> parameters = new HashSet<>();
//        parameters.add("extensions");
//
//        Event event1 = service.get(event.getId(), parameters);
//        Assert.assertEquals(2, event1.getExtensions().size());
//
//        List<AbstractEventExtension> extensionsToDelete = new ArrayList<>();
//        extensionsToDelete.add(event1.getExtensions().get(0));
//        Event event2 =
//                service.deleteExtensions(event.getId(), extensionsToDelete, UserTestData.USER_ID);
//        Assert.assertEquals(1, event2.getExtensions().size());
//    }

//    @Test
//    public void updateExtensions() throws Exception {
////        service.get
//        Event event = EventTestData.getEventExistingWithExtensions();
//        Set<String> parameters = new HashSet<>();
//        parameters.add("extensions");
//
//        Event event1 = service.get(event.getId(), parameters);
//        Assert.assertEquals(2, event1.getExtensions().size());
//
//        List<AbstractEventExtension> extensionsToUpdate = new ArrayList<>();
////        AbstractEventExtension extension = ;
//        ((CounterExtension) event1.getExtensions().get(0)).setCounter(15);
//        ((TickBoxExtension) event1.getExtensions().get(1)).setActive(true);
//
//        extensionsToUpdate.addAll(event1.getExtensions());
//        Event event2 = service.updateExtensions(event.getId(), extensionsToUpdate, UserTestData.USER_ID);
////        Event event2 = service.get(event.getId(), parameters);
//        Assert.assertEquals(event2.getExtensions().size(), 2);
//
//        for (AbstractEventExtension extension : event2.getExtensions()) {
//            if (extension instanceof CounterExtension) {
//                Assert.assertEquals(15, ((CounterExtension) extension).getCounter());
//            } else if (extension instanceof TickBoxExtension) {
//                Assert.assertEquals(true, ((TickBoxExtension) extension).isActive());
//            } else {
//                Assert.fail("Unknown extension type");
//            }
//        }
//    }

    //
//    @Test
//    public void cascadeRemove() throws Exception {
//        Event eventExistingWithExtensions = EventTestData.getEventExistingWithExtensions();
//        //does not work because i remove stuff using ID
//        service.delete(eventExistingWithExtensions);
//        Event event = service.get(eventExistingWithExtensions.getId());
//        Assert.assertEquals(null, event);
//    }
//
//    @Test
//    public void removeOrphanTest() throws Exception {
//        Event eventExistingWithExtensions = EventTestData.getEventExistingWithExtensions();
//        Set<String> parameters = new HashSet<>();
//        parameters.add("extensions");
//
//        Event event = service.get(eventExistingWithExtensions.getId(), parameters);
//        event.getExtensions().remove(0);
//        service.save(event, UserTestData.USER_ID);
//
//        List<AbstractEventExtension> extensions = service.get(eventExistingWithExtensions.getId(), parameters).getExtensions();
//        Assert.assertEquals(1, extensions.size());
//    }

    //
//    @Test
//    public void addNewTasks() throws Exception {
////        service.get
//        Event event = EventTestData.getEventExisting();
////        Set<String> parameters = new HashSet<>();
////        parameters.add("tasks");
//
//        Task task1 = new Task();
//        task1.setId(TaskTestData.TASK_3);
//
//        List<Task> tasks = new ArrayList<>();
//        tasks.add(task1);
//        List<Task> tasks1 = service.linkTasks(event.getId(), tasks, UserTestData.USER_ID);
//        Assert.assertEquals(1, tasks1.size());
////        Event event2 = service.get(event.getId(), parameters);
//    }
//
//    @Test
//    public void deleteTasks() throws Exception {
////        service.get
//        Event event = EventTestData.getEventExistingWithTask();
//        Set<String> parameters = new HashSet<>();
//        parameters.add("tasks");
//
//        Event event1 = service.get(event.getId(), parameters);
//        Assert.assertEquals(1, event1.getTasks().size());
//
//        List<Task> tasksToUnlink = new ArrayList<>();
//        tasksToUnlink.add(event1.getTasks().get(0));
//        List<Task> tasks = service.unlinkTasks(event.getId(), tasksToUnlink, UserTestData.USER_ID);
//        Assert.assertEquals(0, tasks.size());
//    }


}
