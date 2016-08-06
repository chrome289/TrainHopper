package in.trainhopper.trainhopper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedOption extends Fragment {
    View fview;
    static public int selectedOptionID;
    static public Element element;

    public SelectedOption() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_selected_option, container, false);

        ArrayList<DetailedResultContainer> containers = new ArrayList<>();
        Elements elements1 = element.select("span.train");
        Elements elements2 = element.select("span.station");
        Elements elements3 = element.select("span.station-time");
        Elements elements4 = element.select("span.train-name");
        Elements elements5 = element.select("div.right");
        Elements elements6 = element.select("span.station-name");
        Elements elements7 = element.select("span.train-class");
        containers.clear();
        for (int x = 0; x < elements4.size(); x++) {
            DetailedResultContainer container1 = new DetailedResultContainer();
            container1.trainName = elements4.get(x).ownText();
            container1.trainNo = elements1.get(x).ownText();
            container1.trainClass = elements7.get(x).ownText().replace("'", "").replace("[", "").replace("]", "");
            container1.arrival1 = elements3.get((4 * x)).ownText();
            container1.departure1 = elements3.get((4 * x) + 1).ownText();
            container1.arrival2 = elements3.get((4 * x) + 2).ownText();
            container1.departure2 = elements3.get((4 * x) + 3).ownText();
            container1.station1 = elements2.get(x).ownText();
            container1.station2 = elements2.get(x + 1).ownText();
            container1.stationNameStart = elements6.get(x).ownText();
            container1.stationNameEnd = elements6.get(x + 1).ownText();
            containers.add(container1);
        }

        TextView textView = (TextView) fview.findViewById(R.id.textView9);
        textView.setText("Route from " + MainActivity.source + " to " + MainActivity.destination);
        if (elements4.size() > 1)
            textView.setText(textView.getText() + " via " + elements2.get(1).ownText());
        textView = (TextView) fview.findViewById(R.id.textView6);
        textView.setText("Duration " + elements5.get(0).ownText() + " Hrs");

        DetailResultListViewAdapter detailResultListViewAdapter = new DetailResultListViewAdapter(getActivity(), containers);
        ListView listView = (ListView) fview.findViewById(R.id.listView2);
        listView.setAdapter(detailResultListViewAdapter);
        detailResultListViewAdapter.notifyDataSetChanged();
        return fview;
    }
}
