package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cz.vutbr.fit.mogger.R;

import java.util.List;

/**
 * Created by murry on 29.11.14.
 */
public class SettingsActivity extends ListActivity {

    FileStorage storage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);


        // kliknuti na tlacitko
        /*ImageView imageView = (ImageView) findViewById(R.id.imgNote);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */

        // testovaci data
        storage = new FileStorage(getApplicationContext());
        Gesture[] test = new Gesture[10];
        Gesture g = new Gesture(storage); g.name = "test 1"; g.fileSound = "test-sound.mp3";
        test[0] = g;
        test[1] = g;
        test[2] = g;
        test[3] = g;
        g.name = "test 2";
        test[4] = g;
        test[5] = g;
        test[6] = g;
        test[7] = g;
        test[8] = g;
        test[9] = g;

        setListAdapter(new GestugeArrayAdapter(this, test));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        //ListView lv = (ListView)findViewById(R.id.lstGestuge);
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }



    /*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Settings");
        menu.add(0, 1, 1, "About");

        return super.onPrepareOptionsMenu(menu);

    }
    */

}