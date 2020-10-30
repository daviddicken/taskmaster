package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;


public class AddATask extends AppCompatActivity {
   // Database database;
   ArrayList<Team> teams = new ArrayList<>();
   Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_task);

        // ActionBar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set default button press
        RadioButton redButton = AddATask.this.findViewById(R.id.redRadio);
        redButton.toggle();

        queryTeams();
        createToast();

        //========== Add Task Button and Listener ========
        Button submitTask = AddATask.this.findViewById(R.id.submitTask);
        submitTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Team chosenTeam = getTeam();
                getAndSaveTask(chosenTeam);
                toastAndGo();
            }
        });

    }

    //=========== Toast and Go =====================
    public void toastAndGo() {
        toast.show();
        onBackPressed();
    }

    //=========== Get and Save Task ================
    public void getAndSaveTask(Team chosenTeam){
        TextView getTaskTitle = AddATask.this.findViewById(R.id.newTaskTitle);
        String name = getTaskTitle.getText().toString();
        TextView getTaskBody = AddATask.this.findViewById(R.id.newTaskBody);
        String body = getTaskBody.getText().toString();

        Task newThingToDo = Task.builder()
                .title(name)
                .description(body)
                .status("new").taskForTeam(chosenTeam).build();

        //========= Send to online db ===========
        Amplify.API.mutate(ModelMutation.create(newThingToDo),
                response -> Log.i("AddTask", "Added " + name),
                error -> Log.e("AddTask", "Unable to add task"));
    }

    //=========== Get Team =========================
    public Team getTeam() {
        RadioGroup getBox = AddATask.this.findViewById(R.id.buttonBox);
        RadioButton selectedTeam = AddATask.this.findViewById(getBox.getCheckedRadioButtonId());
        String teamName = selectedTeam.getText().toString();
        Team chosenTeam = null;
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getName().equals(teamName)) {
                chosenTeam = teams.get(i);
            }
        }
        return  chosenTeam;
    }


    //=========== Toast setup ======================
    public void createToast() {
    //https://developer.android.com/guide/topics/ui/notifiers/toasts.html
        Context context = getApplicationContext();
        CharSequence text = "Submitted!";
        int duration = Toast.LENGTH_SHORT;

        toast = Toast.makeText(context, text, duration);
    }

    //============= Query Teams ==========================
    public void queryTeams() {
        Amplify.API.query(ModelQuery.list(Team.class), response -> {
            for (Team team : response.getData()) {
                teams.add(team);
            } }, error -> Log.e("AmplifyAddTask", "failed getting teams"));
    }


    //============= Back arrow in Taskbar ================
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(homeIntent, 0);
        return true;
    }
}






//================= Zombie Code =====================

//========= Save to database =============
//database.taskDao().saveToDb(newThingToDo);

//========== Database setup ====================
//        database = Room.databaseBuilder(getApplicationContext(), Database.class, "dbBucket")
//                .allowMainThreadQueries()
//                .build();

//ArrayList<Task> allTask = (ArrayList<Task>) database.taskDao().getDbTasks();

//======== trying to populate counter ===========
//        TextView counter = findViewById(R.id.taskCounter);
//        counter.setText(allTask.size());

