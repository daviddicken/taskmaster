package com.daviddicken.taskmaster;


import androidx.room.RoomDatabase;
import com.amplifyframework.datastore.generated.model.Task;


@androidx.room.Database(entities = {Task.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract TaskDao taskDao();
}
