package com.peerwifi.peerwifi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.peerwifi.peerwifi.R;

/**
 * Created by mdislam on 4/3/16.
 */
public class MainActivity extends ParentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        TextView hostBtn = (TextView) findViewById(R.id.hostBtn);
        hostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HostActivity.class);
                startActivity(intent);
                finish();
            }
        });


        TextView connectBtn = (TextView) findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    private void logout(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
