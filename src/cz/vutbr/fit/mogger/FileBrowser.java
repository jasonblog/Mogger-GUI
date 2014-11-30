package cz.vutbr.fit.mogger;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import cz.vutbr.fit.mogger.R;
import java.io.File;
/**
 * Created by Irteam on 30. 11. 2014.
 */

//test2
public class FileBrowser extends Activity {
    cz.vutbr.fit.mogger.FileDialog fileDialog = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_browser);
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new cz.vutbr.fit.mogger.FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".mp3");
        fileDialog.addFileListener(new cz.vutbr.fit.mogger.FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(getClass().getName(), "selected file " + file.toString());
            }
        });
//fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
// public void directorySelected(File directory) {
// Log.d(getClass().getName(), "selected dir " + directory.toString());
// }
//});
//fileDialog.setSelectDirectoryOption(false);
        fileDialog.showDialog();
    }
}