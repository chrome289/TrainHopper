package in.trainhopper.trainhopper;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ResultFragment extends Fragment {

    View fview;
    int checkedItemId = 0;
    ListView listView;
    ResultListViewAdapter resultListViewAdapter;
    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_result, container, false);
        if (JsonParser.resultContainerArrayList.size() == 0) {
            TextView textView = (TextView) fview.findViewById(R.id.textView30);
            textView.setVisibility(View.VISIBLE);
        } else {
            TextView textView = (TextView) fview.findViewById(R.id.textView30);
            textView.setVisibility(View.INVISIBLE);
        }
        FloatingActionButton button = (FloatingActionButton) fview.findViewById(R.id.fab2);
        checkedItemId = 0;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sorting Criteria");
                builder.setSingleChoiceItems(R.array.sorting_options, checkedItemId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkedItemId = i;
                        Log.v(MainActivity.TAG, String.valueOf(checkedItemId));

                        sortListView();
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        listView = (ListView) fview.findViewById(R.id.listView);
        resultListViewAdapter = new ResultListViewAdapter(getActivity(), JsonParser.resultContainerArrayList);
        listView.setAdapter(resultListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.selectedOption.selectedOptionID = i;
                MainActivity.selectedOption.element = JsonParser.resultContainerArrayList.get(i);
                Log.v(MainActivity.TAG, i + "");
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, MainActivity.selectedOption, "select").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        resultListViewAdapter.notifyDataSetChanged();
        return fview;
    }

    private void sortListView() {
       /* if (checkedItemId == 0) {//total duration
            for (int x = 1; x < elementsArrayList.size(); x++) {
                Elements elements1 = elementsArrayList.get(x).select("div.right");
                Elements elements2 = elementsArrayList.get(x - 1).select("div.right");
                Log.v(MainActivity.TAG, Integer.parseInt(elements1.get(0).ownText().replace(":", "")) + "----" + Integer.parseInt(elements2.get(0).ownText().replace(":", "")));
                if (Integer.parseInt(elements1.get(0).ownText().replace(":", "")) < Integer.parseInt(elements2.get(0).ownText().replace(":", ""))) {
                    int z = 0;
                    for (int y = x - 1; y >= 0; y--) {
                        Elements elements3 = elementsArrayList.get(y).select("div.right");
                        if (Integer.parseInt(elements1.get(0).ownText().replace(":", "")) < Integer.parseInt(elements3.get(0).ownText().replace(":", "")))
                            z = y;
                    }
                    elementsArrayList.add(z, elementsArrayList.get(x));
                    elementsArrayList.remove(x + 1);
                }
            }
        }
        if (checkedItemId == 1) {//arrival time(source)
            for (int x = 1; x < elementsArrayList.size(); x++) {
                Elements elements1 = elementsArrayList.get(x).select("span.station-time");
                Elements elements2 = elementsArrayList.get(x - 1).select("span.station-time");
                Log.v(MainActivity.TAG, Integer.parseInt(elements1.get(0).ownText().replace(":", "")) + "----" + Integer.parseInt(elements2.get(0).ownText().replace(":", "")));
                if (Integer.parseInt(elements1.get(0).ownText().replace(":", "")) < Integer.parseInt(elements2.get(0).ownText().replace(":", ""))) {
                    int z = 0;
                    for (int y = x - 1; y >= 0; y--) {
                        Elements elements3 = elementsArrayList.get(y).select("span.station-time");
                        if (Integer.parseInt(elements1.get(0).ownText().replace(":", "")) < Integer.parseInt(elements3.get(0).ownText().replace(":", "")))
                            z = y;
                    }
                    elementsArrayList.add(z, elementsArrayList.get(x));
                    elementsArrayList.remove(x + 1);
                }
            }
        }
        if (checkedItemId == 2) {//arrival time(destination)
            ArrayList<Element> elementsArrayList1 = new ArrayList<>();
            //daywise buckets
            for (int x = 0; x < 7; x++) {
                for (int y = 0; y < elementsArrayList.size(); y++) {
                    Elements elements1 = elementsArrayList.get(y).select("span.station-day");
                    if (Integer.parseInt(elements1.get(elements1.size() - 1).ownText()) == x)
                        elementsArrayList1.add(elementsArrayList.get(y));
                }
            }
            //bucket sorting(time)
            for (int x = 1; x < elementsArrayList1.size(); x++) {
                Elements elements1 = elementsArrayList1.get(x).select("span.station-time");
                Elements elements2 = elementsArrayList1.get(x - 1).select("span.station-time");
                Elements a1 = elementsArrayList1.get(x).select("span.station-day");
                Elements a2 = elementsArrayList1.get(x - 1).select("span.station-day");
                Log.v(MainActivity.TAG, Integer.parseInt(elements1.get(elements1.size() - 1).ownText().replace(":", "")) + "----" + Integer.parseInt(elements2.get(elements2.size() - 1).ownText().replace(":", "")));
                if ((a1.get(a1.size()-1).ownText()).equals(a2.get(a2.size()-1).ownText()) && Integer.parseInt(elements1.get(elements1.size() - 1).ownText().replace(":", "")) < Integer.parseInt(elements2.get(elements2.size() - 1).ownText().replace(":", ""))) {
                    int z = 0;
                    for (int y = x - 1; y >= 0; y--) {
                        Elements elements3 = elementsArrayList1.get(y).select("span.station-time");
                        Elements a3 = elementsArrayList1.get(y).select("span.station-day");
                        if ((a1.get(a1.size()-1).ownText()).equals(a3.get(a3.size()-1).ownText()) && Integer.parseInt(elements1.get(elements1.size() - 1).ownText().replace(":", "")) < Integer.parseInt(elements3.get(elements3.size() - 1).ownText().replace(":", "")))
                            z = y;
                    }
                    elementsArrayList1.add(z, elementsArrayList1.get(x));
                    elementsArrayList1.remove(x + 1);
                }
            }
            elementsArrayList=elementsArrayList1;
        }
        if (checkedItemId == 3) {//wait item
            for (int x = 1; x < elementsArrayList.size(); x++) {
                Elements elements1 = elementsArrayList.get(x).select("span.wait-time");
                Elements elements2 = elementsArrayList.get(x - 1).select("span.wait-time");
                Log.v(MainActivity.TAG, Integer.parseInt(elements1.get(0).ownText().replace(":", "")) + "----" + Integer.parseInt(elements2.get(0).ownText().replace(":", "")));
                if (Integer.parseInt(elements1.get(0).ownText().replace(":", "")) < Integer.parseInt(elements2.get(0).ownText().replace(":", ""))) {
                    int z = 0;
                    for (int y = x - 1; y >= 0; y--) {
                        Elements elements3 = elementsArrayList.get(y).select("span.wait-time");
                        if (Integer.parseInt(elements1.get(0).ownText().replace(":", "")) < Integer.parseInt(elements3.get(0).ownText().replace(":", "")))
                            z = y;
                    }
                    elementsArrayList.add(z, elementsArrayList.get(x));
                    elementsArrayList.remove(x + 1);
                }
            }
        }*/
        resultListViewAdapter = new ResultListViewAdapter(getActivity(), JsonParser.resultContainerArrayList);
        listView.setAdapter(resultListViewAdapter);
        resultListViewAdapter.notifyDataSetChanged();
    }

}
