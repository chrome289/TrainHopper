package in.trainhopper.trainhopper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Siddharth on 23-Jul-16.
 */
public class ResultListViewAdapter extends BaseAdapter {

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
            /*Element element = resultElements.get(position);
            Elements elements1 = element.select("span.train");
            Elements elements2 = element.select("span.station");
            Elements elements3 = element.select("span.station-time");
            Elements elements4 = element.select("span.train-name");
            Elements elements5 = element.select("div.right");
            Elements elements6 = element.select("span.station-name");
            Elements elements7 = element.select("span.station-day");
            Elements elements8= element.select("span.wait-time");*/
            TextView textView;
            if (resultElements.get(position).legs.size() == 1) {
                textView = (TextView) convertView.findViewById(R.id.textView4);
                textView.setVisibility(View.GONE);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);
                imageView.setVisibility(View.GONE);
                textView = (TextView) convertView.findViewById(R.id.textView24);
                textView.setText("");

                textView = (TextView) convertView.findViewById(R.id.textView33);
                textView.setText("Day " + resultElements.get(position).legs.get(0).day_def);
            } else {
                textView = (TextView) convertView.findViewById(R.id.textView4);
                textView.setText(resultElements.get(position).legs.get(0).station_id_end);
                textView = (TextView) convertView.findViewById(R.id.textView24);
                textView.setText(resultElements.get(position).legs.get(0).station_name_end);

                textView = (TextView) convertView.findViewById(R.id.textView33);
                textView.setText("Day " + resultElements.get(position).legs.get(1).day_def);

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

            textView = (TextView) convertView.findViewById(R.id.textView34);
            textView.setText(resultElements.get(position).wait_time / 3600 + ":" + (((resultElements.get(position).wait_time / 60 % 60) < 10) ? "0" : "") + resultElements.get(position).wait_time / 60 % 60 + " hrs");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}