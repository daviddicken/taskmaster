package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        String taskName = intent.getStringExtra("taskName");
        String taskBody = intent.getStringExtra("taskBody");
        String taskState = intent.getStringExtra("taskState");

        TextView name = findViewById(R.id.taskDetailHeader);
        name.setText(taskName);
        TextView body = findViewById(R.id.taskBody);
        body.setText(taskBody);
        TextView state = findViewById(R.id.taskState);
        state.setText(taskState);



//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        TextView task = findViewById(R.id.taskDetailHeader);
//        task.setText(preferences.getString("taskClicked", "No task selected"));
    }
}