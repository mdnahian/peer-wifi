package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.peerwifi.peerwifi.R;
import com.peerwifi.peerwifi.core.Wifi_Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdislam on 4/2/16.
 */
public class HostActivity extends Activity {

    ArrayList<Wifi_Item> wifi_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity);

        ListView wifi_list = (ListView) findViewById(R.id.wifi_list);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

        wifi_items = new ArrayList<>();

        List<ScanResult> list = wifiManager.getScanResults();
        for(ScanResult i : list) {
            Wifi_Item wifi_item = new Wifi_Item();
            wifi_item.setSSID(i.SSID);
            wifi_item.setPrice(new BigDecimal("5.00"));
            wifi_item.setLimit(500);

            wifi_items.add(wifi_item);
        }


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
