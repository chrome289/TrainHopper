package in.trainhopper.trainhopper;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SelectedOption extends Fragment {
    private View fview;
    static public int selectedOptionID;
    static public ResultContainer element;

    public SelectedOption() {
        // Required empty public constructor
    }
    String toCamelCase(String s){
        String str="";
        int a=0;
        while(a<s.length()){
            if(s.charAt(a)==' ') {
                str=str.concat(String.valueOf(" "+s.charAt(a + 1)).toUpperCase());
                a++;
            }else if(a==0)
                str=str.concat(String.valueOf(s.charAt(a)).toUpperCase());
            else
                str=str.concat(String.valueOf(s.charAt(a)).toLowerCase());
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

        TextView textView;
        RecyclerView recyclerView=(RecyclerView)fview.findViewById(R.id.recyclerView);
        SelectedOptionAdapter detailResultListViewAdapter = new SelectedOptionAdapter(getActivity());
        //textView.setText("Route from " + MainActivity.sourceName + " to " + MainActivity.destinationName);
        if (element.legs.size() > 1) {
            element.legs.add(1, null);
            Log.v(MainActivity.TAG, "bub" + JsonParser.resultContainerArrayList.get(selectedOptionID).legs.size() + "");

            //textView.setText(textView.getText() + " via " + element.legs.get(0).station_name_end);
        }
        textView = (TextView) fview.findViewById(R.id.textView21);
        textView.setText(MainActivity.destinationName);
        textView = (TextView) fview.findViewById(R.id.textView6);
        textView.setText(MainActivity.sourceName);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(detailResultListViewAdapter);
        detailResultListViewAdapter.notifyDataSetChanged();
        return fview;
    }
}
