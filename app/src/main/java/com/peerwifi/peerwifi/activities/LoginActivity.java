package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.peerwifi.peerwifi.R;

/**
 * Created by mdislam on 4/2/16.
 */
public class LoginActivity extends Activity {

    EditText user_field;
    EditText pass_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_field = (EditText)findViewById(R.id.user_field);
        pass_field = (EditText)findViewById(R.id.pass_field);
    }

    public void signin(View view) {
        String username = user_field.getText().toString().trim();
        String password = pass_field.getText().toString().trim();

        if(username.isEmpty() || username.equals("")) {
            Toast.makeText(getApplicationContext(), "Username can't be empty", Toast.LENGTH_LONG).show();
        }
        if(password.isEmpty() || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Password can't be empty", Toast.LENGTH_LONG).show();
        }

    }

}
