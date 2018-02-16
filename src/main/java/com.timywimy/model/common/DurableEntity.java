package com.timywimy.model.common;

import com.timywimy.model.common.converters.PeriodDuration;


public interface DurableEntity extends BaseEntity {

    PeriodDuration getDuration();

    void setDuration(PeriodDuration duration);
}
