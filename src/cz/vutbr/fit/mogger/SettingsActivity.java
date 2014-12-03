package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import cz.vutbr.fit.mogger.gesture.GestureManager;

public class SettingsActivity extends Activity {

    private GestureManager manager;

    ListView listView;
    GestugeArrayAdapter adapter = null;

    public SettingsActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);
        manager = GestureManager.createInstance(getApplicationContext());

        Log.d("settings", "Settings activity starting");


        // testovaci data
        listView = (ListView) findViewById(R.id.list);
        // reflesh seznamu gest
        RefleshListItem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Log.d("settings", "Settings activity click list");
                Gesture g = (Gesture) listView.getAdapter().getItem(position);
                String itemValue = g.name;
                Toast.makeText(getApplicationContext(),
                        "Position: " + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

                // zobrazeni detailu polozky
                Intent myIntent = new Intent(SettingsActivity.this, SettingsDetailActivity.class);
                // data do aktivity
                myIntent.putExtra("gesture", position);
                startActivity(myIntent);

            }
        });
    }

    public void RefleshListItem()
    {
        adapter = new GestugeArrayAdapter(this, manager.getGestures());
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
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
        if (item.getItemId() == R.id.settings_create) {
            // settingsdetail
            Intent myIntent = new Intent(SettingsActivity.this, cz.vutbr.fit.mogger.SettingsDetailActivity.class);
            myIntent.putExtra("gesture", -1);
            this.startActivity(myIntent);

            // reflesh seznamu gest
            RefleshListItem();
        }

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onResume()
    {
        super.onResume();
        // reflesh seznamu gest
        RefleshListItem();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.mPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.mPlayer.stop();
            adapter = null;
        }
    }
}