package cz.vutbr.fit.mogger;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by murry on 29.11.14.
 */
public class GestugeArrayAdapter extends ArrayAdapter<Gesture> {
    private final Context context;
    private ArrayList<Gesture> values;

    // prehravani media
    public MediaPlayer mPlayer = new MediaPlayer();

    public GestugeArrayAdapter(Context context, ArrayList<Gesture> values) {
        super(context, R.layout.list_gesture, values);
        this.context = context;
        this.values = values;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_gesture, parent, false);
        final TextView descrition = (TextView) rowView.findViewById(R.id.lblDescrition);
        TextView title = (TextView) rowView.findViewById(R.id.edtTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgNote);

        //kliknuti na notu - prehrani zvuku
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Playing song: " + descrition.getText(), Toast.LENGTH_LONG).show();
                String ResourceID = "aa";

                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC);
                path = Environment.getExternalStoragePublicDirectory("/");
                File file = new File(path, ResourceID+".mp3");

                try {
                    mPlayer.setDataSource(path + "/" + ResourceID + ".mp3");
                    mPlayer.prepare();
                    mPlayer.start();

                }
                catch (Exception e)
                {
                    Log.d("adapter", e.toString());
                }
            }
        });

        // nastaveni popisku, obrazku
        descrition.setText(values.get(position).fileSound);
        title.setText(values.get(position).name);
        imageView.setImageResource(R.drawable.note);

        return rowView;
    }
}
