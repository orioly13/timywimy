package com.timywimy.model.common;


import java.time.Duration;

public abstract class DurableEntityImpl extends DateTimeZoneEntityImpl implements DurableEntity {

    private Duration duration;

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

}
