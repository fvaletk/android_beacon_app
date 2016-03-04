package com.example.filibertovaletk.exitoone;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private Region region;
    private HashMap<String, String> myBeacons = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconManager = new BeaconManager(this);

        myBeacons.put("63172:10592", "GOT YOUR BEACON");

        // add this below:
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                // V/SUCCESS: Beacon{macAddress=[DC:AE:29:60:F6:C4], proximityUUID=b9407f30-f5f8-466e-aff9-25556b57fe6d, major=63172, minor=10592, measuredPower=-74, rssi=-77} NEAR
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    if(Utils.computeProximity(nearestBeacon) == Utils.Proximity.NEAR){
                        String the_beacon = nearBeacons(nearestBeacon);
                        // TODO: update the UI here
                        Log.v("SUCCESS", nearestBeacon+" "+Utils.computeProximity(nearestBeacon));
                    }else{
                        Log.e("ERROR", "TOO FAR "+Utils.computeProximity(nearestBeacon));
                    }

                }
            }
        });

        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

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

    private String nearBeacons(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (myBeacons.containsKey(beaconKey)) {
            myBeacons.put(beaconKey, "NEW VALUE");
            Log.w("BEACON", "FOUNDED " + myBeacons.get(beaconKey));
            return "founded";
        }
        return "none";
    }
}
