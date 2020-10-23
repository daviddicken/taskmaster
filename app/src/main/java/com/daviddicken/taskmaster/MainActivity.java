package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnInteractWithTaskListener {

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup to be able to save and access data to and from SharedPreferences (local storage)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this); // getter
        final SharedPreferences.Editor preferencesEditor = preferences.edit();

        // check for and display users name
        TextView name = findViewById(R.id.theName);
        String namePassedIn = preferences.getString("usersName", "No User Entered");

        name.setText(String.format("%s's tasks:", namePassedIn));

        //================ database stuff ============
        // database build
        database = Room.databaseBuilder(getApplicationContext(), Database.class, "dbBucket")
                .allowMainThreadQueries()
                .build();

        ArrayList<Task> taskDb = (ArrayList<Task>) database.taskDao().getDbTasks();

        //======== RecyclerView =========================
        RecyclerView recyclerView = findViewById(R.id.recyclerTaskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(taskDb, this));


        //================= Buttons =====================
        // got to settings activity
        ImageButton settingsActivity = MainActivity.this.findViewById(R.id.goToSettings);
        settingsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
                MainActivity.this.startActivity(settingsIntent);
            }
        });

        // add Task button
        Button addTask = MainActivity.this.findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddATask.class);
                MainActivity.this.startActivity(addTaskIntent);
            }
        });

        // go to all tasks button
        Button allTask = MainActivity.this.findViewById(R.id.allTasks);
        allTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allTaskIntent = new Intent(MainActivity.this, AllTasks.class);
                MainActivity.this.startActivity(allTaskIntent);
            }
        });
    }

    @Override
    public void taskListener(Task task) {
        Intent intent = new Intent(MainActivity.this, TaskDetail.class);

        intent.putExtra("taskTitle", task.getTitle());
        intent.putExtra("taskBody", task.getBody());
        intent.putExtra("taskState", task.getState());
        this.startActivity(intent);
    }
}