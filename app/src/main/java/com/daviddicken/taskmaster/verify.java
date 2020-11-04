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
    String namePassedIn;
    String verifyCode;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        populateViews();
        getData();
        verifyUser(namePassedIn, verifyCode);
    }

    //=========== Toast and Go =====================
    public void toastAndGo() {
        toast.show();
        onBackPressed();
    }

    //=========== Toast setup ======================
    public void createToast() {
        //https://developer.android.com/guide/topics/ui/notifiers/toasts.html
        Context context = getApplicationContext();
        CharSequence text = "Verified";
        int duration = Toast.LENGTH_SHORT;

        toast = Toast.makeText(context, text, duration);
    }

    //============= Failed Toast ===========================
//    public void failedToast() {
//        //https://developer.android.com/guide/topics/ui/notifiers/toasts.html
//        Context context = getApplicationContext();
//        CharSequence text = "Failed";
//        int duration = Toast.LENGTH_LONG;
//
//        toast = Toast.makeText(context, text, duration);
//    }

    //============= Get Data ===============================
    public void getData(){
        ((Button) findViewById(R.id.confirmButton)).setOnClickListener(view ->{
            verifyCode = ((TextView) findViewById(R.id.verificationCode)).getText().toString();
        });
    }

    //============= Verify User ============================
    public void verifyUser(String userName, String code){
        Amplify.Auth.confirmSignUp(
                userName,
                code,
                result -> {
                    Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    createToast();
                    toastAndGo();
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

        TextView name = findViewById(R.id.titleName);
        namePassedIn = preferences.getString("usersName", "");

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