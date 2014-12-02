package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
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
                fileName.setText(file.toString());
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
        //Log.d(getClass().getName(), "position: " + position);
        if (position >= 0) {
            g = GestureManager.createInstance(SettingsDetailActivity.this).getGestures().get(position);
            if (g != null) {
                // vypis do GUI
                name.setText(g.name);
                fileName.setText(g.fileSound);
                threshold.setMax((int) (g.getThreshold() + g.getThreshold() * 0.5)); // + 50%
                threshold.setProgress(g.getThreshold());
            }//if
        }//if

        // pridani gesta
        addGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // zobrazeni detailu polozky
                Intent myIntent = new Intent(SettingsDetailActivity.this, RecordActivity.class);
                // data do aktivity
                myIntent.putExtra("gesture", position);
                startActivity(myIntent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureManager manager = GestureManager.createInstance(SettingsDetailActivity.this);

                if (g == null) {
                    g = new Gesture();
                }
                g.name = name.getText().toString();
                g.fileSound = fileName.getText().toString();
                g.setThreshold(threshold.getProgress());

                manager.saveGesture(g);

                // zobrazeni textu uziv.
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();

                // ukonceni aktivity
                finish();
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
        int index = fullPath.lastIndexOf('/');
        if (index > 0 && index < fullPath.length()) {
            fileOnlyName = fullPath.substring(index + 1, fullPath.length());
            fileName.setText(fileOnlyName);
        }

        return fileOnlyName;
    }
}