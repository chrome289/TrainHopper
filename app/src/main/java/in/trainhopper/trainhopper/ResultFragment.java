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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

                        RelativeLayout relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout1);
                        relativeLayout.setAlpha(0.3f);
                        relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout2);
                        relativeLayout.setVisibility(View.VISIBLE);

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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.103:8080/resultsdroid",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.v(MainActivity.TAG, response);
                        JsonParser.resultContainerArrayList.clear();
                        try {
                            JsonParser.parseResponse(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        RelativeLayout relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout1);
                        relativeLayout.setAlpha(1.0f);
                        relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout2);
                        relativeLayout.setVisibility(View.INVISIBLE);
                        Log.v(MainActivity.TAG, "stored in arraylist");
                        resultListViewAdapter = new ResultListViewAdapter(getActivity(), JsonParser.resultContainerArrayList);
                        listView.setAdapter(resultListViewAdapter);
                        resultListViewAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RelativeLayout relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout1);
                relativeLayout.setAlpha(1.0f);
                relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout2);
                relativeLayout.setVisibility(View.INVISIBLE);
                Log.v(MainActivity.TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("from", MainActivity.source);
                params.put("to", MainActivity.destination);
                params.put("time", MainActivity.time);
                params.put("sort", String.valueOf(checkedItemId + 1));
                params.put("date", MainActivity.date);
                params.put("a1", String.valueOf(MainActivityFragment.bool[0]));
                params.put("a2", String.valueOf(MainActivityFragment.bool[1]));
                params.put("a3", String.valueOf(MainActivityFragment.bool[2]));
                params.put("sl", String.valueOf(MainActivityFragment.bool[3]));
                params.put("cc", String.valueOf(MainActivityFragment.bool[4]));
                params.put("s2", String.valueOf(MainActivityFragment.bool[5]));
                params.put("e3", String.valueOf(MainActivityFragment.bool[6]));
                params.put("fc", String.valueOf(MainActivityFragment.bool[7]));
                params.put("gen", String.valueOf(MainActivityFragment.bool[8]));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.v(MainActivity.TAG, "volley timeout");
            }
        });
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
        requestQueue.start();

    }
}