package com.timywimy.model.common;

import com.timywimy.model.common.converters.DateTimeZone;

public interface DateTimeZoneEntity extends BaseEntity {

    DateTimeZone getDateTimeZone();

    void setDateTimeZone(DateTimeZone dateTimeZone);
}
