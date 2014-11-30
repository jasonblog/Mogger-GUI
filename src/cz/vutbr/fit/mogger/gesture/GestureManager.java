package cz.vutbr.fit.mogger.gesture;

import android.content.Context;
import cz.vutbr.fit.mogger.DTW;
import cz.vutbr.fit.mogger.FileStorage;
import cz.vutbr.fit.mogger.Gesture;

import java.util.ArrayList;

public class GestureManager {
    private final FileStorage storage;
    protected ArrayList<Gesture> gestures;
    DTW dtw;

    public GestureManager(Context context) {
        storage = new FileStorage(context);
        gestures = storage.loadConfig();
        dtw = new DTW();
    }

    public void addGesture(Gesture gesture) {
//        for (Gesture storedGesture : gestures) {
//            if (storedGesture.name.equals(gesture.name)) {
//                throw new InvalidArgumentException("Gesture with name '" + gesture.name + "' already exists.");
//            }
//        }
        gestures.add(gesture);
    }

    public void removeGesture(String name) {
        for (Gesture gesture : gestures) {
            if (gesture.name.equals(name)) {
                removeGesture(gesture);
            }
        }
    }

    public void removeGesture(Gesture gesture) {
        gestures.remove(gesture);
    }

    public ArrayList<Gesture> getGestures() {
        return gestures;
    }

    private boolean compareGestures(Gesture testedGesture, Gesture storedGesture) {
        // mame v bufferu vice nebo stejne vektoru jako ma gesto? (= muzeme zkontrolovat celou sekvenci?)
        if (testedGesture.size() >= storedGesture.size()) {
            // vypocet DTW
            int result = dtw.dtw_check(testedGesture.getCoordsArray(), storedGesture.getCoordsArray());

            // gesto zachyceno, promazeme zachycene gesto (kvuli mnohonasobnemu zachyceni s dalsimi vzorky)
            if (result < storedGesture.getThreshold()) {
                testedGesture.cleanCoords();
                return true;
            }
        }

        return false;
    }

    public boolean checkGesture(Gesture gesture) {
        for (Gesture storedGesture : gestures) {
            if (compareGestures(gesture, storedGesture)) {
                return true;
            }
        }

        return false;
    }

    private class InvalidArgumentException extends Throwable {
        public InvalidArgumentException(String s) {
        }
    }


    /**
     * Test na prazdnost gest
     * @return
     */
    public boolean isEmpty() {
        return gestures.isEmpty();

    }
}
