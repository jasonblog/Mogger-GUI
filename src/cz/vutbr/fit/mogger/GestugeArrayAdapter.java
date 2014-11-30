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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_gesture, parent, false);
        final TextView desc = (TextView) rowView.findViewById(R.id.lblDescrition);
        TextView title = (TextView) rowView.findViewById(R.id.edtTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgNote);
        final Gesture gesture = values.get(position);

        //kliknuti na notu - prehrani zvuku
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filePath = gesture.fileSound;
                Log.d("adapter", filePath);

                File path = Environment.getExternalStoragePublicDirectory("/");
                File file = new File(filePath);

                try {
                    mPlayer = new MediaPlayer();

                    mPlayer.setDataSource(filePath);
                    mPlayer.prepare();
                    mPlayer.start();

                    Toast.makeText(v.getContext(), "Playing song: " + desc.getText(), Toast.LENGTH_LONG).show();

                }
                catch (Exception e)
                {
                    Log.d("adapter", e.toString());
                    Log.d("adapter", filePath);

                    Toast.makeText(v.getContext(), "Playing song: failed", Toast.LENGTH_LONG).show();

                }
            }
        });

        // nastaveni popisku, obrazku
        desc.setText(gesture.fileSound);
        title.setText(gesture.name);
        imageView.setImageResource(R.drawable.note);

        return rowView;
    }
}
