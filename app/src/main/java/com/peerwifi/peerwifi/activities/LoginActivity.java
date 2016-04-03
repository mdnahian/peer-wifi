package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.peerwifi.peerwifi.R;

/**
 * Created by mdislam on 4/2/16.
 */
public class LoginActivity extends Activity {

    EditText user_field;
    EditText pass_field;

    TextView signupBtn;
    TextView loginBtn;
    TextView skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, getString(R.string.parseAppId), getString(R.string.parseClientId));
            ParseInstallation.getCurrentInstallation().saveInBackground();
        } catch (Exception e){
            Log.d("Crash", "Application Crashed. Error Report Sent");
        }


        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        user_field = (EditText)findViewById(R.id.user_field);
        pass_field = (EditText)findViewById(R.id.pass_field);

        signupBtn = (TextView) findViewById(R.id.signupBtn);
        loginBtn = (TextView) findViewById(R.id.loginBtn);
        skipBtn = (TextView) findViewById(R.id.skipBtn);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });


        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void signin() {
        String username = user_field.getText().toString().trim();
        String password = pass_field.getText().toString().trim();

        if((username.isEmpty() || username.equals("")) || (password.isEmpty() || password.equals(""))) {
            Toast.makeText(getApplicationContext(), "Please Fill All Fields", Toast.LENGTH_LONG).show();
        } else {

            ParseUser.logInInBackground(username, password, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Cannot Login")
                                .setMessage("Username or Password Incorrect")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(R.drawable.wifi)
                                .show();
                    }
                }
            });

        }


    }

}
