package com.timywimy.model.common.converters;

import com.timywimy.util.TimeFormatUtil;

import javax.persistence.*;
import java.time.*;
import java.util.Objects;

@Embeddable
public class DateTimeZone {

    @Column(name = "date", columnDefinition = "date")
    @Temporal(TemporalType.DATE)
    private LocalDate date;
    @Column(name = "time", columnDefinition = "time")
    @Temporal(TemporalType.TIME)
    private LocalTime time;
    @Column(name = "zone", columnDefinition = "varchar(20)")
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId zone;

    @Transient
    private boolean initialized;
    @Transient
    private LocalDateTime localDateTime;
    @Transient
    private ZonedDateTime zonedDateTime;

    public DateTimeZone() {
        initialized = false;
    }

    public DateTimeZone(LocalDate date, LocalTime time, ZoneId zone) {
        this.date = date;
        this.time = time;
        this.zone = zone;
        initialized = false;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        initialized = false;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
        initialized = false;
    }

    public ZoneId getZone() {
        return zone;
    }

    public void setZone(ZoneId zone) {
        this.zone = zone;
        initialized = false;
    }

    public LocalDateTime getLocalDateTime() {
        if (!initialized) {
            init();
        }
        return localDateTime;
    }

    public ZonedDateTime getZonedDateTime() {
        if (!initialized) {
            init();
        }
        return zonedDateTime;
    }

    //initialize dates

    private void init() {
        initLocalDateTime();
        initOffsetDateTime();
        initialized = true;
    }

    private void initLocalDateTime() {
        if (date == null || time == null) {
            localDateTime = null;
        } else {
            localDateTime = LocalDateTime.of(date, time);
        }
    }

    private void initOffsetDateTime() {
        if (date == null || time == null || zone == null) {
            zonedDateTime = null;
        } else {
            zonedDateTime = ZonedDateTime.of(date, time, zone);
        }
    }

    @Override
    public String toString() {
        return String.format("DateTimeZone(%s, %s, %s)",
                TimeFormatUtil.toString(date), TimeFormatUtil.toString(date), zone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DateTimeZone)) {
            return false;
        }
        DateTimeZone that = (DateTimeZone) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(time, that.time) &&
                Objects.equals(zone, that.zone) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, zone);
    }
}
