package in.trainhopper.trainhopper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Siddharth on 25-Jul-16.
 */
public class SelectedOptionAdapter extends BaseAdapter {
    private Activity context;

    public SelectedOptionAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return SelectedOption.element.legs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.result_details_card, null);

            TextView textView = (TextView) convertView.findViewById(R.id.textView11);
            textView.setText(SelectedOption.element.legs.get(position).station_id_start);
            textView = (TextView) convertView.findViewById(R.id.textView12);
            textView.setText(SelectedOption.element.legs.get(position).station_id_end);

            textView = (TextView) convertView.findViewById(R.id.textView13);
            textView.setText(SelectedOption.element.legs.get(position).train_name);
            textView = (TextView) convertView.findViewById(R.id.textView14);
            Log.v(MainActivity.TAG, position + "::::" + SelectedOption.element.legs.get(position).train_id);
            textView.setText(SelectedOption.element.legs.get(position).train_id + "");
            textView = (TextView) convertView.findViewById(R.id.textView29);
            textView.setText(SelectedOption.element.legs.get(position).train_class);

            textView = (TextView) convertView.findViewById(R.id.textView26);
            textView.setText(SelectedOption.element.legs.get(position).station_name_start);
            textView = (TextView) convertView.findViewById(R.id.textView27);
            textView.setText(SelectedOption.element.legs.get(position).station_name_end);


            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

            textView = (TextView) convertView.findViewById(R.id.textView15);
            textView.setText(SelectedOption.element.legs.get(position).station_name_start);

            Date dateObj = sdf.parse(SelectedOption.element.legs.get(position).arrival_start / 3600 + ":" + (((SelectedOption.element.legs.get(position).arrival_start / 60 % 60) < 10) ? "0" : "") + SelectedOption.element.legs.get(position).arrival_start / 60 % 60);
            textView = (TextView) convertView.findViewById(R.id.textView16);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            dateObj = sdf.parse(SelectedOption.element.legs.get(position).departure_start / 3600 + ":" + (((SelectedOption.element.legs.get(position).departure_start / 60 % 60) < 10) ? "0" : "") + SelectedOption.element.legs.get(position).departure_start / 60 % 60);
            textView = (TextView) convertView.findViewById(R.id.textView17);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            textView = (TextView) convertView.findViewById(R.id.textView18);
            textView.setText(SelectedOption.element.legs.get(position).station_name_end);

            dateObj = sdf.parse(SelectedOption.element.legs.get(position).arrival_end / 3600 + ":" + (((SelectedOption.element.legs.get(position).arrival_end / 60 % 60) < 10) ? "0" : "") + SelectedOption.element.legs.get(position).arrival_end / 60 % 60);
            textView = (TextView) convertView.findViewById(R.id.textView19);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            dateObj = sdf.parse(SelectedOption.element.legs.get(position).departure_end / 3600 + ":" + (((SelectedOption.element.legs.get(position).departure_end / 60 % 60) < 10) ? "0" : "") + SelectedOption.element.legs.get(position).departure_end / 60 % 60);
            textView = (TextView) convertView.findViewById(R.id.textView20);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            textView = (TextView) convertView.findViewById(R.id.textView10);
            textView.setText("Stop No. " + (position + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
