package com.timywimy.model.common;

import java.time.LocalDateTime;


public interface DurableEntity extends BaseEntity {

    LocalDateTime getDuration();

    void setDuration(LocalDateTime duration);
}
