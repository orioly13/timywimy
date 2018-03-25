package timywimy.unit.web;

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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.filter.CharacterEncodingFilter;
import timywimy.util.PositiveTestData;
import timywimy.util.RequestUtil;
import timywimy.util.TimeFormatUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.ServiceException;
import timywimy.web.controllers.RestAppController;
import timywimy.web.controllers.entities.EventController;
import timywimy.web.controllers.entities.ScheduleController;
import timywimy.web.controllers.entities.TaskController;
import timywimy.web.controllers.entities.UserController;
import timywimy.web.dto.common.DateTimeZone;
import timywimy.web.dto.common.Response;
import timywimy.web.dto.events.Event;
import timywimy.web.dto.events.Schedule;
import timywimy.web.dto.events.extensions.CounterExtension;
import timywimy.web.dto.events.extensions.EventExtension;
import timywimy.web.dto.events.extensions.TickBoxExtension;
import timywimy.web.dto.security.User;
import timywimy.web.dto.tasks.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ContextConfiguration({
        "classpath:spring/spring-db-test.xml",
        "classpath:spring/spring-app-test.xml",
        "classpath:spring/spring-mvc-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestPropertySource(locations = {"classpath:properties/api-test.properties"})
@Sql(scripts = {
        "classpath:db/postgresql/0-generate-all.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class PositiveControllerTest {
    private static final Logger log = LoggerFactory.getLogger("result");


    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @Autowired
    private RestAppController restController;
    @Autowired
    private UserController userController;
    @Autowired
    private ScheduleController scheduleController;
    @Autowired
    private EventController eventController;
    @Autowired
    private TaskController taskController;

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

    //USERS
    @Test
    public void UserControllerTestPositive() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.openSession(requestId,
                PositiveTestData.USER_LOGIN_ADMIN).getResponse().getSession();
        //getAll
        Response<List<User>> all = userController.getAll(requestId, session);
        Assert.assertEquals(3, all.getResponse().size());
        //ban
        Response<Boolean> ban = userController.ban(requestId, PositiveTestData.USER_USER, session,
                "2018-03-25T01:20:21.903+04:00[Europe/Samara]");
        Assert.assertEquals(true, ban.getResponse());

        //byEmail
        Response<User> byEmail = userController.getByEmail(requestId,
                PositiveTestData.USER_LOGIN_USER.getEmail(), session);
        Assert.assertEquals("user", byEmail.getResponse().getName());
        //assert banned
        Assert.assertEquals(true, byEmail.getResponse().isBanned());
        Assert.assertEquals(TimeFormatUtil.parseZonedDateTime("2018-03-25T01:20:21.903+04:00[Europe/Samara]"),
                byEmail.getResponse().getBannedTill());
        //unban
        Response<Boolean> unban = userController.unBan(requestId, PositiveTestData.USER_USER, session);
        Assert.assertEquals(true, unban.getResponse());
        //byId
        Response<User> byId = userController.get(requestId, session, PositiveTestData.USER_USER);
        Assert.assertEquals("user", byEmail.getResponse().getName());
        //assert unbanned
        Assert.assertEquals(false, byId.getResponse().isBanned());
        Assert.assertNull(byId.getResponse().getBannedTill());
    }

    @Test
    public void UserControllerTestFailNoSession() {
        int requestId = RequestUtil.getRandomRequestId();
        try {
            userController.getAll(requestId, null);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            userController.ban(requestId, PositiveTestData.USER_USER, null,
                    "2018-03-25T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            userController.getByEmail(requestId,
                    PositiveTestData.USER_LOGIN_USER.getEmail(), null);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            userController.unBan(requestId, PositiveTestData.USER_USER, null);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            userController.get(requestId, null, PositiveTestData.USER_USER);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
    }

    @Test
    public void UserControllerTestFailSessionNotFound() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = UUID.randomUUID();
        try {
            userController.getAll(requestId, session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            userController.ban(requestId, PositiveTestData.USER_USER, session,
                    "2018-03-25T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            userController.getByEmail(requestId,
                    PositiveTestData.USER_LOGIN_USER.getEmail(), session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            userController.unBan(requestId, PositiveTestData.USER_USER, session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            userController.get(requestId, session, PositiveTestData.USER_USER);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
    }

    @Test
    public void UserControllerTestFailNotAdmin() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.openSession(requestId,
                PositiveTestData.USER_LOGIN_USER).getResponse().getSession();
        try {
            userController.getAll(requestId, session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            userController.ban(requestId, PositiveTestData.USER_USER, session,
                    "2018-03-25T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            userController.getByEmail(requestId,
                    PositiveTestData.USER_LOGIN_USER.getEmail(), session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            userController.unBan(requestId, PositiveTestData.USER_USER, session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            userController.get(requestId, session, PositiveTestData.USER_USER);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
    }

    @Test
    public void UserControllerTestFailRestricted() {
        UUID session = restController.openSession(RequestUtil.getRandomRequestId(),
                PositiveTestData.USER_LOGIN_ADMIN).getResponse().getSession();
        try {
            userController.delete(RequestUtil.getRandomRequestId(), session, PositiveTestData.USER_USER);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_OPERATION_RESTRICTED, e.getErrorCode());
        }
        try {
            userController.update(RequestUtil.getRandomRequestId(), session, PositiveTestData.USER_USER, PositiveTestData.USER_LOGIN_USER);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_OPERATION_RESTRICTED, e.getErrorCode());
        }
    }

    //SCHEDULES
    @Test
    public void ScheduleControllerTestPositive1() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_USER).
                getResponse().getSession();
        //getAll
        Response<List<Schedule>> all = scheduleController.getAll(requestId, session);
        Assert.assertEquals(2, all.getResponse().size());
        //getById
        Response<Schedule> byId = scheduleController.get(requestId, session, PositiveTestData.SCHED_1);
        Assert.assertEquals("sched_1", byId.getResponse().getName());
        //add instances
        List<Event> events = new ArrayList<>();
        Event event1 = new Event();
        event1.setName("NAME_1");
        event1.setDateTimeZone(new DateTimeZone(LocalDate.now(), LocalTime.MIDNIGHT,null));
        events.add(event1);
        Event event2 = new Event();
        event2.setName("NAME_2");
        event2.setDateTimeZone(new DateTimeZone(LocalDate.now(), LocalTime.MIDNIGHT,null));
        events.add(event2);
        Response<List<Event>> listResponse = scheduleController.addInstances(requestId, session, PositiveTestData.SCHED_1, events);
        Assert.assertEquals(2, listResponse.getResponse().size());
        UUID event1Id = listResponse.getResponse().get(0).getId();
        UUID event2Id = listResponse.getResponse().get(1).getId();
        //delete instances
        event1.setId(event1Id);
        event2.setId(event2Id);
        Response<List<Event>> listResponse2 = scheduleController.deleteInstances(requestId, session, PositiveTestData.SCHED_1, events);
        Assert.assertEquals(0, listResponse2.getResponse().size());
        Assert.assertNull(eventController.get(requestId, session, event1Id).getResponse());
        Assert.assertNull(eventController.get(requestId, session, event2Id).getResponse());
        //delete empty
        scheduleController.delete(requestId, session, PositiveTestData.SCHED_1);
        Assert.assertNull(scheduleController.get(requestId, session, PositiveTestData.SCHED_1).getResponse());
        //delete with cascade tasks and schedules
        scheduleController.delete(requestId, session, PositiveTestData.SCHED_2);
        Assert.assertNull(scheduleController.get(requestId, session, PositiveTestData.SCHED_2).getResponse());
        Assert.assertNull(eventController.get(requestId, session, PositiveTestData.SCHED_2_EVT_1).getResponse());
        Assert.assertNull(eventController.get(requestId, session, PositiveTestData.SCHED_2_EVT_2).getResponse());
        Assert.assertNull(eventController.get(requestId, session, PositiveTestData.SCHED_2_EVT_3).getResponse());
        //create
        Schedule schedule1 = new Schedule();
        schedule1.setName("LOL");
        schedule1.setCron("* * * * *");
        Response<Schedule> scheduleResponse = scheduleController.create(requestId, session, schedule1);
        Assert.assertEquals("LOL", scheduleResponse.getResponse().getName());
        Assert.assertNotNull(scheduleResponse.getResponse().getId());
    }

    @Test
    public void ScheduleControllerTestPositive2() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_USER).
                getResponse().getSession();
        //update empty
        Schedule schedule1 = new Schedule();
        schedule1.setName("LOL");
        schedule1.setCron("* * * * *");
        Response<Schedule> update = scheduleController.update(requestId, session, PositiveTestData.SCHED_1, schedule1);
        Assert.assertEquals("LOL", update.getResponse().getName());
        //update with instances
        Schedule schedule2 = new Schedule();
        schedule2.setCron("* * * * *");
        schedule2.setName("LOL");
        Response<Schedule> update2 = scheduleController.update(requestId, session, PositiveTestData.SCHED_2, schedule2);
        Assert.assertEquals("LOL", update2.getResponse().getName());
        Assert.assertNull(eventController.get(requestId, session, PositiveTestData.SCHED_2_EVT_1).getResponse());
        Assert.assertNull(eventController.get(requestId, session, PositiveTestData.SCHED_2_EVT_2).getResponse());
        Assert.assertNull(eventController.get(requestId, session, PositiveTestData.SCHED_2_EVT_3).getResponse());
    }

    @Test
    public void ScheduleControllerTestFailNoSession() {
        int requestId = RequestUtil.getRandomRequestId();
        try {
            scheduleController.getAll(requestId, null);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            scheduleController.get(requestId, null, PositiveTestData.SCHED_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {

            scheduleController.addInstances(requestId, null, PositiveTestData.SCHED_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            scheduleController.deleteInstances(requestId, null, PositiveTestData.SCHED_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            scheduleController.delete(requestId, null, PositiveTestData.SCHED_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            scheduleController.create(requestId, null, new Schedule());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            scheduleController.update(requestId, null, PositiveTestData.SCHED_1, new Schedule());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
    }

    @Test
    public void ScheduleControllerTestFailSessionNotFound() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = UUID.randomUUID();
        try {
            scheduleController.getAll(requestId, session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            scheduleController.get(requestId, session, PositiveTestData.SCHED_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {

            scheduleController.addInstances(requestId, session, PositiveTestData.SCHED_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            scheduleController.deleteInstances(requestId, session, PositiveTestData.SCHED_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            scheduleController.delete(requestId, session, PositiveTestData.SCHED_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            scheduleController.create(requestId, session, new Schedule());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            scheduleController.update(requestId, session, PositiveTestData.SCHED_1, new Schedule());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
    }

    @Test
    public void ScheduleControllerTestFailNotOwner() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_ADMIN).
                getResponse().getSession();
        try {
            scheduleController.get(requestId, session, PositiveTestData.SCHED_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            scheduleController.addInstances(requestId, session, PositiveTestData.SCHED_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            scheduleController.deleteInstances(requestId, session, PositiveTestData.SCHED_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            scheduleController.delete(requestId, session, PositiveTestData.SCHED_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            Schedule schedule = new Schedule();
            schedule.setCron("* * * * *");
            scheduleController.update(requestId, session, PositiveTestData.SCHED_1, schedule);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
    }

    @Test
    public void ScheduleControllerTestFailOther() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_USER).
                getResponse().getSession();
        //add instance with ID
        try {
            List<Event> events = new ArrayList<>();
            Event event = new Event();
            event.setId(UUID.randomUUID());
            events.add(event);
            scheduleController.addInstances(requestId, session, PositiveTestData.SCHED_1, events);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, e.getErrorCode());
        }
        //delete intastance that does not exist
        try {
            List<Event> events = new ArrayList<>();
            Event event = new Event();
            event.setId(UUID.randomUUID());
            events.add(event);
            scheduleController.deleteInstances(requestId, session, PositiveTestData.SCHED_1, events);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.ENTITY_NOT_FOUND, e.getErrorCode());
        }
    }

    //EVENTS
    @Test
    public void EventControllerTestPositive1() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_USER).
                getResponse().getSession();
        //getAll
        Response<List<Event>> all = eventController.getAll(requestId, session);
        Assert.assertEquals(5, all.getResponse().size());
        //getBetween
        Response<List<Event>> between = eventController.getBetween(requestId, session,
                "2018-03-18T01:20:21.903+04:00[Europe/Samara]",
                "2018-03-19T01:20:21.903+04:00[Europe/Samara]");
        Assert.assertEquals(1, between.getResponse().size());
        //getById
        Response<Event> byId = eventController.get(requestId, session, PositiveTestData.EVT_1);
        Assert.assertEquals("event_1", byId.getResponse().getName());
        //add extensions
        List<EventExtension> extensions = new ArrayList<>();
        EventExtension ext1 = new TickBoxExtension();
        ext1.setName("NAME_1");
        ((TickBoxExtension) ext1).setActive(true);
        extensions.add(ext1);
        EventExtension ext2 = new CounterExtension();
        ext2.setName("NAME_2");
        ((CounterExtension) ext2).setCounter(7);
        extensions.add(ext2);
        Response<Event> addExtensions = eventController.addExtensions(requestId, session, PositiveTestData.EVT_1, extensions);
        Assert.assertEquals(2, addExtensions.getResponse().getEventExtensions().size());
        //update extensions
        Response<Event> updateExtensions = eventController.updateExtensions(requestId, session, PositiveTestData.EVT_1, addExtensions.getResponse().getEventExtensions());
        Assert.assertEquals(2, updateExtensions.getResponse().getEventExtensions().size());
        //delete extensions
        Response<Event> deleteExtensions = eventController.deleteExtensions(requestId, session, PositiveTestData.EVT_1, addExtensions.getResponse().getEventExtensions());
        Assert.assertEquals(0, deleteExtensions.getResponse().getEventExtensions().size());
        //link task
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(PositiveTestData.TSK_CHILD);
        tasks.add(task);
        Response<List<Task>> linkTasks = eventController.linkTasks(requestId, session, PositiveTestData.EVT_1, tasks);
        Assert.assertEquals(2, linkTasks.getResponse().size());
        Assert.assertEquals(PositiveTestData.EVT_1, linkTasks.getResponse().get(0).getEvent().getId());
        //unlink task
        Response<List<Task>> unlinkTasks = eventController.unlinkTasks(requestId, session, PositiveTestData.EVT_1, tasks);
        Assert.assertEquals(1, unlinkTasks.getResponse().size());
        //delete task
        eventController.delete(requestId, session, PositiveTestData.EVT_1);
        Assert.assertNull(eventController.get(requestId, session, PositiveTestData.EVT_1).getResponse());
        Assert.assertNotNull(taskController.get(requestId, session, PositiveTestData.EVT_1_TSK).getResponse());
        //delete with cascade extensions
        eventController.delete(requestId, session, PositiveTestData.EVT_2);
        Assert.assertNull(eventController.get(requestId, session, PositiveTestData.EVT_2).getResponse());
        //create
        Event event1 = new Event();
        event1.setName("LOL");
        Response<Event> created = eventController.create(requestId, session, event1);
        Assert.assertEquals("LOL", created.getResponse().getName());
        Assert.assertNotNull(created.getResponse().getId());
        //update
        event1.setName("LOL2");
        Response<Event> updated = eventController.update(requestId, session, created.getResponse().getId(), event1);
        Assert.assertEquals("LOL2", updated.getResponse().getName());
    }

    @Test
    public void EventControllerTestFailNoSession() {
        int requestId = RequestUtil.getRandomRequestId();
        try {
            eventController.getAll(requestId, null);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.getBetween(requestId, null,
                    "2018-03-18T01:20:21.903+04:00[Europe/Samara]",
                    "2018-03-19T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.get(requestId, null, PositiveTestData.EVT_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.addExtensions(requestId, null, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.updateExtensions(requestId, null, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.deleteExtensions(requestId, null, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.linkTasks(requestId, null, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.unlinkTasks(requestId, null, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.delete(requestId, null, PositiveTestData.EVT_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.create(requestId, null, new Event());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            eventController.update(requestId, null, PositiveTestData.EVT_1, new Event());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
    }

    @Test
    public void EventControllerTestFailSessionNotFound() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = UUID.randomUUID();
        try {
            eventController.getAll(requestId, session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.getBetween(requestId, session,
                    "2018-03-18T01:20:21.903+04:00[Europe/Samara]",
                    "2018-03-19T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.get(requestId, session, PositiveTestData.EVT_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.addExtensions(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.updateExtensions(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.deleteExtensions(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.linkTasks(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.unlinkTasks(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.delete(requestId, session, PositiveTestData.EVT_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.create(requestId, session, new Event());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            eventController.update(requestId, session, PositiveTestData.EVT_1, new Event());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
    }

    @Test
    public void EventControllerTestFailNotOwner() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_ADMIN).
                getResponse().getSession();
        try {
            eventController.addExtensions(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            eventController.updateExtensions(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            eventController.deleteExtensions(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            eventController.linkTasks(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            eventController.unlinkTasks(requestId, session, PositiveTestData.EVT_1, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            eventController.delete(requestId, session, PositiveTestData.EVT_1);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            eventController.update(requestId, session, PositiveTestData.EVT_1, new Event());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
    }


    @Test
    public void EventControllerTestFailOther() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_USER).
                getResponse().getSession();
        //create with id
        List<EventExtension> exts = new ArrayList<>();
        EventExtension ext = new CounterExtension();
        ext.setId(UUID.randomUUID());
        exts.add(ext);
        try {
            eventController.addExtensions(requestId, session, PositiveTestData.EVT_1, exts);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, e.getErrorCode());
        }
        //update not found
        try {
            eventController.updateExtensions(requestId, session, PositiveTestData.EVT_1, exts);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.ENTITY_NOT_FOUND, e.getErrorCode());
        }
        //delete not found
        try {
            eventController.deleteExtensions(requestId, session, PositiveTestData.EVT_1, exts);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.ENTITY_NOT_FOUND, e.getErrorCode());
        }
        //get between fin before start
        try {
            eventController.getBetween(requestId, session,
                    "2018-03-19T01:20:21.903+04:00[Europe/Samara]", "2018-03-18T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, e.getErrorCode());
        }

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(UUID.randomUUID());
        tasks.add(task);
        //link not found
        try {
            eventController.linkTasks(requestId, session, PositiveTestData.EVT_1, tasks);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.ENTITY_NOT_FOUND, e.getErrorCode());
        }
        //unlink not found
        try {
            eventController.unlinkTasks(requestId, session, PositiveTestData.EVT_1, tasks);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.ENTITY_NOT_FOUND, e.getErrorCode());
        }
        //link found, not owned
        task.setId(PositiveTestData.TSK_ADMIN);
        try {
            eventController.linkTasks(requestId, session, PositiveTestData.EVT_1, tasks);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
    }

    //TASKS
    @Test
    public void TaskControllerTestPositive1() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_USER).
                getResponse().getSession();
        //getAll
        Response<List<Task>> all = taskController.getAll(requestId, session);
        Assert.assertEquals(4, all.getResponse().size());
        //getBetween
        Response<List<Task>> between = taskController.getBetween(requestId, session,
                "2018-03-24T01:20:21.903+04:00[Europe/Samara]",
                "2018-03-26T01:20:21.903+04:00[Europe/Samara]");
        Assert.assertEquals(3, between.getResponse().size());
        //getById
        Response<Task> byId = taskController.get(requestId, session, PositiveTestData.TSK_CHILD);
        Assert.assertEquals("task_child", byId.getResponse().getName());
        //link task
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(PositiveTestData.EVT_1_TSK);
        tasks.add(task);
        Response<List<Task>> linkTasks = taskController.linkChildren(requestId, session, PositiveTestData.TSK_CHILD, tasks);
        Assert.assertEquals(1, linkTasks.getResponse().size());
        Assert.assertEquals(PositiveTestData.TSK_CHILD, linkTasks.getResponse().get(0).getParent().getId());
        //unlink task
        Response<List<Task>> unlinkTasks = eventController.unlinkTasks(requestId, session, PositiveTestData.EVT_1, tasks);
        Assert.assertEquals(0, unlinkTasks.getResponse().size());
        //delete with subtasks
        taskController.delete(requestId, session, PositiveTestData.TSK_PARENT);
        Assert.assertNull(taskController.get(requestId, session, PositiveTestData.TSK_PARENT).getResponse());
        Assert.assertNotNull(taskController.get(requestId, session, PositiveTestData.TSK_CHILD).getResponse());
        //create
        Task task1 = new Task();
        task1.setName("LOL");
        task1.setDescription("URURU");
        Response<Task> created = taskController.create(requestId, session, task1);
        Assert.assertEquals("LOL", created.getResponse().getName());
        Assert.assertNotNull(created.getResponse().getId());
        //update
        task1.setName("LOL2");
        Response<Task> updated = taskController.update(requestId, session, created.getResponse().getId(), task1);
        Assert.assertEquals("LOL2", updated.getResponse().getName());
    }

    @Test
    public void TaskControllerTestFailNoSession() {
        int requestId = RequestUtil.getRandomRequestId();
        try {
            taskController.getAll(requestId, null);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            taskController.getBetween(requestId, null,
                    "2018-03-18T01:20:21.903+04:00[Europe/Samara]",
                    "2018-03-19T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            taskController.get(requestId, null, PositiveTestData.TSK_CHILD);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            taskController.linkChildren(requestId, null, PositiveTestData.TSK_CHILD, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            taskController.unlinkChildren(requestId, null, PositiveTestData.TSK_CHILD, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            taskController.delete(requestId, null, PositiveTestData.TSK_CHILD);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            taskController.create(requestId, null, new Task());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
        try {
            taskController.update(requestId, null, PositiveTestData.TSK_CHILD, new Task());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS, e.getErrorCode());
        }
    }

    @Test
    public void TaskControllerTestFailSessionNotFound() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = UUID.randomUUID();
        try {
            taskController.getAll(requestId, session);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            taskController.getBetween(requestId, session,
                    "2018-03-18T01:20:21.903+04:00[Europe/Samara]",
                    "2018-03-19T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            taskController.get(requestId, session, PositiveTestData.TSK_CHILD);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            taskController.linkChildren(requestId, session, PositiveTestData.TSK_CHILD, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            taskController.unlinkChildren(requestId, session, PositiveTestData.TSK_CHILD, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            taskController.delete(requestId, session, PositiveTestData.TSK_CHILD);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            taskController.create(requestId, session, new Task());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
        try {
            taskController.update(requestId, session, PositiveTestData.TSK_CHILD, new Task());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_SESSION_REQUIRED, e.getErrorCode());
        }
    }

    @Test
    public void TaskControllerTestFailNotOwner() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_ADMIN).
                getResponse().getSession();
        try {
            taskController.linkChildren(requestId, session, PositiveTestData.TSK_CHILD, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            taskController.unlinkChildren(requestId, session, PositiveTestData.TSK_CHILD, new ArrayList<>());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            taskController.delete(requestId, session, PositiveTestData.TSK_CHILD);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
        try {
            taskController.update(requestId, session, PositiveTestData.TSK_CHILD, new Task());
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
    }


    @Test
    public void TaskControllerTestFailOther() {
        int requestId = RequestUtil.getRandomRequestId();
        UUID session = restController.
                openSession(requestId, PositiveTestData.USER_LOGIN_USER).
                getResponse().getSession();
        //create with id

        //get between fin before start
        try {
            taskController.getBetween(requestId, session,
                    "2018-03-19T01:20:21.903+04:00[Europe/Samara]", "2018-03-18T01:20:21.903+04:00[Europe/Samara]");
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, e.getErrorCode());
        }

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(UUID.randomUUID());
        tasks.add(task);
        //link not found
        try {
            taskController.linkChildren(requestId, session, PositiveTestData.TSK_CHILD, tasks);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.ENTITY_NOT_FOUND, e.getErrorCode());
        }
        //unlink not found
        try {
            taskController.unlinkChildren(requestId, session, PositiveTestData.TSK_CHILD, tasks);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.ENTITY_NOT_FOUND, e.getErrorCode());
        }
        //link found, not owned
        task.setId(PositiveTestData.TSK_ADMIN);
        try {
            taskController.linkChildren(requestId, session, PositiveTestData.TSK_CHILD, tasks);
        } catch (ServiceException e) {
            Assert.assertEquals(ErrorCode.REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS, e.getErrorCode());
        }
    }
}
