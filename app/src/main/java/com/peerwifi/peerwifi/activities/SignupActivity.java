package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.peerwifi.peerwifi.R;

import org.bson.Document;

/**
 * Created by mdislam on 4/2/16.
 */
public class SignupActivity extends Activity {

    EditText user_field;
    EditText pass_field;
    EditText cpass_field;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_field = (EditText)findViewById(R.id.user_field);
        pass_field = (EditText)findViewById(R.id.pass_field);
        cpass_field = (EditText)findViewById(R.id.cpass_field);

    }

    public void signup(View view) {
        String username = user_field.getText().toString().trim();
        String password = pass_field.getText().toString().trim();
        String cpassword = cpass_field.getText().toString().trim();

        if(username.isEmpty() || username.equals("")) {
            Toast.makeText(getApplicationContext(), "Username Cannot Be Empty", Toast.LENGTH_LONG).show();
        } else {
            if(password.isEmpty() || password.equals("")) {
                Toast.makeText(getApplicationContext(), "Password Cannot Be Empty", Toast.LENGTH_LONG).show();
            } else {
                if(password.equals(cpassword)) {

                    MongoClientURI uri = new MongoClientURI( "mongodb://root:peerwifi@http://104.236.93.62/:12345/test" );
                    MongoClient mongoClient = new MongoClient(uri);
                    MongoDatabase db = mongoClient.getDatabase(uri.getDatabase());

                    MongoCollection<Document> collection = db.getCollection("test");

                    Document doc = new Document("name", "MongoDB")
                            .append("type", "database")
                            .append("count", 1)
                            .append("info", new Document("x", 203).append("y", 102));

                    collection.insertOne(doc);


                } else {
                    Toast.makeText(getApplicationContext(), "Password Does Not Match Confirmation", Toast.LENGTH_LONG).show();
                }
            }
        }




    }

//    // Method to start the service
//    public void startService(View view) {
//        startService(new Intent(getBaseContext(), wifiService.class));
//    }
//
//    // Method to stop the service
//    public void stopService(View view) {
//        stopService(new Intent(getBaseContext(), wifiService.class));
//    }




}
