package com.peerwifi.peerwifi.core;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by mdislam on 4/3/16.
 */
public class WifiConnectionService extends Service {

    int counter;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        final int time = intent.getIntExtra("time", 0);


        Toast.makeText(this, "Connected to Peer Wifi", Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(counter < time) {
                    counter++;
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }

                stopSelf();
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Toast.makeText(this, counter + "Disconnected from Peer Wifi", Toast.LENGTH_LONG).show();
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        wifiManager.disconnect();
    }

}
