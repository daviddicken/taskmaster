package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        String taskName = intent.getStringExtra("taskTitle");
        String taskBody = intent.getStringExtra("taskBody");
        String taskState = intent.getStringExtra("taskState");

        TextView name = findViewById(R.id.taskTitle);
        name.setText(taskName);
        TextView body = findViewById(R.id.taskBody);
        body.setText(taskBody);
        TextView state = findViewById(R.id.taskState);
        state.setText(taskState);

    }
}