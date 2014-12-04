package cz.vutbr.fit.mogger.gesture;

import android.content.Context;
import android.content.res.Resources;
import cz.vutbr.fit.mogger.DTW;
import cz.vutbr.fit.mogger.FileStorage;
import cz.vutbr.fit.mogger.Gesture;

import java.util.ArrayList;

public class GestureManager {
    private final FileStorage storage;
    protected ArrayList<Gesture> gestures;
    DTW dtw;

    // instance managera - singleton
    private static GestureManager instance = null;

    private GestureManager(Context context) {
        storage = new FileStorage(context);
        gestures = storage.loadConfig();
        dtw = new DTW();
    }

    public static GestureManager createInstance(Context context) {
        if (instance == null) instance = new GestureManager(context);
        return instance;
    }

    public Gesture getGesture(String name) throws Resources.NotFoundException {
        for (Gesture gesture : gestures) {
            if (gesture.name.equals(name)) {
                return gesture;
            }
        }
        throw new Resources.NotFoundException("Gesture '" + name + "' not found");
    }

    public Gesture getGesture(Gesture g) throws Resources.NotFoundException {
        if (!gestures.contains(g)) {
            throw new Resources.NotFoundException("Gesture '" + g.name + "' not found");
        }
        return g;
    }

    public void saveGesture(Gesture gesture) {
        try {
            gesture = getGesture(gesture);
        } catch (Resources.NotFoundException e) {
//            for (Gesture storedGesture : gestures) {
//                if (storedGesture.name.equals(gesture.name)) {
//                    throw new InvalidArgumentException("Gesture with name '" + gesture.name + "' already exists.");
//                }
//            }
            gestures.add(gesture);
        }
        storage.storeGestures(gestures);
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
        if (testedGesture.size() >= storedGesture.size() && storedGesture.size() > 0 && testedGesture.size() > 0) {
            // vypocet DTW
            int result = dtw.dtw_check(storedGesture, testedGesture);

            // gesto zachyceno, promazeme zachycene gesto (kvuli mnohonasobnemu zachyceni s dalsimi vzorky)
            if (result < storedGesture.getThreshold()) {
                testedGesture.cleanCoords();
                return true;
            }
        }

        return false;
    }

    /**
     * Vraci index gesta, ktere se stejne gesto jako v parametru, -1 pri nenalezeni
     * @param gesture
     * @return
     */
    public int checkGesture(Gesture gesture) {
        for (int i = 0; i < gestures.size(); i++) {
            if (compareGestures(gesture, gestures.get(i))) {
                return i;
            }
        }//for

        return -1;
    }

    /**
     * Test na prazdnost gest
     */
    public boolean isEmpty() {
        return gestures.isEmpty();
    }

    private class InvalidArgumentException extends Throwable {
        public InvalidArgumentException(String s) {
        }
    }
}
