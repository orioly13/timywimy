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
import timywimy.model.bo.events.Schedule;
import timywimy.model.bo.tasks.Task;
import timywimy.repository.EventRepository;
import timywimy.repository.ScheduleRepository;
import timywimy.repository.TaskRepository;
import timywimy.util.ScheduleTestData;
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
        "classpath:db/postgresql/4-init-schedule.sql"},
        config = @SqlConfig(encoding = "UTF-8"))
public class ScheduleRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    @Autowired
    private ScheduleRepository repository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TaskRepository taskRepository;

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
        Schedule schedule = repository.get(ScheduleTestData.SCHEDULE_1);
        Assert.assertNotNull(schedule);
        Assert.assertEquals(ScheduleTestData.SCHEDULE_1, schedule.getId());
    }

    //
    @Test
    public void saveValidEvent() throws Exception {
        Schedule schedule = ScheduleTestData.getScheduleNew();
        Schedule save = repository.save(schedule, UserTestData.USER_ID);
        Assert.assertEquals(schedule.getId(), save.getId());

    }

    //
    @Test
    public void saveInvalidNullOwner() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("owner should be provided");
        Schedule schedule = ScheduleTestData.getScheduleExisting();
        schedule.setOwner(null);
        repository.save(schedule, UserTestData.USER_ID);
    }

    @Test
    public void deleteWithEvent() throws Exception {
        Schedule schedule = ScheduleTestData.getScheduleExistingWithEvents();
        Set<String> parameters = new HashSet<>();
        parameters.add("instances");

        Schedule schedule1 = repository.get(schedule.getId(), parameters);
        Assert.assertEquals(1, schedule1.getInstances().size());
        timywimy.model.bo.events.Event instance = schedule1.getInstances().get(0);

        repository.delete(schedule1.getId());
        timywimy.model.bo.events.Event instance1 = eventRepository.get(instance.getId());
        Assert.assertNull(instance1);
    }

    @Test
    public void deleteWithEventWithTask() throws Exception {
        Schedule schedule = ScheduleTestData.getScheduleExistingWithEventsTasks();
        Set<String> parameters = new HashSet<>();
        parameters.add("instances");

        Schedule schedule1 = repository.get(schedule.getId(), parameters);
        Assert.assertEquals(1, schedule1.getInstances().size());
        Set<String> parameters2 = new HashSet<>();
        parameters2.add("tasks");
        timywimy.model.bo.events.Event instance = eventRepository.get(schedule1.getInstances().get(0).getId(), parameters2);
        Task task = instance.getTasks().get(0);

        repository.delete(schedule1.getId());
        timywimy.model.bo.events.Event instance1 = eventRepository.get(instance.getId());
        Assert.assertNull(instance1);
        Set<String> parameters1 = new HashSet<>();
        parameters.add("event");
        Task childTask = taskRepository.get(task.getId(), parameters1);
        Assert.assertNotNull(childTask);
        Assert.assertNull(childTask.getEvent());
    }

    //
    @Test
    public void addNewInstances() throws Exception {
        Schedule schedule = ScheduleTestData.getScheduleExisting();

        Event event1 = new Event();
        event1.setName("event1");
        event1.setOwner(UserTestData.getExistingUser());
        event1.setDescription("event_desc");
        List<Event> events = new ArrayList<>();
        events.add(event1);
        List<Event> events1 = repository.addInstances(schedule.getId(), events, UserTestData.USER_ID);
        Assert.assertEquals(1, events1.size());
//        Event event2 = repository.get(event.getId(), parameters);
    }

    @Test
    public void deleteInstances() throws Exception {
////        repository.get
        Schedule schedule = ScheduleTestData.getScheduleExistingWithEvents();
        Set<String> parameters = new HashSet<>();
        parameters.add("instances");
//
        Schedule schedule1 = repository.get(schedule.getId(), parameters);
        Assert.assertEquals(1, schedule1.getInstances().size());
//
        List<Event> eventsToDelete = new ArrayList<>();
        eventsToDelete.add(schedule1.getInstances().get(0));
        List<Event> events = repository.deleteInstances(schedule1.getId(), eventsToDelete, UserTestData.USER_ID);
        Assert.assertEquals(0, events.size());
    }

//    @Test
//    public void updateInstances() throws Exception {
//        Schedule schedule = ScheduleTestData.getScheduleExistingWithEvents();
//        Set<String> parameters = new HashSet<>();
//        parameters.add("instances");
////
//        Schedule schedule1 = repository.get(schedule.getId(), parameters);
//        Assert.assertEquals(1, schedule1.getInstances().size());
//
//        schedule1.getInstances().get(0).setName("CHANGED EVENT");
//        List<Event> eventsToUpdate = new ArrayList<>();
//        eventsToUpdate.add(schedule1.getInstances().get(0));
//        List<Event> events = repository.updateInstances(schedule1.getId(), eventsToUpdate, UserTestData.USER_ID);
//        Assert.assertEquals(1, events.size());
//        Assert.assertEquals("CHANGED EVENT", events.get(0).getName());
////        }
//    }

}
