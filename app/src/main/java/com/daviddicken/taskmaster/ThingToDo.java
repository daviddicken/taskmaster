package com.daviddicken.taskmaster;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ThingToDo {

    //======== Task class ===========
    @PrimaryKey(autoGenerate = true)
    long id;

    String title;
    String body;
    String state;

    public ThingToDo(String title, String body, String state) {
        this.title = title;
        this.body = body;
        //possible states "new", "assigned", "in progress", "completed"
        this.state = state;
    }

    //========== Getters & Setters =========

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

