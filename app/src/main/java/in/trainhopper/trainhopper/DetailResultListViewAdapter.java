package in.trainhopper.trainhopper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Siddharth on 25-Jul-16.
 */
public class DetailResultListViewAdapter extends BaseAdapter {
    private Activity context;
    ArrayList<DetailedResultContainer> containers = new ArrayList<>();

    public DetailResultListViewAdapter(Activity context, ArrayList<DetailedResultContainer> containers) {
        this.context = context;
        this.containers = containers;
    }

    @Override
    public int getCount() {
        return containers.size();
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
            textView.setText(containers.get(position).station1);
            textView = (TextView) convertView.findViewById(R.id.textView12);
            textView.setText(containers.get(position).station2);

            textView = (TextView) convertView.findViewById(R.id.textView13);
            textView.setText(containers.get(position).trainName);
            textView = (TextView) convertView.findViewById(R.id.textView14);
            textView.setText(containers.get(position).trainNo);
            textView = (TextView) convertView.findViewById(R.id.textView29);
            textView.setText(containers.get(position).trainClass);

            textView = (TextView) convertView.findViewById(R.id.textView26);
            textView.setText(containers.get(position).stationNameStart);
            textView = (TextView) convertView.findViewById(R.id.textView27);
            textView.setText(containers.get(position).stationNameEnd);


            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

            textView = (TextView) convertView.findViewById(R.id.textView15);
            textView.setText(containers.get(position).station1);

            Date dateObj = sdf.parse(containers.get(position).arrival1);
            textView = (TextView) convertView.findViewById(R.id.textView16);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            dateObj = sdf.parse(containers.get(position).departure1);
            textView = (TextView) convertView.findViewById(R.id.textView17);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            textView = (TextView) convertView.findViewById(R.id.textView18);
            textView.setText(containers.get(position).station2);

            dateObj = sdf.parse(containers.get(position).arrival2);
            textView = (TextView) convertView.findViewById(R.id.textView19);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            dateObj = sdf.parse(containers.get(position).departure2);
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

