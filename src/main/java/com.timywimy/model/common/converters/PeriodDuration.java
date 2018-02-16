package com.timywimy.model.common.converters;

import java.time.Duration;
import java.time.Period;
import java.util.Objects;

public class PeriodDuration {

    private Period period;
    private Duration duration;

    public PeriodDuration() {
    }

    public PeriodDuration(Period period, Duration duration) {
        this.period = period;
        this.duration = duration;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("PeriodDuration(P:%s ; D:%s)", period, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof PeriodDuration)) {
            return false;
        }
        PeriodDuration that = (PeriodDuration) o;
        return Objects.equals(period, that.period) &&
                Objects.equals(duration, that.duration) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, duration);
    }
}
