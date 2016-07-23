package in.trainhopper.trainhopper;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    static public View fview;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_main, container, false);

        String[] stationList = getActivity().getResources().getStringArray(R.array.station_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stationList);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) fview.findViewById(R.id.source);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView = (AutoCompleteTextView) fview.findViewById(R.id.destination);
        autoCompleteTextView.setAdapter(adapter);
        return fview;
    }
}
