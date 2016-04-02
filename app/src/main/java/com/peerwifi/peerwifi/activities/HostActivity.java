package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.peerwifi.peerwifi.R;

import java.util.List;

/**
 * Created by mdislam on 4/2/16.
 */
public class HostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity);

        ListView wifi_list = (ListView) findViewById(R.id.wifi_list);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            Log.d("Crash", i.SSID);
        }



    }


}
