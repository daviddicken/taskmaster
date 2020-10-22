package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // SharePreferences setup (local storage)
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor preferencesEditor = preferences.edit();

        // Save user button action
        Button saveUser = this.findViewById(R.id.saveName);
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView getName = Settings.this.findViewById(R.id.enteredName);

                preferencesEditor.putString("usersName", getName.getText().toString());
                preferencesEditor.apply();

                Intent goHomeIntent = new Intent(Settings.this, MainActivity.class);
                Settings.this.startActivity(goHomeIntent);
            }
        });



    }
}