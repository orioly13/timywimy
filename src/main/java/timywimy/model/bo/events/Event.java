package timywimy.model.bo.events;

import timywimy.model.bo.events.extensions.common.EventExtension;
import timywimy.model.bo.tasks.Task;
import timywimy.model.common.*;

import java.util.List;


public interface Event extends OwnedEntity, NamedEntity, DescribedEntity, DurableEntity, DateTimeZoneEntity {

    List<EventExtension> getExtensions();

    void setExtensions(List<EventExtension> extensions);

    List<Task> getTasks();

    void setTasks(List<Task> tasks);

    Schedule getSchedule();

    void setSchedule(Schedule schedule);
}
