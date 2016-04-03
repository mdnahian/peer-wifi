package com.peerwifi.peerwifi.core;

import android.app.Service;
import android.content.Intent;
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
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(counter < 120) {
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
        stopSelf();
        super.onDestroy();
        Toast.makeText(this, counter + " Count When running", Toast.LENGTH_LONG).show();
    }

}
