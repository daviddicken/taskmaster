package com.daviddicken.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void saveToDb(Task task);

    @Query("SELECT * FROM Task")
    public List<Task> getDbTasks();

}
