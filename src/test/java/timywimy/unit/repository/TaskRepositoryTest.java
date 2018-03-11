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
import timywimy.model.bo.tasks.Task;
import timywimy.repository.TaskRepository;
import timywimy.util.TaskTestData;
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
        "classpath:db/postgresql/3-init-tasks.sql"},
        config = @SqlConfig(encoding = "UTF-8"))
public class TaskRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    @Autowired
    private TaskRepository repository;

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
        Task task = repository.get(TaskTestData.TASK_1);
        Assert.assertNotNull(task);
        Assert.assertEquals(TaskTestData.TASK_1, task.getId());
    }

    //
    @Test
    public void saveValidTask() throws Exception {
        Task task = TaskTestData.getTaskNew();
        Task save = repository.save(task, UserTestData.USER_ID);
        Assert.assertEquals(task.getId(), save.getId());

    }

    //
    @Test
    public void saveInvalidNullOwner() throws Exception {
        thrown.expect(RepositoryException.class);
        thrown.expectMessage("owner should be provided");
        Task task = TaskTestData.getTaskExisting();
        task.setOwner(null);
        repository.save(task, UserTestData.USER_ID);
    }

    @Test
    public void deleteWithTasks() throws Exception {
        Task task = TaskTestData.getTaskExistingWithTasks();
        Set<String> parameters = new HashSet<>();
        parameters.add("children");

        Task task1 = repository.get(task.getId(), parameters);
        Assert.assertEquals(1, task1.getChildren().size());
        Task child = task1.getChildren().get(0);

        repository.delete(task1.getId());
        Set<String> parameters1 = new HashSet<>();
        parameters.add("parent");
        Task childTask = repository.get(child.getId(), parameters1);
        Assert.assertNotNull(childTask);
        Assert.assertNull(childTask.getParent());
    }

    //
    @Test
    public void addNewTasks() throws Exception {
        Task task = TaskTestData.getTaskExisting();

        Task task1 = new Task();
        task1.setId(TaskTestData.TASK_3);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        List<Task> tasks1 = repository.linkChildren(task.getId(), tasks, UserTestData.USER_ID);
        Assert.assertEquals(1, tasks1.size());
//        Task Task2 = repository.get(Task.getId(), parameters);
    }

    @Test
    public void deleteTasks() throws Exception {
//        repository.get
        Task task = TaskTestData.getTaskExistingWithTasks();
        Set<String> parameters = new HashSet<>();
        parameters.add("children");

        Task task1 = repository.get(task.getId(), parameters);
        Assert.assertEquals(1, task1.getChildren().size());

        List<Task> extensionsToDelete = new ArrayList<>();
        extensionsToDelete.add(task1.getChildren().get(0));
        List<Task> tasks = repository.unlinkChildren(task.getId(), extensionsToDelete, UserTestData.USER_ID);
        Assert.assertEquals(0, tasks.size());
    }
}
