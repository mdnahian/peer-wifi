package com.peerwifi.peerwifi.activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.peerwifi.peerwifi.R;
import com.peerwifi.peerwifi.core.Wifi_Item;

import java.lang.reflect.Method;

/**
 * Created by mdislam on 4/3/16.
 */
public class HostingActivity extends ParentActivity {


    private WifiManager wifiManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connected_activity);

        Intent intent = getIntent();
        Wifi_Item wifi_item = (Wifi_Item) intent.getSerializableExtra("WifiItem");
        setWifi_item(wifi_item);

        wifiManager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
        final WifiConfiguration wifiConfiguration = getWifiApConfiguration();
        checkConnection(this, wifiConfiguration);

        TextView ssid = (TextView) findViewById(R.id.ssid);
        TextView price = (TextView) findViewById(R.id.price);
        TextView time = (TextView) findViewById(R.id.time);

        ssid.setText(wifi_item.getSSID());
        price.setText("$"+wifi_item.getPrice());
        time.setText(Double.toString(wifi_item.getDuration())+" minutes");

        TextView disconnectBtn = (TextView) findViewById(R.id.disconnectBtn);
        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect(wifiConfiguration);
            }
        });
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
