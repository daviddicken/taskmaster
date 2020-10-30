package com.daviddicken.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnInteractWithTaskListener {

    //Database database;
    ArrayList<Task> tasks = new ArrayList<>();
    SharedPreferences preferences;
    RecyclerView recyclerView;
    Handler handler;

    @Override
    public void onResume(){
        super.onResume();

        populateViews();
        populateRecycler();
    }

//=================== ON CREATE ==================================
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        configAws();
        setupButtons();
        createHandler();
        populateRecycler();
        populateViews();
    }

    //============= Go to Task Detail Page =================
    @Override
    public void taskListener(Task task) {
        Intent intent = new Intent(MainActivity.this, TaskDetail.class);

        intent.putExtra("taskTitle", task.getTitle());
        intent.putExtra("taskBody", task.getDescription());
        intent.putExtra("taskState", task.getStatus());
        this.startActivity(intent);
    }

    //============ Populate views (Users name) ====
    public void populateViews() {
        // setup to be able to save and access data to and from SharedPreferences (local storage)
        preferences = PreferenceManager.getDefaultSharedPreferences(this); // getter
        final SharedPreferences.Editor preferencesEditor = preferences.edit();

        // check for and display users name
        TextView name = findViewById(R.id.theName);
        String namePassedIn = preferences.getString("usersName", "No User Entered");

        name.setText(String.format("%s's tasks:", namePassedIn));
    }

    //============= RecyclerView ===========================
    public void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerTaskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(tasks, this));
    }

    //============= Configure AWS =========================
    public void configAws() {
        // initialize Amplify API
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            //createTeams();

        } catch (AmplifyException e) {
            Log.e("MainActivityAmplify", "Could not initialize Amplify", e);
        }
    }

    //================= Handler =====================
    public void createHandler() {
        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        return false;
                    }
                });
    }

    //================= Populate RecyclerView =======
    public void populateRecycler() {
        Amplify.API.query(
                ModelQuery.list(Task.class),
                response -> {
                    tasks.clear();
                    for (Task task : response.getData()) {  //populate list for recyclerview.
                        if (preferences.contains("userTeam")) { // check that a user team exist before trying to find teams task
                            if (task.taskForTeam.getName().equals(preferences.getString("userTeam", " "))) {
                                tasks.add(task);
                            }
                        } else {
                            tasks.add(task);
                        }

                    }
                    handler.sendEmptyMessage(1);
                    Log.i("AmplifyQuery", "Number of items from dynamodb" + tasks.size());
                },
                error -> Log.i("AmplifyQuery", "Failed to get items"));
    }

    //================= Buttons =====================
    public void setupButtons() {
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

    //============= Create Test teams ======================
    public void createTeams(){
        Team teamRed = Team.builder()
                .name("Red Team").build();

        Team teamBlue = Team.builder()
                .name("Blue Team").build();

        Team teamGreen = Team.builder()
                .name("Green Team").build();

        Amplify.API.mutate(ModelMutation.create(teamRed),
        response -> Log.i("Amplify", "Team created"),
        error -> Log.e("Amplify", "Team created"));

        Amplify.API.mutate(ModelMutation.create(teamBlue),
                response -> Log.i("Amplify", "Team created"),
                error -> Log.e("Amplify", "Team created"));

        Amplify.API.mutate(ModelMutation.create(teamGreen),
                response -> Log.i("Amplify", "Team created"),
                error -> Log.e("Amplify", "Team created"));
    }
}// end of mainActivity



//==================== Zombie code =======================

//ArrayList<Task> taskDb = (ArrayList<Task>) database.taskDao().getDbTasks();

//================ database stuff ============
// database build
//        database = Room.databaseBuilder(getApplicationContext(), Database.class, "dbBucket")
//                //.fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
