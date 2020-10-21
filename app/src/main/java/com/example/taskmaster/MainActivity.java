package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

        // Intent to got to Task Detail activity
        final Intent goToDetailIntent = new Intent(MainActivity.this, TaskDetail.class);



        // dishes button
        Button addDishes = this.findViewById(R.id.dishesTask);
        addDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               TextView getTask = findViewById(R.id.dishesTask);
               storeTask(getTask, preferencesEditor);
               MainActivity.this.startActivity(goToDetailIntent);
            }
        });

        // laundry button
        Button addLaundry = this.findViewById(R.id.laundryTask);
        addLaundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView getTask = findViewById(R.id.laundryTask);
                storeTask(getTask, preferencesEditor);
                MainActivity.this.startActivity(goToDetailIntent);
            }
        });

        // dishes button
        Button addSweep = this.findViewById(R.id.sweepTask);
        addSweep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView getTask = findViewById(R.id.sweepTask);
                storeTask(getTask, preferencesEditor);
                MainActivity.this.startActivity(goToDetailIntent);
            }
        });
    }

    public void storeTask(TextView getTask, SharedPreferences.Editor preferencesEditor){
        String buttonText = getTask.getText().toString();
        preferencesEditor.putString("taskClicked", buttonText);
        preferencesEditor.apply();
    }
}