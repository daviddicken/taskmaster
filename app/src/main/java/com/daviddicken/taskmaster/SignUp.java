package com.daviddicken.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUp extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // SharePreferences setup (local storage)
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesEditor = preferences.edit();

        createToast();
        getInfo();
    }

    //============= Get info ==============================
    public void getInfo(){
        ((Button) findViewById(R.id.signupButton)).setOnClickListener(v -> {
            String userName = ((TextView) findViewById(R.id.userNameInput)).getText().toString();
            String password = ((TextView) findViewById(R.id.passwordInput)).getText().toString();
            String email = ((TextView) findViewById(R.id.emailInput)).getText().toString();

            preferencesEditor.putString("usersName", userName);
            preferencesEditor.apply();
            signup(userName, password, email);
        });
    }

    //============= Sign User up =========================
    public void signup(String userName, String password, String email){
        Amplify.Auth.signUp(
                userName,
                password,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
                result -> {
                    Log.i("Signup", "Result: " + result.toString());
                    toast.show();
                    confirm();
                },
                error -> Log.e("Signup", "Sign up failed", error)
        );
        onBackPressed();
    }

    //=========== Send to confirmation =============
    public void confirm(){
        startActivity(new Intent(SignUp.this, verify.class));


    }

    //=========== Toast setup ======================
    public void createToast() {
        //https://developer.android.com/guide/topics/ui/notifiers/toasts.html
        Context context = getApplicationContext();
        CharSequence text = "User created. Go to email to authenticate.";
        int duration = Toast.LENGTH_LONG;

        toast = Toast.makeText(context, text, duration);
    }
}