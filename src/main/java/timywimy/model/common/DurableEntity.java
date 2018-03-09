package timywimy.model.common;

import java.time.Duration;


public interface DurableEntity extends BaseEntity {

    Duration getDuration();

    void setDuration(Duration duration);
}
