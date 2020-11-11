package com.daviddicken.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnInteractWithTaskListener {

    //Database database;
    ArrayList<Task> tasks = new ArrayList<>();
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    RecyclerView recyclerView;
    Handler handler;
    Handler signedInHandler;

    //================ Pin Point =============================
    public static final String TAG = "Amplify";
    private static PinpointManager pinpointManager;

    //=================== ON CREATE ==================================
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        configAws();
        getPinpointManager(getApplicationContext());
        createSignedInHandler(); //TODO: Merge handler creation into one method
        setupButtons();
        createHandler();
        populateRecycler();
        populateViews();
        analyticsEvent("OpenedApp", "User has opened the app");

    }

    //============== On Resume ===================
    @Override
    public void onResume(){
        super.onResume();

        populateViews();
        populateRecycler();

    }


    //=============== event creater ==========================
    public void analyticsEvent(String nameOfEvent, String message) {
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name(nameOfEvent)
                .addProperty("time", Long.toString(new Date().getTime()))
                .addProperty(nameOfEvent, message)
                .build();
        Amplify.Analytics.recordEvent(event);
    }

    //=============== PinPoint Manager =======================
    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<InstanceIdResult> task) {
                            final String token = task.getResult().getToken();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }


    //============= Log In =================================
    public void logIn(String userName, String password){
        Amplify.Auth.signIn(
                userName,
                password,
                result -> {
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                    isLoggedIn();
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );

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

    //============ Populate views (Users name) ===============
    public void populateViews() {
        // setup to be able to save and access data to and from SharedPreferences (local storage)
        preferences = PreferenceManager.getDefaultSharedPreferences(this); // getter
        preferencesEditor = preferences.edit();

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
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));
            Amplify.configure(getApplicationContext());
            //createTeams();

        } catch (AmplifyException e) {
            Log.e("MainActivityAmplify", "Could not initialize Amplify", e);
        }
    }

    //============ Check if Signed In ================
    public void isLoggedIn(){
        AtomicBoolean isSignedIn = new AtomicBoolean(false);
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i("AmplifyQuickstart", result.toString());
                    Message message = new Message();
                    if(result.isSignedIn()){
                        message.arg1 = 1;
                        signedInHandler.sendMessage(message);
                    }else{
                        message.arg1 = 0;
                        signedInHandler.sendMessage(message);
                    }
                },
                error -> Log.e("AmplifyQuickstart", error.toString())
        );
    }

    //============ Handler - signed In ===============
    public void createSignedInHandler() {
        signedInHandler = new Handler(Looper.getMainLooper(), message -> {
            if(message.arg1 == 0){
               Log.i("Amplify.login", "Not signed in");
            }else if(message.arg1 == 1){
                Log.i("Amplify.login", "Signed in: True");
            }else{
                Log.i("Amplify.login", "missed the if?");
            }
            return false;
        });
    }

    //============ Handler ===========================
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

        // log in---------------------------
        Button login = findViewById(R.id.logIn);
        login.setOnClickListener(view -> this.startActivity(new Intent(this, LogIn.class)));

        // Sign up-------------------------------
        Button signup = findViewById(R.id.signUp);
        signup.setOnClickListener(view -> this.startActivity(new Intent(this, SignUp.class)));

        // got to settings activity----------------------------------------------------
        ImageButton settingsActivity = MainActivity.this.findViewById(R.id.goToSettings);
        settingsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
                MainActivity.this.startActivity(settingsIntent);
            }
        });

        // add Task button-------------------------------------------
        Button addTask = MainActivity.this.findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddATask.class);
                MainActivity.this.startActivity(addTaskIntent);
            }
        });

        // go to all tasks button-------------------------------------
        Button allTask = MainActivity.this.findViewById(R.id.allTasks);
        allTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyticsEvent("GoToAllTasks", "User is checking out all tasks");
                Log.i("Amplify.", "reached this");
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



//    //============= Verify User ============================
//    public void verifyUser(String userName, String code){
//        Amplify.Auth.confirmSignUp(
//                userName,
//                code,
//                result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
//                error -> Log.e("AuthQuickstart", error.toString())
//        );
//
//    }

//============= Add test Users =========================
//    public void addUsers(String userName, String password, String email){
//        Amplify.Auth.signUp(
//                userName,
//                password,
//                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
//                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
//                error -> Log.e("AuthQuickStart", "Sign up failed", error)
//        );
//    }


//        AnalyticsEvent event = AnalyticsEvent.builder()
//                .name("Opened app test")
//                .addProperty("time", Long.toString(new Date().getTime()))
//                .addProperty("openApp", "This one went through")
//                .build();
//        Amplify.Analytics.recordEvent(event);


   //=============== on Stop ==================================
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    protected void onStop() {
//        super.onStop();
//        analyticsEvent("StoppedApp", "User has stopped the app");
//
//    }
//
//    //=============== on Pause ==================================
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    protected void onPause() {
//        super.onPause();
//        analyticsEvent("PausedApp", "User has paused the app");
//
//    }
//
//    //=============== on destrtoy ================================
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    protected void onDestroy() {
//        super.onDestroy();
//        analyticsEvent("ExitApp", "User has exited the app");
//    }
