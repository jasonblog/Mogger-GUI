package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import cz.vutbr.fit.mogger.R;
import cz.vutbr.fit.mogger.gesture.GestureManager;

import java.io.File;

public class SettingsDetailActivity extends Activity {
    FileDialog fileDialog = null;
    File mPath = null;

    EditText name = null;
    SeekBar threshold = null;
    ImageButton openFile = null;
    TextView fileName = null;
    ImageButton addGesture = null;
    TextView gestureOk = null;
    ImageButton save = null;
    ImageButton delete = null;

    // plna cesta
    String fullPath;

    // pozice gest v seznamu
    int position = 0;
    Gesture g = null;


    // flag na detekci umeleho ulozeni
    boolean manualSave = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsdetailactivity);

        mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".mp3");
        fileDialog.addFileListener(new cz.vutbr.fit.mogger.FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {

                fullPath = file.toString();
                fileName.setText(getFileNameOnly(fullPath));
                //Log.d(getClass().getName(), "selected file " + file.toString());
            }
        });

        name = (EditText) findViewById(R.id.editText);
        threshold = (SeekBar) findViewById(R.id.seekBar);
        openFile = (ImageButton) findViewById(R.id.imageButton);
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fileDialog
                fileDialog.showDialog();
            }
        });
        fileName = (TextView) findViewById(R.id.textView5);
        addGesture = (ImageButton) findViewById(R.id.imageButton2);
        gestureOk = (TextView) findViewById(R.id.textView7);
        save = (ImageButton) findViewById(R.id.imageButton5);
        delete = (ImageButton) findViewById(R.id.imageButton4);

        // najdi gesto z pozice v poli gest
        position = (int) getIntent().getExtras().getInt("gesture");

        if (position >= 0) {
            g = GestureManager.createInstance(getApplicationContext()).getGestures().get(position);
            if (g != null) {
                // vypis do GUI
                name.setText(g.name);
                fileName.setText(getFileNameOnly(g.fileSound));
                fullPath = g.fileSound;
                threshold.setMax((int) (g.getThreshold() + g.getThreshold() * 0.5)); // + 50%
                threshold.setProgress(g.getThreshold());
            }//if
        }//if

        // nelze mazat, pridavame-li nove gesto
        if (g == null) delete.setVisibility(View.INVISIBLE);

        // pridani gesta
        addGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // pokud je gesto nove - index je mimo
                // udelame prvni udelame umely save
                if (position == -1) {
                    manualSave = true; // jelikoz to neumime predat v evente...
                    save.performClick();
                    manualSave = false;
                    position = 0;
                }

                Log.d("SettingsDetailActivity", "Position: " + position);

                // zobrazeni detailu polozky
                Intent myIntent = new Intent(SettingsDetailActivity.this, RecordActivity.class);
                // data do aktivity
                myIntent.putExtra("gesture", position);
                startActivity(myIntent);
            }
        });

        // ulozeni gesta
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureManager manager = GestureManager.createInstance(getApplicationContext());

                if (g == null) {
                    g = new Gesture();
                }
                g.name = name.getText().toString();
                g.fileSound = fullPath;
                g.setThreshold(threshold.getProgress());

                manager.saveGesture(g);

                // zobrazeni textu uziv.
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();

                if (!manualSave) {
                    // ukonceni aktivity
                    finish();
                }
            }
        });


        // mazani gesta
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureManager manager = GestureManager.createInstance(getApplicationContext());

                if (g != null) {
                    manager.removeGesture(g);

                    // zobrazeni textu uziv.
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();

                    // ukonceni aktivity
                    finish();
                }//if null
            }

        });

    }

    /**
     * Vraci nazev souboru z plne cesty
     *
     * @param fullPath Cesta cesta souboru
     */
    private String getFileNameOnly(String fullPath) {

        String fileOnlyName = "";
        if (fullPath != null) {
            int index = fullPath.lastIndexOf('/');

            if (index > 0 && index < fullPath.length()) {
                fileOnlyName = fullPath.substring(index + 1, fullPath.length());
                fileName.setText(fileOnlyName);
            }
        }//if
        return fileOnlyName;
    }
}