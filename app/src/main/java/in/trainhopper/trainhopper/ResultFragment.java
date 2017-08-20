package in.trainhopper.trainhopper;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultFragment extends Fragment {

    private View fview;
    private int checkedItemId = 0;
    private ResultRecyclerViewAdapter resultRecyclerViewAdapter;

    private boolean mLoading = false;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_result, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(MainActivity.sourceName + " TO " + MainActivity.destinationName);

            actionBar.setSubtitle(MainActivity.formatDate1(MainActivity.date));
        }
        if (JsonParser.resultContainerArrayList.size() == 0) {
            TextView textView2 = fview.findViewById(R.id.textView30);
            textView2.setVisibility(View.VISIBLE);
        } else {
            TextView textView2 = fview.findViewById(R.id.textView30);
            textView2.setVisibility(View.INVISIBLE);
        }
        FloatingActionButton button = fview.findViewById(R.id.fab2);
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

        RecyclerView recyclerView = fview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultRecyclerViewAdapter = new ResultRecyclerViewAdapter(getActivity(), JsonParser.resultContainerArrayList);
        recyclerView.setAdapter(resultRecyclerViewAdapter);

        RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItems = recyclerView.getLayoutManager().getChildCount();
                int totalItems = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (!mLoading) {
                    if ((firstVisibleItem+visibleItems)>=resultRecyclerViewAdapter.getItemCount()
                            && resultRecyclerViewAdapter.getItemCount()<50) {
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.IP + "/paginationdroid",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        new fetchMoreResults().execute(response);
                                }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               // Log.v(MainActivity.TAG, error.toString());
                                mLoading=false;
                                removeLoadingScreen();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                JsonParser.resultCurrentPage++;
                                Map<String, String> params = new HashMap<>();
                                params.put("queryID", JsonParser.resultQueryID+"");
                                params.put("page", JsonParser.resultCurrentPage+"");
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                        mLoading=true;
                        RelativeLayout relativeLayout = fview.findViewById(R.id.layout2);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        recyclerView.addOnScrollListener(onScrollListener);
        resultRecyclerViewAdapter.notifyDataSetChanged();

        return fview;
    }

    private void sortListView() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.IP + "/resultsdroid",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // Log.v(MainActivity.TAG, String.valueOf(response.length()));
                        new parseResults().execute(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                removeLoadingScreen();
                //Log.v(MainActivity.TAG, error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("from", MainActivity.source);
                params.put("to", MainActivity.destination);
                params.put("time", String.valueOf(MainActivity.date.getTime()));
                // params.put("timeB", MainActivity.timeB);
                params.put("direct", String.valueOf(MainActivity.checked));
                params.put("sort", String.valueOf(checkedItemId + 1));

                String classes = "";
                classes += (MainActivity.bool[0]) ? ",a1," : "";
                classes += (MainActivity.bool[1]) ? ",a2," : "";
                classes += (MainActivity.bool[2]) ? ",a3," : "";
                classes += (MainActivity.bool[3]) ? ",sl," : "";
                classes += (MainActivity.bool[4]) ? ",cc," : "";
                classes += (MainActivity.bool[5]) ? ",s2," : "";
                classes += (MainActivity.bool[6]) ? ",e3," : "";
                classes += (MainActivity.bool[7]) ? ",fc," : "";
                classes += (MainActivity.bool[8]) ? ",gen," : "";
                params.put("classes", '[' + classes.substring(1, classes.length() - 1) + ']');
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
        RelativeLayout relativeLayout = fview.findViewById(R.id.layout2);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    private void removeLoadingScreen() {
        RelativeLayout relativeLayout = fview.findViewById(R.id.layout2);
        relativeLayout.setVisibility(View.GONE);
    }

    private class fetchMoreResults extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {

            //Log.v(MainActivity.TAG, response);
            //JsonParser.resultContainerArrayList.clear();
            try {
                new JsonParser().parseResponse2(getActivity(), strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.v(MainActivity.TAG, "stored in arraylist");
            //resultRecyclerViewAdapter = new ResultRecyclerViewAdapter(getActivity(), JsonParser.resultContainerArrayList);
            //listView.setAdapter(resultRecyclerViewAdapter);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultRecyclerViewAdapter.notifyDataSetChanged();
                    mLoading = false;

                    RelativeLayout relativeLayout = fview.findViewById(R.id.layout2);
                    relativeLayout.setVisibility(View.GONE);
                }
            });
            return 0;
        }

        protected void onProgressUpdate(Integer... p) {

        }

        protected void onPostExecute(Integer p) {

        }
    }

    class parseResults extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            JsonParser.resultContainerArrayList.clear();
            try {
                new JsonParser().parseResponse1(getActivity(), strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        protected void onProgressUpdate(Integer... p) {

        }

        protected void onPostExecute(Integer p) {
            resultRecyclerViewAdapter.notifyDataSetChanged();
            removeLoadingScreen();
        }
    }
}