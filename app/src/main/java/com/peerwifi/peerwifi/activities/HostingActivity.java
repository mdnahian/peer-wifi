package com.peerwifi.peerwifi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.peerwifi.peerwifi.R;
import com.peerwifi.peerwifi.core.Wifi_Item;

/**
 * Created by mdislam on 4/3/16.
 */
public class HostingActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connected_activity);

        Intent intent = getIntent();
        Wifi_Item wifi_item = (Wifi_Item) intent.getSerializableExtra("WifiItem");
        setWifi_item(wifi_item);
        checkConnection(this);

        TextView ssid = (TextView) findViewById(R.id.ssid);
        TextView price = (TextView) findViewById(R.id.price);
        TextView limit = (TextView) findViewById(R.id.limit);

        ssid.setText(wifi_item.getSSID());
        price.setText("$"+wifi_item.getPrice());
        limit.setText(Double.toString(wifi_item.getDuration())+" minutes");

        TextView disconnectBtn = (TextView) findViewById(R.id.disconnectBtn);
        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
                checkConnection(HostingActivity.this);
            }
        });
    }


}
