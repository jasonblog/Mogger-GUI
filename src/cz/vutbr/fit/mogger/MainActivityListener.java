package cz.vutbr.fit.mogger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.util.Log;
import cz.vutbr.fit.mogger.gesture.GestureManager;

import java.io.IOException;

import static cz.vutbr.fit.mogger.Constants.*;
import static cz.vutbr.fit.mogger.Constants.CHECK_GESTURE_START;
import static java.lang.Math.abs;

/**
 * Created by murry on 2.12.14.
 */
public class MainActivityListener implements SensorEventListener {

    // zvuk
    Sounds sounds;

    private boolean isActive = false;
    private long lastUpdate = 0;
    private MainActivity mogger;
    private GestureManager gestureManager;
    //stav posuzovaneho gesta
    private int check_gesture;
    private int prev_x, prev_y, prev_z;

    // prehravac
    MediaPlayer mPlayer;

    //aktualni gesto
    private Gesture currentGesture;

    public MainActivityListener(MainActivity mogger, GestureManager gestureManager) {

        this.mogger = mogger;
        this.gestureManager = gestureManager;
        sounds = new Sounds();

        currentGesture = new Gesture("new", "", 0); // to avoid overriding existing gesture

        check_gesture = CHECK_GESTURE_STOP;
        prev_x = 100;
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

                // porovnani gest

                // kontrola, zda se provadi gesto
                // prvni inicializace
                if (prev_x != 100) {
                    // stav kontrola neaktivni
                    if (check_gesture == CHECK_GESTURE_START) {
                        if (calculateDiff(x, y, z) < 2) {
                            check_gesture = CHECK_GESTURE_STOP;
                        }
                    } else if (check_gesture == CHECK_GESTURE_STOP) {
                        if (calculateDiff(x, y, z) > 2) {
                            // prepneme stav
                            check_gesture = CHECK_GESTURE_START;
                            // vyresetujeme zaznamenane udaje
                            currentGesture.cleanCoords();
                        }
                    }
                }
                // gesto se provadi
                if (check_gesture == CHECK_GESTURE_START) {
                    currentGesture.addCoords(x, y, z);

                    // pro vsechny gesta - zkontroluj!
                    int gestureIndex = gestureManager.checkGesture(currentGesture);
                    if (gestureIndex >= 0) {
                        onGestureDetected(gestureIndex);
                    }
                }


                prev_x = x;
                prev_y = y;
                prev_z = z;
            } // if vzorkovani dat
        }//if active

    }

    protected int calculateDiff(int x, int y, int z) {
        return abs(x - prev_x) + abs(y - prev_y) + abs(z - prev_z);
    }

    /**
     * Detekovano gesto!
     */
    private void onGestureDetected(int gestureIndex) {
        currentGesture.cleanCoords();

        // prehrej, zatim jen ton
        //TODO: zde bude prehravani mp3
        sounds.PlayTone();

        try {

            // stop predchoziho
            if (mPlayer != null && mPlayer.isPlaying()) mPlayer.stop();
            // nova instance a prehravej!
            mPlayer = new MediaPlayer();
            String filePath = gestureManager.getGestures().get(gestureIndex).fileSound;
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    public void startRecording() {
        isActive = true;
    }
    public void stopRecording() {
        isActive = false;
        // stop hudby, je-li
        if (mPlayer != null) mPlayer.stop();

    }
}
