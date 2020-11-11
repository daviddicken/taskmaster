package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        String taskName = intent.getStringExtra("taskTitle");
        String taskBody = intent.getStringExtra("taskBody");
        String taskState = intent.getStringExtra("taskState");
        String taskAddress = intent.getStringExtra("taskLocation");
        String taskImage = intent.getStringExtra("taskImage");

        TextView name = findViewById(R.id.taskTitle);
        name.setText(taskName);
        TextView body = findViewById(R.id.taskBody);
        body.setText(taskBody);
        TextView state = findViewById(R.id.taskState);
        state.setText(taskState);
        TextView location = findViewById(R.id.locationView);
        location.setText(taskAddress);
        downloadFile(taskImage);



        //========= Home arrow in action bar =========
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //================ download file S3 ===========================
    public void downloadFile(String key){

        Amplify.Storage.downloadFile(
                key,
                new File(getApplicationContext().getFilesDir() + "/" + key + ".txt"),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                    ImageView img = findViewById(R.id.taskImage);
                    img.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                },
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(homeIntent, 0);
        return true;
    }
}