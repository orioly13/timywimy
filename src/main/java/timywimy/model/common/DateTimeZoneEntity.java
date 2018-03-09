package timywimy.model.common;

import timywimy.model.common.util.DateTimeZone;

public interface DateTimeZoneEntity extends BaseEntity {

    DateTimeZone getDateTimeZone();

    void setDateTimeZone(DateTimeZone dateTimeZone);
}
