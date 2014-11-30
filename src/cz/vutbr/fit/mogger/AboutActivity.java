package cz.vutbr.fit.mogger;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import cz.vutbr.fit.mogger.R;

/**
 * Created by murry on 29.11.14.
 */
public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutactivity);

        TextView tvAbout = (TextView)findViewById(R.id.aboutText);
        String s = getString(R.string.about);
        tvAbout.setText(Html.fromHtml(s));
    }
}