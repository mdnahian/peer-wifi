package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peerwifi.peerwifi.core.Wifi_Item;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Created by mdislam on 4/3/16.
 */
public class ParentActivity extends Activity {

    private Wifi_Item wifi_item;


    public boolean isConnected(){
        return (wifi_item != null);
    }

    public void setWifi_item(Wifi_Item wifi_item){
        this.wifi_item = wifi_item;
    }

    public Wifi_Item getWifi_item() {
        return wifi_item;
    }

    public void disconnect(WifiConfiguration wifiConfiguration){

        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("WifiItem");
        parseQuery.whereContains("SSID", wifiConfiguration.SSID);
        parseQuery.whereContains("password", wifiConfiguration.preSharedKey);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        object.deleteEventually();
                    }

                    configApState(getApplicationContext());
                    wifi_item = null;

                    Intent intent = new Intent(ParentActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }

    public void checkConnection(final WifiConfiguration wifiConfiguration){

        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("WifiItem");
        parseQuery.whereContains("SSID", wifiConfiguration.SSID);
        parseQuery.whereContains("password", wifiConfiguration.preSharedKey);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    if(objects.size() == 1){
                        ParseObject parseObject = objects.get(0);
                        Wifi_Item wifi_item = new Wifi_Item();
                        wifi_item.setId(parseObject.getObjectId());
                        wifi_item.setSSID(parseObject.getString("SSID"));
                        wifi_item.setPrice(new BigDecimal(parseObject.getInt("price")));
                        wifi_item.setDuration(parseObject.getInt("duration"));

                        Intent intent = new Intent(ParentActivity.this, HostingActivity.class);
                        intent.putExtra("WifiItem", wifi_item);
                        startActivity(intent);
                        finish();

                    } else if(objects.size() > 1){
                        disconnect(wifiConfiguration);
                    }

                }
            }
        });

    }


    public void checkConnection(Context context, WifiConfiguration wifiConfiguration){
        if(!isConnected()){
            disconnect(wifiConfiguration);
            Intent intent = new Intent(ParentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public static String generateRandomString(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }




    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable ignored) {}
        return false;
    }


    public static boolean configApState(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            if(isApOn(context)) {
                wifimanager.setWifiEnabled(false);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }







}
