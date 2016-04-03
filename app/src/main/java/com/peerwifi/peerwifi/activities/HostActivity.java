package com.peerwifi.peerwifi.activities;

import android.app.Activity;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.peerwifi.peerwifi.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by mdislam on 4/3/16.
 */
public class HostActivity extends Activity {


    private WifiManager wifiManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity);

        if(ParseUser.getCurrentUser() == null){
            Intent intent = new Intent(HostActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        final EditText ssid = (EditText) findViewById(R.id.ssid);
        final EditText price = (EditText) findViewById(R.id.price);
        final EditText limit = (EditText) findViewById(R.id.limit);

        ssid.setText("peer-wifi-" + ParseUser.getCurrentUser().getUsername());
        ssid.setEnabled(false);

        price.setText("5.00");
        limit.setText("500");


        TextView startBtn = (TextView) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!price.getText().toString().equals("") && !limit.getText().equals("")){
                    BigDecimal newPrice = new BigDecimal(price.getText().toString());
                    int newLimit = Integer.parseInt(limit.getText().toString());

                    startHotspot(ssid.getText().toString(), newPrice, newLimit);

                } else {

                    new AlertDialog.Builder(HostActivity.this)
                            .setTitle("Could Not Start Hotspot")
                            .setMessage("Please fill all fields and try again.")
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





    private void startHotspot(String ssid, BigDecimal price, int limit){

        boolean isStarted;

        String newPassword = generateRandomString();

        wifiManager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        try {
            if(changeConfiguration(ssid, newPassword)){
                isStarted = true;
            } else {
                isStarted = false;
            }
        } catch (Exception e) {
            Log.d("Crash", e.getMessage());
            isStarted = false;
        }



        if(isStarted){

            ParseObject parseObject = new ParseObject("WifiItem");
            parseObject.put("userId", ParseUser.getCurrentUser().getObjectId());
            parseObject.put("SSID", ssid);
            parseObject.put("password", newPassword);
            parseObject.put("price", price);
            parseObject.put("limit", limit);

            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        new AlertDialog.Builder(HostActivity.this)
                                .setTitle("Failed to Start Hotspot")
                                .setMessage("Are you logged in?")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(R.drawable.wifi)
                                .show();
                    } else {

                    }
                }
            });


        } else {
            new AlertDialog.Builder(HostActivity.this)
                    .setTitle("Failed to Start Hotspot")
                    .setMessage("Please make sure all fields are valid.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(R.drawable.wifi)
                    .show();
        }






    }







    private WifiConfiguration getWifiApConfiguration() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            return (WifiConfiguration) method.invoke(wifiManager);
        } catch (Exception e) {
            Log.e("Crash", e.getMessage());
            return null;
        }
    }

    private boolean changeConfiguration(String newSSID, String newPassword) {

        WifiConfiguration mWifiConfiguration = getWifiApConfiguration();

        if(mWifiConfiguration != null){
            Log.d("Crash", mWifiConfiguration.SSID);
            mWifiConfiguration.SSID = newSSID;
            mWifiConfiguration.preSharedKey = newPassword;
            mWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            mWifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

            try {
                Method method = wifiManager.getClass().getMethod("setWifiApConfiguration", HostActivity.class);
                Object s = method.invoke(wifiManager, mWifiConfiguration, true);
                Log.d("Crash", s.toString());

            } catch (IllegalAccessException e) {
                Log.d("Crash", e.getMessage());
            } catch (InvocationTargetException e) {
                Log.d("Crash", e.getMessage());
            } catch (NoSuchMethodException e) {
                Log.d("Crash", e.getMessage());
            }
        } else {
            Log.d("Crash", "Returned False...");
        }


        return false;
    }

    private static String generateRandomString(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }







}
