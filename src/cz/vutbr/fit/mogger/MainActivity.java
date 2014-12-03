package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import cz.vutbr.fit.mogger.AboutActivity;
import cz.vutbr.fit.mogger.R;
import cz.vutbr.fit.mogger.SettingsActivity;
import cz.vutbr.fit.mogger.gesture.GestureManager;

import java.util.ArrayList;

/**
 * Created by murry on 29.11.14.
 */
public class MainActivity extends Activity {
    // get started tlacitko
    private ImageButton btnStarted = null;

    SensorManager sensorManager;
    Sensor accelerometer;
    Sounds sounds;

    GestureManager gestureManager;
    MainActivityListener fastestListener;

    public MainActivity() {
        sounds = new Sounds();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        gestureManager = GestureManager.createInstance(getApplicationContext());

        btnStarted = (ImageButton)findViewById(R.id.btnGetStarted);

        // kliknuti na tlacitko get started
        btnStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // settings
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(myIntent);
            }
        });

        // prvni spusteni
        GetStarted();


        //init senzoru
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        fastestListener = new MainActivityListener(this, gestureManager);
        sensorManager.registerListener(fastestListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        //start!
        fastestListener.startRecording();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        menu.add(0, 0, 0, "Settings");
        menu.add(0, 1, 1, "About");

        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == 0)
        {
            // settings
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            this.startActivity(myIntent);
        }
        if(id == 1)
        {
            //about
            Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
            this.startActivity(myIntent);
        }

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void onResume() {
        super.onResume();

        //pokracuj v testovani podobnosti
        sensorManager.registerListener(fastestListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        fastestListener.startRecording();

        //reflesh
        GetStarted();
    }

    protected void onPause() {
        super.onPause();

        //pozastav testovani podobnosti
        sensorManager.unregisterListener(fastestListener);
        fastestListener.stopRecording();
    }

    /**
     * Nastaveni viditelnosti GetStarted tlacitla
     */
    private void GetStarted()
    {
        // pokud je prazdny seznam gest
        if (gestureManager.isEmpty())
        {
            btnStarted.setVisibility(View.VISIBLE);
        }
        else
        {
            btnStarted.setVisibility(View.INVISIBLE);
        }
    }
}