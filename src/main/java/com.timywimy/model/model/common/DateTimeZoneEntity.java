package com.timywimy.model.model.common;

import com.timywimy.model.model.common.converters.DateTimeZone;

public interface DateTimeZoneEntity extends DescribedEntity {

    DateTimeZone getDateTimeZone();

    void setDateTimeZone(DateTimeZone dateTimeZone);
}
