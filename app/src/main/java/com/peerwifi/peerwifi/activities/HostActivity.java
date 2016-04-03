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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.peerwifi.peerwifi.R;
import com.peerwifi.peerwifi.core.Wifi_Item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * Created by mdislam on 4/3/16.
 */
public class HostActivity extends ParentActivity {


    private WifiManager wifiManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity);

        checkConnection();

        if(ParseUser.getCurrentUser() == null){
            Intent intent = new Intent(HostActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        final EditText ssid = (EditText) findViewById(R.id.ssid);
        final EditText price = (EditText) findViewById(R.id.price);
        final EditText limit = (EditText) findViewById(R.id.limit);

        wifiManager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfiguration = getWifiApConfiguration();
        ssid.setText(wifiConfiguration.SSID);
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

                    startHotspot(newPrice, newLimit);

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





    private void startHotspot(final BigDecimal price, final int limit){

        if(wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        WifiConfiguration wifi_configuration = null;

        try {
            //USE REFLECTION TO GET METHOD "SetWifiAPEnabled"
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, wifi_configuration, true);

            final ParseObject parseObject = new ParseObject("WifiItem");
            parseObject.put("userId", ParseUser.getCurrentUser().getObjectId());
            parseObject.put("SSID", getWifiApConfiguration().SSID);
            parseObject.put("password", getWifiApConfiguration().preSharedKey);
            parseObject.put("price", price);
            parseObject.put("limit", limit);

            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        Wifi_Item wifi_item = new Wifi_Item();
                        wifi_item.setId(parseObject.getObjectId());
                        wifi_item.setSSID(getWifiApConfiguration().SSID);
                        wifi_item.setPrice(price);
                        wifi_item.setLimit(limit);
                        wifi_item.setPassword(getWifiApConfiguration().preSharedKey);

                        setWifi_item(wifi_item);

                        Intent intent = new Intent(HostActivity.this, ConnectedActivity.class);
                        intent.putExtra("WifiItem", wifi_item);
                        startActivity(intent);
                        finish();

                    } else {

                        disconnect();

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


                    }
                }
            });


        }
        catch (NoSuchMethodException a) {
            a.printStackTrace();
        }
        catch (IllegalArgumentException a) {
            a.printStackTrace();
        }
        catch (IllegalAccessException a) {
            a.printStackTrace();
        }
        catch (InvocationTargetException a) {
            a.printStackTrace();
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
