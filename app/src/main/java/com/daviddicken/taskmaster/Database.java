package com.daviddicken.taskmaster;


import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Task.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract TaskDao taskDao();
}
