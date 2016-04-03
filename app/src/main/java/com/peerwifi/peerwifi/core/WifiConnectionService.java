package com.peerwifi.peerwifi.core;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
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
        Log.d("Crash", Integer.toString(time));

        Toast.makeText(this, "Connected to Peer Wifi", Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(counter < time) {
                    counter++;
                    try {
                        Thread.sleep(1000);
                        Log.d("Crash", "1 Second");
                    } catch (Exception e) {

                    }
                }

                WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
                wifiManager.disconnect();

                stopSelf();
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();

        Toast.makeText(this, counter + "Disconnected from Peer Wifi", Toast.LENGTH_LONG).show();
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        wifiManager.disconnect();
    }

}
