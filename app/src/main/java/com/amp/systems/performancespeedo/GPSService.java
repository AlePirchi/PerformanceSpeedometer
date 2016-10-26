package com.amp.systems.performancespeedo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;


/**
 * Created by Alejandro M. Pirchi on 10/25/2016.
 */

public class GPSService extends Service
{

    private LocationListener listener;
    private LocationManager locationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location)
            {
                Intent i = new Intent( "location_Update" );
                i.putExtra("speed", location.getSpeed());
                i.putExtra("coordinates",+ location.getLongitude() +" " + location.getLatitude());


                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) //What to do when location services are disabled
            {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 90, 0, listener);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onDestroy()
    {
        if (locationManager != null)
        {
            locationManager.removeUpdates(listener);
        }

    }
}
