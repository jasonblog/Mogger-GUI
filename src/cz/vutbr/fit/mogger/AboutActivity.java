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
        String s = "<h1>Aplikace MOGGER</h1>";
        s += "<br /><br /> Aplikace <b>MOGER</b> byla vytvořena v rámci předmětu TAM na Fakultě Informačních technologií VUT v Brně, 2014";
        s += "<br /><br /> <b>Autoři:</b> Adámek Jakub, Dvořák Petr, Paulík Miroslav, Smetka Tomáš";
        tvAbout.setText(Html.fromHtml(s));
    }
}