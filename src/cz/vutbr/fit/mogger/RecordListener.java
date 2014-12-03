package cz.vutbr.fit.mogger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import cz.vutbr.fit.mogger.gesture.GestureManager;

import static cz.vutbr.fit.mogger.Constants.*;
import static cz.vutbr.fit.mogger.Constants.CHECK_GESTURE_STOP;
import static java.lang.Math.abs;

/**
 * Created by murry on 2.12.14.
 */
public class RecordListener implements SensorEventListener {



    // zvuk
    Sounds sounds;
    private boolean isActive = false;
    private long lastUpdate = 0;
    private RecordActivity mogger;
    private GestureManager gestureManager;
    private int check_gesture;
    private int prev_x, prev_y, prev_z;

    private Gesture currentGesture;

    public RecordListener(RecordActivity mogger, GestureManager gestureManager) {

        this.mogger = mogger;
        this.gestureManager = gestureManager;
        sounds = new Sounds();

        check_gesture = CHECK_GESTURE_STOP;
        prev_x = 100;
    }

    public void startRecording(Gesture _currentGesture) {
        isActive = true;
        this.currentGesture = _currentGesture;

        if (currentGesture == null) {
            currentGesture = new Gesture("new", "", 0); // to avoid overriding existing gesture
        }
    }

    public void stopRecording() {
        isActive = false;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (isActive) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];

            long curTime = System.currentTimeMillis();

            // vzorkovani dat
            if ((curTime - lastUpdate) >= SAMPLING_RATE) {
                lastUpdate = curTime;

                // ulozeni gesta
                save(x, y, z);

                prev_x = x;
                prev_y = y;
                prev_z = z;
            }
        }
    }


    // ulozeni noveho gesta
    private void save(int x, int y, int z) {

        if (currentGesture.size() == 0) {
            // prvni inicializace
            if (prev_x != 100) {
                // uzivatel zacal vytvaret gesto
                if (calculateDiff(x, y, z) > 1) {
                    currentGesture.addCoords(x, y, z);
                    onGestureStart();
                }
            }
        } else {
            currentGesture.addCoords(x, y, z);

            // mame alespon 8 vektoru, zacneme overovat, zda uzivatel neukoncil gesto
            if (currentGesture.size() >= 8) {
                if (calculateDiff(x, y, z) < 3) {
                    onGestureEnd();
                }
            }
        }
    }

    protected int calculateDiff(int x, int y, int z) {
        return abs(x - prev_x) + abs(y - prev_y) + abs(z - prev_z);
    }

    private void onGestureStart() {
        mogger.textView.setText("Recording ...");
        sounds.PlayTone();
    }


    private void onGestureEnd() {
        mogger.fastestListener.stopRecording();
        mogger.btnRecord.setText("Save");
        mogger.btnRecord.setTag(1);
        mogger.textView.setText("New gesture saved.");
        sounds.PlayTone();

        gestureManager.saveGesture(currentGesture);
    }
}


