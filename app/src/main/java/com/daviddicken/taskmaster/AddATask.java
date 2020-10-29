package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_task);

        ArrayList<Team> teams = new ArrayList<>();

        //========== Get Teams ========================
        Amplify.API.query(ModelQuery.list(Team.class),
                response -> {
                    for(Team team : response.getData()){
                        teams.add(team);
                    } },
                error -> Log.e("AmplifyAddTask", "failed getting teams"));

        //========== Database setup ====================
//        database = Room.databaseBuilder(getApplicationContext(), Database.class, "dbBucket")
//                .allowMainThreadQueries()
//                .build();

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

                //========== Get team matching selected radio button ====================
                RadioGroup getBox = AddATask.this.findViewById(R.id.buttonBox);
                RadioButton selectedTeam = AddATask.this.findViewById(getBox.getCheckedRadioButtonId());
                String teamName = selectedTeam.getText().toString();
                Team chosenTeam = null;
                for(int i = 0; i < teams.size(); i++){
                    if(teams.get(i).getName().equals(teamName)){
                        chosenTeam = teams.get(i);
                    }
                }

                //========== Get new Task ================
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

                //========= Save to database =============
                //database.taskDao().saveToDb(newThingToDo);
                //========= Toast and Go =================
                toast.show();
                onBackPressed();
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