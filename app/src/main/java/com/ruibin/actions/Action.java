package com.ruibin.actions;

import java.io.Serializable;

public class Action implements Serializable {
    private String title;
    private String description;
    private long createTime;
    private long dueTime;
    private long achieveTime;
    private boolean isAchieved;

    public Action() {
    }

    public Action(String title, String description, long createTime, long dueTime, long achieveTime) {
        this.title = title;
        this.description = description;
        this.createTime = createTime;
        this.dueTime = dueTime;
        this.achieveTime = achieveTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    public long getAchieveTime() {
        return achieveTime;
    }

    public void setAchieveTime(long achieveTime) {
        this.achieveTime = achieveTime;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    public void setAchieved(boolean isAchieved) {
        this.isAchieved = isAchieved;
    }
}
