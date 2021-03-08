package com.example.assignment.database;

import io.realm.RealmObject;

public class History extends RealmObject {

    private String pathId;
    private String startTime;

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
