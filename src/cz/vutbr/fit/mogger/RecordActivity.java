package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cz.vutbr.fit.mogger.gesture.GestureManager;

/**
 * Created by murry on 2.12.14.
 */
public class RecordActivity extends Activity {


    // pozice gest v seznamu
    int position = 0;
    Gesture g = null;

    TextView textView;
    Button button1;
    Button btnRecord;
    SensorManager sensorManager;
    Sensor accelerometer;
    Sounds sounds;

    GestureManager gestureManager;
    RecordListener fastestListener;

    public RecordActivity() {
        gestureManager = GestureManager.createInstance(RecordActivity.this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordactivity);

        //senzory
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // poslouchac zmen senzoru
        fastestListener = new RecordListener(this, gestureManager);
        sensorManager.registerListener(fastestListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        //pipnuti
        sounds = new Sounds();

        // prevezmi pozici a najdi gesto
        position = (int) getIntent().getExtras().getInt("gesture");
        GestureManager manager = GestureManager.createInstance(RecordActivity.this);
        g = manager.getGestures().get(position);

        // text popisek
        textView = (TextView) findViewById(R.id.text_view);

        // nahravani
        btnRecord = (Button) findViewById(R.id.button2);
        // stav pro nahravani
        btnRecord.setTag(1);
        // kliknuti
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = v.getId();
                int status = (Integer) btnRecord.getTag();

                if (status == 1) {
                    btnRecord.setTag(2);

                    textView.setText("Make gesture! First beep - start recording. Second beep - stop recording.");
                    btnRecord.setText("Stop");

                    Log.d("RecordActivity", "Gesture: " + (g == null));

                    // vycisti a napl
                    g.cleanCoords();

                    // zpozdeni 2 sec pred samotnym nahravanim gesta
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            // ulozeni gesta
                            fastestListener.startRecording(g);
                        }
                    }, 1000);
                }
            }
        });


        // zrus, nenasels-li
        // TODO: pokud je to novy gesto???
        if (g == null) {
            finish();
        }
    }


    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(fastestListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(fastestListener);
    }
}