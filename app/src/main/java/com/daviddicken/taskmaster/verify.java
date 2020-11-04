package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class verify extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    String namePassedIn;
    String verifyCode;
    String password;
    Boolean flag = false;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        populateViews();
        getData();
    }

    //============= Log In =================================
    public void logIn(String userName, String password){
        Amplify.Auth.signIn(
                userName,
                password,
                result -> {
                    System.out.println("result " + result);
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                    System.out.println("Logged in!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      //              flag = true;
//                    onBackPressed();
                    //finish();
//                    preferencesEditor.putString("password", "");
//                    preferencesEditor.apply();
                   // Intent intent
                    this.startActivity(new Intent(verify.this, MainActivity.class));
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );

    }

    //============= Get Data ===============================
    public void getData(){
        ((Button) findViewById(R.id.confirmButton)).setOnClickListener(view ->{
            verifyCode = ((TextView) findViewById(R.id.verificationCode)).getText().toString();
            verifyUser(namePassedIn, verifyCode);
        });
    }

    //============= Verify User ============================
    public void verifyUser(String userName, String code){
        Amplify.Auth.confirmSignUp(
                userName,
                code,
                result -> {
                    Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    System.out.println("before log in111111111111111111111111111111111111111111111111111111111111");
                    logIn(userName, password);
                    System.out.println("made it this far!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

//                    createToast();
//                    toastAndGo();
                },
                error -> {
                    Log.e("AuthQuickstart", error.toString());
                    //failedToast();
                    //toast.show();
                    startActivity(new Intent(verify.this, verify.class));
                }

        );

    }

    //============ Populate views (Users name) ===============
    public void populateViews() {

        preferences = PreferenceManager.getDefaultSharedPreferences(this); // getter
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        TextView name = findViewById(R.id.titleName);
        namePassedIn = preferences.getString("usersName", "");
        password = preferences.getString("password", "");


        name.setText(String.format("%s\nPlease check your email for verification code.", namePassedIn));

    }
}

//===================== Zombie code =============================

//    //=========== Toast setup ======================
//    public void createToast(boolean passed) {
//        //https://developer.android.com/guide/topics/ui/notifiers/toasts.html
//        Context context = getApplicationContext();
//        CharSequence text = "";
//        int duration;
//        //passed ? text = "Verified" : text = "Verification failed. Please try again.";
//        if(passed) {
//            text = "Verified";
//            duration = Toast.LENGTH_SHORT;
//        }else{
//            text = "Verification failed";
//            duration = Toast.LENGTH_LONG;
//        }
//
//        toast = Toast.makeText(context, text, duration);
//    }



