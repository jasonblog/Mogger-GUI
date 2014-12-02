package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cz.vutbr.fit.mogger.R;
import cz.vutbr.fit.mogger.gesture.GestureManager;

/**
 * Created by murry on 2.12.14.
 */
public class RecordActivity extends Activity {


    // pozice gest v seznamu
    int position = 0;
    Gesture g = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordactivity);

        position = (int) getIntent().getExtras().getInt("gesture");
        GestureManager manager = GestureManager.createInstance(RecordActivity.this);
        g = manager.getGestures().get(position);

        // zrus, nenasels-li
        // TODO: pokud je to novy gesto???
        if (g == null) {
            finish();
        }

        // vycisti a napl
        g.cleanCoords();
        //g.addCoords();
    }
}