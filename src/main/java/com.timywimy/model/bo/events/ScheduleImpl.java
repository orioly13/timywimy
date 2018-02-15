package com.timywimy.model.bo.events;

import com.timywimy.model.common.DurableEntityImpl;
import com.timywimy.model.security.User;

import java.util.List;

public class ScheduleImpl extends DurableEntityImpl implements Schedule {

    private String cron;
    private List<Event> instances;

    private User owner;

    @Override
    public String getCron() {
        return cron;
    }

    @Override
    public void setCron() {
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
    public User getOwner() {
        return owner;
    }

    @Override
    public void setOwner(User owner) {
        this.owner = owner;
    }
}
