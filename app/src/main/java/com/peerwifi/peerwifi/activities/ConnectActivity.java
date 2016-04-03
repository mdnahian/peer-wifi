package com.peerwifi.peerwifi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.parse.ParseUser;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.peerwifi.peerwifi.R;
import com.peerwifi.peerwifi.core.WifiConnectionService;
import com.peerwifi.peerwifi.core.Wifi_Item;

import org.json.JSONException;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdislam on 4/2/16.
 */
public class ConnectActivity extends ParentActivity {

    ArrayList<Wifi_Item> wifi_items;
    private static PayPalConfiguration config = new PayPalConfiguration();

    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);

        wifiManager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfiguration = getWifiApConfiguration();
        checkConnection(wifiConfiguration);

        config.environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK);
        config.clientId(getString(R.string.paypalAPIKey));

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        ImageView backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final ListView wifi_list = (ListView) findViewById(R.id.wifi_list);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

        wifi_items = new ArrayList<>();

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
                wifi_item.setDuration(30);
                wifi_items.add(wifi_item);
            }
        }


        ParseQuery<ParseObject> query = ParseQuery.getQuery("WifiItem");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ArrayList<Wifi_Item> wifi_items_copy = new ArrayList<Wifi_Item>();
                    wifi_items_copy.addAll(wifi_items);

                    for (Wifi_Item wifi_item : wifi_items_copy) {
                        boolean exists = false;
                        for (ParseObject object : objects) {
                            if (object.getString("SSID").equals(wifi_item.getSSID())) {
                                wifi_items.remove(wifi_item);

                                wifi_item.setId(object.getObjectId());
                                wifi_item.setPassword(object.getString("password"));
                                wifi_item.setPrice(new BigDecimal(object.getInt("price")));
                                wifi_item.setDuration(object.getInt("time"));

                                wifi_items.add(wifi_item);

                                exists = true;
                                break;
                            }
                        }

                        if (!exists) {
                            wifi_items.remove(wifi_item);
                        }
                    }

                    if(wifi_items.size() > 0) {
                        WifiAdapter wifiAdapter = new WifiAdapter();
                        wifi_list.setAdapter(wifiAdapter);
                    } else {
                        new AlertDialog.Builder(ConnectActivity.this)
                                .setTitle("No Peer Wifi Mobile Hotspot Found")
                                .setMessage("Please rescan to search for a mobile hotspot again.")
                                .setPositiveButton(R.string.rescan, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ConnectActivity.this, ConnectActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.wifi)
                                .show();
                    }

                } else {
                    Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });


    }





    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }





    private class WifiAdapter extends ArrayAdapter<Wifi_Item>{

        public WifiAdapter() {
            super(getApplicationContext(), R.layout.wifi_list_item, wifi_items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Wifi_Item wifi_item = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.wifi_list_item, parent, false);
            }

            final TextView ssid = (TextView) convertView.findViewById(R.id.ssid);
            final TextView time = (TextView) convertView.findViewById(R.id.time);
            final TextView price = (TextView) convertView.findViewById(R.id.price);


            ssid.setText(wifi_item.getSSID());
            time.setText(Double.toString(wifi_item.getDuration())+" minutes");
            price.setText("$"+wifi_item.getPrice());



            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WifiConnect wifiConnect = new WifiConnect(wifi_item);
                    wifiConnect.execute();
                }
            });


            return convertView;
        }


    }



    private class WifiConnect extends AsyncTask<String, Void, Void>{

        Wifi_Item wifi_item;

        public WifiConnect(Wifi_Item wifi_item) {
            this.wifi_item = wifi_item;
        }

        @Override
        protected Void doInBackground(String... params) {

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = String.format("\"%s\"", wifi_item.getSSID());
            conf.preSharedKey = String.format("\"%s\"", wifi_item.getPassword());

            WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
            int netId = wifiManager.addNetwork(conf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setWifi_item(wifi_item);

            PayPalPayment payment = new PayPalPayment(wifi_item.getPrice(), "USD", "Peer Wifi - "+wifi_item.getSSID(), PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(ConnectActivity.this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, 0);
        }


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("Crash", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                    Intent service = new Intent(ConnectActivity.this, WifiConnectionService.class);
                    service.putExtra("time", getWifi_item().getDuration()*60);
                    startService(service);

                    if(ParseUser.getCurrentUser() != null){
                        ParseObject parseObject = new ParseObject("WifiConnection");
                        parseObject.put("userId", ParseUser.getCurrentUser().getObjectId());
                        parseObject.put("wifi_item", getWifi_item());

                        wifiManager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
                        WifiConfiguration wifiConfiguration = getWifiApConfiguration();
                        checkConnection(wifiConfiguration);
                    } else {
                        Intent intent = new Intent(ConnectActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    Log.e("Crash", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else {
            WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
            wifiManager.disconnect();
        }

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
