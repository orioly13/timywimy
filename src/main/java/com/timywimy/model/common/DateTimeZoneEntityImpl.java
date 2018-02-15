package com.timywimy.model.common;


import com.timywimy.model.common.converters.DateTimeZone;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class DateTimeZoneEntityImpl extends DescribedEntityImpl implements DateTimeZoneEntity{

    @Column(name = "date", columnDefinition = "VARCHAR", nullable = false)
    private DateTimeZone dateTimeZone;

    protected DateTimeZoneEntityImpl() {
    }

    protected DateTimeZoneEntityImpl(LocalDate date, LocalTime time, ZoneOffset offset,
                                     String name, String description, UUID id) {
        super(name, description, id);
        this.dateTimeZone = new DateTimeZone(date, time, offset);
    }

    public DateTimeZone getDateTimeZone() {
        return dateTimeZone;
    }

    public void setDateTimeZone(DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }

    //other methods

    @Override
    public String toString() {
        return String.format("(%s, dtz:%s)", super.toString(), dateTimeZone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DateTimeZoneEntity)) {
            return false;
        }
        DateTimeZoneEntityImpl that = (DateTimeZoneEntityImpl) o;
        return Objects.equals(dateTimeZone, that.dateTimeZone) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTimeZone, super.hashCode());
    }
}
