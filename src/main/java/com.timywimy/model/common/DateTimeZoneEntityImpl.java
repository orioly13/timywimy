package com.timywimy.model.common;


import com.timywimy.model.common.converters.DateTimeZone;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DateTimeZoneEntityImpl extends DescribedEntityImpl implements DateTimeZoneEntity {

    @Column(name = "date", columnDefinition = "VARCHAR", nullable = false)
    private DateTimeZone dateTimeZone;

    public DateTimeZone getDateTimeZone() {
        return dateTimeZone;
    }

    public void setDateTimeZone(DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }
}
