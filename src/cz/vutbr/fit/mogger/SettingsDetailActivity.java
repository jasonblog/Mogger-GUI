package cz.vutbr.fit.mogger;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import cz.vutbr.fit.mogger.R;

import java.io.File;

/**
 * Created by murry on 29.11.14.
 */
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

    String filePath = null;
    String fileOnlyName = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsdetailactivity);

        mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".mp3");
        fileDialog.addFileListener(new cz.vutbr.fit.mogger.FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                filePath = file.toString();
                int index = filePath.lastIndexOf('\\');
                if(index > 0 && index < filePath.length())
                {
                    fileOnlyName = filePath.substring(index, filePath.length());
                    fileName.setText(fileOnlyName);
                }
                Log.d(getClass().getName(), "selected file " + file.toString());
            }
        });

        name = (EditText)findViewById(R.id.editText);
        threshold = (SeekBar)findViewById(R.id.seekBar);
        openFile = (ImageButton)findViewById(R.id.imageButton);
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fileDialog
                fileDialog.showDialog();
            }
        });
        fileName = (TextView)findViewById(R.id.textView5);
        addGesture = (ImageButton)findViewById(R.id.imageButton2);
        gestureOk = (TextView)findViewById(R.id.textView7);
        save = (ImageButton)findViewById(R.id.imageButton5);
        delete = (ImageButton)findViewById(R.id.imageButton4);
    }
}