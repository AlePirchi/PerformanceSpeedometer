package com.amp.systems.performancespeedo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView speedText;
    TextView unitText;
    Button startButton;
    Button resetButton;

    int startClickCount = 1; //Counts the clicks in button.
    boolean isRunning;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedText = (TextView) findViewById(R.id.speedText);
        unitText = (TextView) findViewById(R.id.unitText);
        startButton = (Button) findViewById(R.id.startButton);
        resetButton = (Button) findViewById(R.id.resetButton);


        if (!runtimePermissions()) //Will call a method that checks Location services are permitted.
        {
            enableButtons();
        }
    }



    private boolean runtimePermissions()
    {
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            return true;
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                enableButtons();
            }
            else
            {
                runtimePermissions();
            }
        }
    }



    private void enableButtons() //using this method will not require the use of two buttons for On/Off
    {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClickCount ++; //Counts how many time the button has been clicked
                if (startClickCount % 2 == 0) //If it is even
                {
                    Intent i = new Intent(getApplicationContext(), GPSService.class);
                    startService(i);
                    startButton.setText("STOP");
                    speedText.setText("0");
                    isRunning = true;


                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), GPSService.class);
                    stopService(i);
                    startButton.setText("START");
                    isRunning = false;
                    speedText.setText("0");
                }

            }
        });
    }


    //Location location;

    private BroadcastReceiver broadcastReceiver;

    String slat1,slon1,slat2,slon2;


    @Override
    protected void onResume()
    {
        super.onResume();
        if (broadcastReceiver == null)
        {
            broadcastReceiver = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    if (intent.getExtras().get("speed") != null)
                    {
                        //noinspection ConstantConditions
                        CoreFunctionality.getGroundSpeed(intent.getExtras().get("speed").toString());
                        speedText.setText(Double.toString(CoreFunctionality.groundSpeed));


                    }
                    if (intent.getExtras().get("coordinates") != null)
                    {
                        
                    }

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_Update") );
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (broadcastReceiver != null)
        {
            unregisterReceiver(broadcastReceiver);
        }
    }

}





