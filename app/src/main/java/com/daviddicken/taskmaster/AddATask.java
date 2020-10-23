package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddATask extends AppCompatActivity {
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_task);

        //========== Database setup ====================
        database = Room.databaseBuilder(getApplicationContext(), Database.class, "dbBucket")
                .allowMainThreadQueries()
                .build();

        //ArrayList<Task> allTask = (ArrayList<Task>) database.taskDao().getDbTasks();

//        TextView counter = findViewById(R.id.taskCounter);
//        counter.setText(allTask.size());
        //=========== Toast setup ======================
//https://developer.android.com/guide/topics/ui/notifiers/toasts.html
        Context context = getApplicationContext();
        CharSequence text = "Submitted!";
        int duration = Toast.LENGTH_SHORT;

        final Toast toast = Toast.makeText(context, text, duration);

        //========== Add Task Button and Listener ========
        Button submitTask = AddATask.this.findViewById(R.id.submitTask);
        submitTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //========== Get new Task ================
                TextView getTaskTitle = AddATask.this.findViewById(R.id.newTaskTitle);
                String name = getTaskTitle.getText().toString();
                TextView getTaskBody = AddATask.this.findViewById(R.id.newTaskBody);
                String body = getTaskBody.getText().toString();

                Task newTask = new Task(name, body, "new");

                //========= Save to database =============
                database.taskDao().saveToDb(newTask);
                //========= Toast and Go =================
                toast.show();
                Intent goHomeIntent = new Intent(AddATask.this, MainActivity.class);
                AddATask.this.startActivity(goHomeIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    //back arrow in taskbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(homeIntent, 0);
        return true;
    }
}