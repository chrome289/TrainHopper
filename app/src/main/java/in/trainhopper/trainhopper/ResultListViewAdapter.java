package in.trainhopper.trainhopper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Siddharth on 23-Jul-16.
 */
public class ResultListViewAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<Element> resultElements = new ArrayList<>();

    public ResultListViewAdapter(Activity context, ArrayList<Element> resultElements) {
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
            Element element = resultElements.get(position);
            Elements elements1 = element.select("span.train");
            Elements elements2 = element.select("span.station");
            Elements elements3 = element.select("span.station-time");
            Elements elements4 = element.select("span.train-name");
            Elements elements5 = element.select("div.right");
            Elements elements6 = element.select("span.station-name");
            Elements elements7 = element.select("span.station-day");
            TextView textView;
            if (elements2.size() == 2) {
                textView = (TextView) convertView.findViewById(R.id.textView3);
                textView.setText(elements2.get(0).ownText());
                textView = (TextView) convertView.findViewById(R.id.textView4);
                textView.setVisibility(View.GONE);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);
                imageView.setVisibility(View.GONE);
                textView = (TextView) convertView.findViewById(R.id.textView5);
                textView.setText(elements2.get(1).ownText());

                textView = (TextView) convertView.findViewById(R.id.textView24);
                textView.setText("");
            } else {
                textView = (TextView) convertView.findViewById(R.id.textView3);
                textView.setText(elements2.get(0).ownText());
                textView = (TextView) convertView.findViewById(R.id.textView4);
                textView.setText(elements2.get(1).ownText());
                textView = (TextView) convertView.findViewById(R.id.textView5);
                textView.setText(elements2.get(2).ownText());

                textView = (TextView) convertView.findViewById(R.id.textView24);
                textView.setText(elements6.get(1).ownText());

            }
            textView = (TextView) convertView.findViewById(R.id.textView22);
            textView.setText(MainActivity.sourceName);
            textView = (TextView) convertView.findViewById(R.id.textView25);
            textView.setText(MainActivity.destinationName);

            textView = (TextView) convertView.findViewById(R.id.textView32);
            textView.setText("Day " + elements7.get(0).ownText());
            textView = (TextView) convertView.findViewById(R.id.textView33);
            textView.setText("Day " + elements7.get(elements7.size() - 1).ownText());

            textView = (TextView) convertView.findViewById(R.id.textView7);
            textView.setText(elements5.get(0).ownText() + " hrs");

            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

            Date dateObj = sdf.parse(elements3.get(1).ownText());
            textView = (TextView) convertView.findViewById(R.id.textView2);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            dateObj = sdf.parse(elements3.get(elements3.size() - 1).ownText());
            textView = (TextView) convertView.findViewById(R.id.textView8);
            textView.setText(new SimpleDateFormat("h:mm a").format(dateObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}