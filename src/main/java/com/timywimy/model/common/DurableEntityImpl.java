package com.timywimy.model.common;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

public abstract class DurableEntityImpl extends DateTimeZoneEntityImpl implements DurableEntity {

    private Duration duration;

    protected DurableEntityImpl() {
    }

    protected DurableEntityImpl(Duration duration, LocalDate date, LocalTime time, ZoneOffset offset,
                                String name, String description, UUID id) {
        super(date, time, offset, name, description, id);
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }


    //other methods
    @Override
    public String toString() {
        return String.format("(%s, duration:[%s])", super.toString(), duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DurableEntity)) {
            return false;
        }
        DurableEntityImpl that = (DurableEntityImpl) o;
        return Objects.equals(duration, that.duration) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, super.hashCode());
    }


}
