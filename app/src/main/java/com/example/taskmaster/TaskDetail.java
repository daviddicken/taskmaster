package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView task = findViewById(R.id.taskDetailHeader);
        task.setText(preferences.getString("taskClicked", "No task selected"));
    }
}