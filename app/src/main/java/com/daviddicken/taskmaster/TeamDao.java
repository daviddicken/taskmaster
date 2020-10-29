package com.daviddicken.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.amplifyframework.datastore.generated.model.Team;

import java.util.List;

@Dao
public interface TeamDao {

    @Query("SELECT * FROM Team WHERE name = :name")
    public Team getTeam(String name);
}
