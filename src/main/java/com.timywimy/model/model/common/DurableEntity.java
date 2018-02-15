package com.timywimy.model.model.common;

import java.time.Duration;

public interface DurableEntity extends DateTimeZoneEntity {
    Duration getDuration();

    void setDuration(Duration duration);
}
