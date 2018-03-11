package timywimy.model.common.util;

import timywimy.model.common.converters.ZoneIdConverter;
import timywimy.util.TimeFormatUtil;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.time.*;
import java.util.Objects;

@Embeddable
public class DateTimeZone implements Comparable<DateTimeZone> {

    @Column(name = "date", columnDefinition = "date")
    private LocalDate date;
    @Column(name = "time", columnDefinition = "time")
    private LocalTime time;
    @Column(name = "zone", columnDefinition = "varchar(20)")
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId zone;

    private static final ZonedDateTime MAX = Instant.ofEpochMilli(Long.MAX_VALUE/2).atZone(ZoneOffset.UTC);

    @Transient
    private boolean initialized;
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

    //returns in UTC if no zone
    public ZonedDateTime getZonedDateTime() {
        if (!initialized) {
            init();
        }
        return zonedDateTime;
    }

    public void init() {
        if (!initialized) {
            initOffsetDateTime();
            initialized = true;
        }
    }

    private void initOffsetDateTime() {
        if (date == null) {
            zonedDateTime = MAX;
        } else {
            zonedDateTime = ZonedDateTime.of(date,
                    time == null ? LocalTime.MIDNIGHT : time,
                    zone == null ? ZoneId.of("UTC") : zone);
        }
    }

    @Override
    public String toString() {
        return String.format("DateTimeZone(%s, %s, %s)",
                TimeFormatUtil.toString(date), TimeFormatUtil.toString(time), TimeFormatUtil.toString(zone));
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
                Objects.equals(zone, that.zone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, zone);
    }

    @Override
    public int compareTo(DateTimeZone that) {
        if (that == null) {
            throw new NullPointerException();
        }
        this.init();
        that.init();
        return this.zonedDateTime.compareTo(that.zonedDateTime);
    }

    public boolean isAfter(DateTimeZone that) {
        if (that == null) {
            throw new NullPointerException();
        }
        this.init();
        that.init();
        return this.zonedDateTime.isAfter(that.zonedDateTime);
    }

    public boolean isBefore(DateTimeZone that) {
        if (that == null) {
            throw new NullPointerException();
        }
        this.init();
        that.init();
        return this.zonedDateTime.isBefore(that.zonedDateTime);
    }

    public static DateTimeZone now() {
        ZonedDateTime now = ZonedDateTime.now();
        return new DateTimeZone(now.toLocalDate(), now.toLocalTime(), now.getZone());
    }

}
