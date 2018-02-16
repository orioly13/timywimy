package com.timywimy.model.bo.events;

import com.timywimy.model.common.DefaultEntityImpl;
import com.timywimy.model.common.converters.PeriodDuration;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class ScheduleImpl extends DefaultEntityImpl implements Schedule {

    private String cron;
    private PeriodDuration duration;

    private List<Event> instances;

    @Override
    public String getCron() {
        return cron;
    }

    @Override
    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public List<Event> getInstances() {
        return instances;
    }

    @Override
    public void setInstances(List<Event> instances) {
        this.instances = instances;
    }

    @Override
    public PeriodDuration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(PeriodDuration duration) {
        this.duration = duration;
    }
}
