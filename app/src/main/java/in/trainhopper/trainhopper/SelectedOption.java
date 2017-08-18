package in.trainhopper.trainhopper;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SelectedOption extends Fragment {
    private View fview;
    static public int selectedOptionID;
    static public ResultContainer element;

    public SelectedOption() {
        // Required empty public constructor
    }

    String toCamelCase(String s) {
        String str = "";
        int a = 0;
        while (a < s.length()) {
            if (s.charAt(a) == ' ') {
                str = str.concat(String.valueOf(" " + s.charAt(a + 1)).toUpperCase());
                a++;
            } else if (a == 0)
                str = str.concat(String.valueOf(s.charAt(a)).toUpperCase());
            else
                str = str.concat(String.valueOf(s.charAt(a)).toLowerCase());
            a++;
        }
        return str;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_selected_option, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("via " + element.legs.get(0).station_name_end);

        TextView textView;
        RecyclerView recyclerView = fview.findViewById(R.id.recyclerView);
        SelectedOptionAdapter detailResultListViewAdapter = new SelectedOptionAdapter();
        //textView.setText("Route from " + MainActivity.sourceName + " to " + MainActivity.destinationName);
        if (element.legs.size() > 1) {
            element.legs.add(1, null);
            //Log.v(MainActivity.TAG, "bub" + JsonParser.resultContainerArrayList.get(selectedOptionID).legs.size() + "");

            //textView.setText(textView.getText() + " via " + element.legs.get(0).station_name_end);
        }
        TextView textView2 = fview.findViewById(R.id.textView21);
        textView = fview.findViewById(R.id.textView6);
        if (element.legs.size() == 1) {
            textView.setText("This route has no stops");
            LinearLayout linearLayout = fview.findViewById(R.id.linNo2);
            linearLayout.setVisibility(View.GONE);
        } else {
            textView.setText("This route has 1 stop");
            textView2.setText("Layover of " + element.layover / 3600
                    + ":" + (((element.layover / 60 % 60) < 10) ? "0" : "")
                    + element.layover / 60 % 60 + " HRS");

        }
        textView = fview.findViewById(R.id.textView24);
        textView.setText(element.total_duration / 3600
                + ":" + (((element.total_duration / 60 % 60) < 10) ? "0" : "")
                + element.total_duration / 60 % 60 + " HRS");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(detailResultListViewAdapter);
        detailResultListViewAdapter.notifyDataSetChanged();
        return fview;
    }
}
