package com.daviddicken.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AddATask extends AppCompatActivity {
   // Database database;
   ArrayList<Team> teams = new ArrayList<>();
   Toast toast;
   String lastFileUploadedKey;
   FusedLocationProviderClient locationProviderClient;
   Location currentLocation;
   String address;
   String bucketKey;
   String TAG = "myApp";



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
        getPermission();
        configLocation();
        getLastLocation();

        //========== Add pic Button and Listener =========
        Button addPic = AddATask.this.findViewById(R.id.addImg);
        addPic.setOnClickListener(v -> retrieveFile());

        //========== Add Task Button and Listener ========
        Button submitTask = AddATask.this.findViewById(R.id.submitTask);
        submitTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Team chosenTeam = getTeam();
                getAndSaveTask(chosenTeam);
//                getPermission();
//                configLocation();
                getLastLocation();
                toastAndGo();

            }
        });

    }

    //================ ask permission ===========================
    public void getPermission(){
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }

    //=============== Configure location Services ================
    public void configLocation(){
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    //================ get location ==============================
    public void getLastLocation(){
        System.out.println("stepped into get last");
        //======================================================================================================
        // This code should be ran on actual phone after testing is complete ===================================
//        locationProviderClient.getLastLocation()
//                .addOnSuccessListener(location -> Log.i(TAG + ".locSuccess", location.toString()))
//                .addOnFailureListener(error -> Log.e(TAG + ".locFailure", error.toString()))
//                .addOnCanceledListener(() -> Log.e(TAG + ".locCancel", "canceled"))
//                .addOnCompleteListener(complete -> Log.i(TAG + ".locComplete", complete.toString()));
//===============================================================================================================
        //Use code below for testing a moving location ==========================================================
        LocationRequest locationRequest = null;

        locationRequest = locationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult == null) {
                    return;
                }
                currentLocation = locationResult.getLastLocation();
                Log.i(TAG, currentLocation.toString());

                Geocoder geocoder = new Geocoder(AddATask.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(),10);
                    address =addresses.get(0).getAddressLine(0);
                    Log.i(TAG + ".address", address);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.
                permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast toast = new Toast(this);
            toast.setText("Please accept location permission");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    //================ retrieve file s3 ==========================
    public void retrieveFile(){
        Intent getPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getPicIntent.setType("*/*");
        //getPicIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{".jpg", ".png"});
        //next two lines make sure the images are accessible and openable locally
        //getPicIntent.addCategory(Intent.CATEGORY_OPENABLE);
        //getPicIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(getPicIntent, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if(reqCode == 1){
            Log.i("Amplify.picImg", "Got an image back");
            File file = new File(getFilesDir(), "testFile");
            try {
                InputStream inStream = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(inStream, new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            bucketKey = file.getName() + Math.random();
            uploadFile(bucketKey, file);
        }
    }

    //================ download file S3 ===========================
    public void downloadFile(String key){

        Amplify.Storage.downloadFile(
                key,
                new File(getApplicationContext().getFilesDir() + "/" + key + ".txt"),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                    ImageView img = findViewById(R.id.img);
                    img.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                },
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );

    }

    //================ upload file S3 =============================
    public void uploadFile(String key, File file) {
        lastFileUploadedKey = key;
        Amplify.Storage.uploadFile(
                key,
                file,
                result -> {
                    Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey());
                    downloadFile(key);
                },
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
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
                .address(address)
                .bucketkey(bucketKey)
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

