package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peerwifi.peerwifi.R;
import com.peerwifi.peerwifi.core.Wifi_Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdislam on 4/2/16.
 */
public class ConnectActivity extends Activity {

    ArrayList<Wifi_Item> wifi_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);

        ImageView backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ListView wifi_list = (ListView) findViewById(R.id.wifi_list);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

        wifi_items = new ArrayList<Wifi_Item>();

        List<ScanResult> list = wifiManager.getScanResults();
        for(ScanResult i : list) {
            boolean added = false;
            for (Wifi_Item item : wifi_items) {
                if (item.getSSID().equals(i.SSID)) {
                    added = true;
                    break;
                }
            }

            if (!added) {
                Wifi_Item wifi_item = new Wifi_Item();
                wifi_item.setSSID(i.SSID);
                wifi_item.setPrice(new BigDecimal("5.00"));
                wifi_item.setLimit(500);
                wifi_items.add(wifi_item);
            }
        }


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Wifi");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (Wifi_Item wifi_item : wifi_items) {
                        boolean exists = true;
                        for (ParseObject object : objects) {

                            if (object.getString("SSID").equals(wifi_item.getSSID())) {
                                exists = true;
                                break;
                            }
                        }

                        if (!exists) {
                        }
                    }
                }else{

                }
            }

            );


            WifiAdapter wifiAdapter = new WifiAdapter();
        wifi_list.setAdapter(wifiAdapter);

        }


        private class WifiAdapter extends ArrayAdapter<Wifi_Item>{

        public WifiAdapter() {
            super(getApplicationContext(), R.layout.wifi_list_item, wifi_items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Wifi_Item wifi_item = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.wifi_list_item, parent, false);
            }

            TextView ssid = (TextView) convertView.findViewById(R.id.ssid);
            TextView limit = (TextView) convertView.findViewById(R.id.limit);
            TextView price = (TextView) convertView.findViewById(R.id.price);


            ssid.setText(wifi_item.getSSID());
            limit.setText(Double.toString(wifi_item.getLimit())+" Mb");
            price.setText("$"+wifi_item.getPrice());



            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            return convertView;
        }


    }


}
