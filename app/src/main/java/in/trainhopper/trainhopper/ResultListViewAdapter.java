package in.trainhopper.trainhopper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class ResultListViewAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<ResultContainer> resultElements = new ArrayList<>();

    public ResultListViewAdapter(Activity context, ArrayList<ResultContainer> resultElements) {
        this.context = context;
        this.resultElements = resultElements;
    }

    @Override
    public int getCount() {
        return resultElements.size();
    }

    @Override
    public Object getItem(int i) {
        return resultElements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.result_card, null);
            TextView textView;
            if (resultElements.get(position).legs.size() == 1) {
                LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.poof);
                layout.setVisibility(View.GONE);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);
                imageView.setVisibility(View.GONE);

                textView = (TextView) convertView.findViewById(R.id.textView36);
                textView.setVisibility(View.GONE);
                textView = (TextView) convertView.findViewById(R.id.textView37);
                textView.setVisibility(View.GONE);

                textView = (TextView) convertView.findViewById(R.id.textView33);
                textView.setText("Day " + resultElements.get(position).legs.get(0).day_def);
            } else {
                textView = (TextView) convertView.findViewById(R.id.textView4);
                textView.setText(resultElements.get(position).legs.get(0).station_id_end);
                textView = (TextView) convertView.findViewById(R.id.textView24);
                textView.setText(resultElements.get(position).legs.get(0).station_name_end);

                textView = (TextView) convertView.findViewById(R.id.textView37);
                textView.setText("  " + (resultElements.get(position).layover / 3600 + ":" + (((resultElements.get(position).layover / 60 % 60) < 10) ? "0" : "") + resultElements.get(position).layover / 60 % 60 + " hrs"));

                textView = (TextView) convertView.findViewById(R.id.textView33);
                textView.setText("Day " + resultElements.get(position).legs.get(resultElements.get(position).legs.size() - 1).day_def);

            }
            textView = (TextView) convertView.findViewById(R.id.textView3);
            textView.setText(MainActivity.source);
            textView = (TextView) convertView.findViewById(R.id.textView5);
            textView.setText(MainActivity.destination);
            textView = (TextView) convertView.findViewById(R.id.textView22);
            textView.setText(MainActivity.sourceName);
            textView = (TextView) convertView.findViewById(R.id.textView25);
            textView.setText(MainActivity.destinationName);

            textView = (TextView) convertView.findViewById(R.id.textView32);
            textView.setText("Day 0");

            textView = (TextView) convertView.findViewById(R.id.textView7);
            textView.setText(resultElements.get(position).total_duration / 3600 + ":" + (((resultElements.get(position).total_duration / 60 % 60) < 10) ? "0" : "") + resultElements.get(position).total_duration / 60 % 60 + " hrs");

            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

            // Log.v(MainActivity.TAG,"result time"+ resultElements.get(position).legs.get(0).arrival_start/3600+":"+(((resultElements.get(position).legs.get(0).arrival_start/60%60)<10)?"0":"")+ resultElements.get(position).legs.get(0).arrival_start/60%60);
            Date dateObj = sdf.parse(resultElements.get(position).legs.get(0).arrival_start / 3600 + ":" + (((resultElements.get(position).legs.get(0).arrival_start / 60 % 60) < 10) ? "0" : "") + resultElements.get(position).legs.get(0).arrival_start / 60 % 60);
            textView = (TextView) convertView.findViewById(R.id.textView2);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            dateObj = sdf.parse(resultElements.get(position).legs.get(resultElements.get(position).legs.size() - 1).arrival_end / 3600 + ":" + (((resultElements.get(position).legs.get(resultElements.get(position).legs.size() - 1).arrival_end / 60 % 60) < 10) ? "0" : "") + resultElements.get(position).legs.get(resultElements.get(position).legs.size() - 1).arrival_end / 60 % 60);
            textView = (TextView) convertView.findViewById(R.id.textView8);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}