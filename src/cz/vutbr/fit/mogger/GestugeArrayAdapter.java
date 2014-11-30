package cz.vutbr.fit.mogger;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by murry on 29.11.14.
 */
public class GestugeArrayAdapter extends ArrayAdapter<Gesture> {
    private final Context context;
    private final Gesture[] values;

    public GestugeArrayAdapter(Context context, Gesture[] values) {
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
            }
        });

        // nastaveni popisku, obrazku
        descrition.setText(values[position].fileSound);
        title.setText(values[position].name);
        imageView.setImageResource(R.drawable.note);

        return rowView;
    }
}
