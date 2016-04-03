package com.peerwifi.peerwifi.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.peerwifi.peerwifi.R;

import java.lang.reflect.Method;

/**
 * Created by mdislam on 4/2/16.
 */
public class SignupActivity extends ParentActivity {

    EditText user_field;
    EditText pass_field;
    EditText cpass_field;
    EditText phone_field;
    EditText email_field;

    TextView signupBtn;
    TextView loginBtn;
    TextView skipBtn;

    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        wifiManager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfiguration = getWifiApConfiguration();
        checkConnection(wifiConfiguration);

        user_field = (EditText)findViewById(R.id.user_field);
        phone_field = (EditText) findViewById(R.id.phone_field);
        email_field = (EditText) findViewById(R.id.email_field);
        pass_field = (EditText)findViewById(R.id.pass_field);
        cpass_field = (EditText)findViewById(R.id.cpass_field);

        signupBtn = (TextView) findViewById(R.id.signupBtn);
        loginBtn = (TextView) findViewById(R.id.loginBtn);
        skipBtn = (TextView) findViewById(R.id.skipBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void signup() {
        String username = user_field.getText().toString().trim();
        String phone = phone_field.getText().toString().trim();
        String email = email_field.getText().toString().trim();
        String password = pass_field.getText().toString().trim();
        String cpassword = cpass_field.getText().toString().trim();


        if((username.isEmpty() || username.equals("")) &&
                (password.isEmpty() || password.equals("")) &&
                (email.isEmpty() || email.equals("")) &&
                (phone.isEmpty() || phone.equals(""))) {

            Toast.makeText(getApplicationContext(), "Please Fill All Fields", Toast.LENGTH_LONG).show();
        } else {
            if(password.equals(cpassword)) {

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.put("phone", phone);


                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("Sign Up Failed")
                                    .setMessage("Username is already in use. Please try again.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(R.drawable.wifi)
                                    .show();

                            Log.d("Crash", e.getMessage());
                        }
                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "Password Does Not Match Confirmation", Toast.LENGTH_LONG).show();
            }
        }
    }





    public WifiConfiguration getWifiApConfiguration() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            return (WifiConfiguration) method.invoke(wifiManager);
        } catch (Exception e) {
            Log.e("Crash", e.getMessage());
            return null;
        }
    }










}
