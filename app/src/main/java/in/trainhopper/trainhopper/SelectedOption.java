package in.trainhopper.trainhopper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedOption extends Fragment {
    View fview;
    static public int selectedOptionID;
    static public ResultContainer element;

    public SelectedOption() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_selected_option, container, false);

        TextView textView = (TextView) fview.findViewById(R.id.textView9);
        textView.setText("Route from " + MainActivity.sourceName + " to " + MainActivity.destinationName);
        if (element.legs.size()>1) {
            textView.setText(textView.getText() + " via " + element.legs.get(0).station_id_end);
        }
        textView = (TextView) fview.findViewById(R.id.textView6);
        textView.setText("Total Duration : "+element.total_duration/3600+":"+(((element.total_duration/60%60)<10)?"0":"")+ element.total_duration/60%60+" hrs");

        SelectedOptionAdapter detailResultListViewAdapter = new SelectedOptionAdapter(getActivity());
        ListView listView = (ListView) fview.findViewById(R.id.listView2);
        listView.setAdapter(detailResultListViewAdapter);
        detailResultListViewAdapter.notifyDataSetChanged();
        return fview;
    }
}
