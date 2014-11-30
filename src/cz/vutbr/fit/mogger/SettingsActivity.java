package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import cz.vutbr.fit.mogger.R;
import cz.vutbr.fit.mogger.SettingsDetailActivity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by murry on 29.11.14.
 */
public class SettingsActivity extends Activity {

    ListView listView;
    GestugeArrayAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);

        Log.d("settings", "Settings activity starting");

        // testovaci data
        Gesture[] test = new Gesture[10];
        Gesture g = new Gesture();
        g.name = "test 1";
        g.fileSound = "test-sound.mp3";

        test[0] = g;
        test[1] = g;
        test[2] = g;
        test[3] = g;
        test[4] = g;
        test[5] = g;
        test[6] = g;
        test[7] = g;
        test[8] = g;
        test[9] = g;



        listView = (ListView)findViewById(R.id.list);
        adapter = new GestugeArrayAdapter(this, test);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Log.d("settings", "Settings activity click list");
                Gesture g = (Gesture)listView.getAdapter().getItem(position);
                String  itemValue = g.name;
                Toast.makeText(getApplicationContext(),
                        "Position: " + position + "  ListItem : " + itemValue , Toast.LENGTH_LONG)
                        .show();

                // zobrazeni detailu polozky
                Intent myIntent = new Intent(SettingsActivity.this, SettingsDetailActivity.class);
                // TODO: data to detailu
                //Bundle bundle = new Bundle();
                //bundle.putSerializable("g", g);
                //myIntent.putExtra(bundle);
                myIntent.putExtra("gestuge", position);
                startActivity(myIntent);

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(item.getItemId() == R.id.settings_create)
        {
            // settingsdetail
            Intent myIntent = new Intent(SettingsActivity.this, cz.vutbr.fit.mogger.SettingsDetailActivity.class);
            myIntent.putExtra("GestureObject", "ahoj");
            this.startActivity(myIntent);
        }

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop ()
    {
        if (adapter != null)
        {
            adapter.mPlayer.pause();
        }
    }

    @Override
    protected  void onDestroy()
    {
        if (adapter!=null)
        {
            adapter.mPlayer.stop();
            adapter = null;
        }
    }
}