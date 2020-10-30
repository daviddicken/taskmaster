package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //set default radio button
        RadioButton redButton = Settings.this.findViewById(R.id.red);
        redButton.toggle();

        // SharePreferences setup (local storage)
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor preferencesEditor = preferences.edit();

        // Save user button action
        Button saveUser = this.findViewById(R.id.saveName);
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //========== Radio buttons ================
                RadioGroup getBox = Settings.this.findViewById(R.id.settingBox);
                RadioButton selectedButton = Settings.this.findViewById(getBox.getCheckedRadioButtonId());
                String userTeam = selectedButton.getText().toString();
                TextView getName = Settings.this.findViewById(R.id.enteredName);

                preferencesEditor.putString("usersName", getName.getText().toString());
                preferencesEditor.putString("userTeam", userTeam);
                preferencesEditor.apply();

                onBackPressed();
            }
        });
    }
}

//================== Zombie Code ==========================

//========== Get Teams ========================
//        ArrayList<Team> teams = new ArrayList<>();
//        Amplify.API.query(ModelQuery.list(Team.class),
//                response -> {
//                    for(Team team : response.getData()){
//                        teams.add(team);
//                    } },
//                error -> Log.e("AmplifyAddTask", "failed getting teams"));