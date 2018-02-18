package timywimy.model.common;

import timywimy.model.common.converters.DateTimeZone;

public interface DateTimeZoneEntity extends BaseEntity {

    DateTimeZone getDateTimeZone();

    void setDateTimeZone(DateTimeZone dateTimeZone);
}
