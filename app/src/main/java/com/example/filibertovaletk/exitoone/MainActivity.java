package com.example.filibertovaletk.exitoone;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;
import com.example.filibertovaletk.exitoone.adapters.PromotionAdapter;
import com.example.filibertovaletk.exitoone.models.Promotions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private Region region;
    private HashMap<String, HashMap<String, Boolean>> myBeacons = new HashMap<String, HashMap<String, Boolean>>();
    private HashMap<String, Boolean> beaconObject = new HashMap<String, Boolean>();
    private ArrayList<Promotions> promotionsList = new ArrayList<Promotions>();
    private Promotions promoOne = new Promotions("First Promo", "This is the first promo", "05/03/2016", "http://api.androidhive.info/json/movies/1.jpg");
    private Promotions promoTwo = new Promotions("Second Promo", "This is the second promo", "05/03/2016", "http://api.androidhive.info/json/movies/2.jpg");
    private Promotions promoThree = new Promotions("Third Promo", "This is the third promo", "05/03/2016", "http://api.androidhive.info/json/movies/3.jpg");
    private PromotionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconManager = new BeaconManager(this);

        beaconObject.put("detected", false);
        myBeacons.put("63172:10592", beaconObject);

        promotionsList.add(promoOne);
        promotionsList.add(promoTwo);
        promotionsList.add(promoThree);

        // add this below:
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                // V/SUCCESS: Beacon{macAddress=[DC:AE:29:60:F6:C4], proximityUUID=b9407f30-f5f8-466e-aff9-25556b57fe6d, major=63172, minor=10592, measuredPower=-74, rssi=-77} NEAR
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    if (Utils.computeProximity(nearestBeacon) == Utils.Proximity.NEAR) {
                        beaconFachada(nearestBeacon);
                        // TODO: update the UI here
                        Log.v("SUCCESS", nearestBeacon + " " + Utils.computeProximity(nearestBeacon));
                    } else {
                        Log.e("ERROR", "TOO FAR " + Utils.computeProximity(nearestBeacon));
                    }

                }
            }
        });

        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        adapter = new PromotionAdapter(getApplicationContext(), R.layout.list_row, promotionsList);
        ListView promotionListView = (ListView) findViewById(R.id.list);
        promotionListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onDestroy() {
        beaconManager.stopRanging(region);
        super.onDestroy();
    }

    private void beaconFachada(Beacon beacon){
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if(isARegisteredBeacon(beacon, beaconKey) && !wasAlreadyDetected(beacon, beaconKey) ){
            showNotification("Promocion", "Coca-Cola 2.5 Litros");
        }
    }

    private boolean isARegisteredBeacon(Beacon beacon, String key) {
        boolean is_registered = false;
        if (myBeacons.containsKey(key)) {
            Log.w("BEACON", "FOUNDED " + myBeacons.get(key));
            is_registered = true;
        }
        return is_registered;
    }

    private boolean wasAlreadyDetected(Beacon beacon, String key){
        boolean was_detected = true;
        HashMap<String, Boolean> beacon_temp = myBeacons.get(key);
        if(!beacon_temp.get("detected")){
            beacon_temp.put("detected", true);
            was_detected = false;
        }
        return was_detected;
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.w("NOTIFICATION","INTENT");
        Promotions promoFourth = new Promotions("Fourth Promo", "This is the fourth promo", "05/03/2016", "http://api.androidhive.info/json/movies/4.jpg");
        promotionsList.add(promoFourth);
        adapter.notifyDataSetChanged();
    }
}
