package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.peerwifi.peerwifi.core.Wifi_Item;

import java.lang.reflect.Method;
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

    public void disconnect(){

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("WifiItem");
//        query.getInBackground(wifi_id, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if(e == null){
//                    object.deleteEventually();
//                }
//            }
//        });

        configApState(getApplicationContext());
        wifi_item = null;
    }

    public void checkConnection(){
        if(isConnected()){
            Intent intent = new Intent(ParentActivity.this, ConnectedActivity.class);
            intent.putExtra("WifiItem", wifi_item);
            startActivity(intent);
            finish();
        }
    }


    public void checkConnection(Context context){
        if(!isConnected()){
            disconnect();
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
