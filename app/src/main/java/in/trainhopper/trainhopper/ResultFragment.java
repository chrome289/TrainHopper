package in.trainhopper.trainhopper;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private View fview;
    private int checkedItemId = 0;
    private RecyclerView recyclerView;
    private ResultRecyclerViewAdapter resultRecyclerViewAdapter;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_result, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.sourceName+" TO "+MainActivity.destinationName);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(MainActivity.formatDate1(MainActivity.dateMILLIs));

        if (JsonParser.resultContainerArrayList.size() == 0) {
            TextView textView2 = (TextView) fview.findViewById(R.id.textView30);
            textView2.setVisibility(View.VISIBLE);
        } else {
            TextView textView2 = (TextView) fview.findViewById(R.id.textView30);
            textView2.setVisibility(View.INVISIBLE);
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
                        //Log.v(MainActivity.TAG, String.valueOf(checkedItemId));

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

        recyclerView = (RecyclerView) fview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultRecyclerViewAdapter = new ResultRecyclerViewAdapter(getActivity(), JsonParser.resultContainerArrayList);
        recyclerView.setAdapter(resultRecyclerViewAdapter);

        RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener() {
            boolean mLoading=false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

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

                                        //Log.v(MainActivity.TAG, response);
                                        //JsonParser.resultContainerArrayList.clear();
                                        try {
                                            if (new JsonParser().parseResponse2(getActivity(), response) == 1) {
                                                // button1.setEnabled(false);
                                                //Log.v("TAG", JsonParser.resultContainerArrayList.size() + "");
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        //Log.v(MainActivity.TAG, "stored in arraylist");
                                        //resultRecyclerViewAdapter = new ResultRecyclerViewAdapter(getActivity(), JsonParser.resultContainerArrayList);
                                        //listView.setAdapter(resultRecyclerViewAdapter);
                                        resultRecyclerViewAdapter.notifyDataSetChanged();
                                        mLoading=false;

                                        RelativeLayout relativeLayout=(RelativeLayout)fview.findViewById(R.id.layout2);
                                        relativeLayout.setVisibility(View.GONE);
                                }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                RelativeLayout relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout1);
                                relativeLayout.setAlpha(1.0f);
                               // Log.v(MainActivity.TAG, error.toString());
                                mLoading=false;

                                relativeLayout=(RelativeLayout)fview.findViewById(R.id.layout2);
                                relativeLayout.setVisibility(View.GONE);
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
                        RelativeLayout relativeLayout=(RelativeLayout)fview.findViewById(R.id.layout2);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.IP+"/resultsdroid",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Log.v(MainActivity.TAG, response);
                        JsonParser.resultContainerArrayList.clear();
                        try {
                            new JsonParser().parseResponse1(getActivity(),response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        RelativeLayout relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout1);
                        relativeLayout.setAlpha(1.0f);
                        relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout2);
                        relativeLayout.setVisibility(View.INVISIBLE);
                        //Log.v(MainActivity.TAG, "stored in arraylist");
                        resultRecyclerViewAdapter = new ResultRecyclerViewAdapter(getActivity(), JsonParser.resultContainerArrayList);
                        recyclerView.setAdapter(resultRecyclerViewAdapter);
                        resultRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RelativeLayout relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout1);
                relativeLayout.setAlpha(1.0f);
                relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout2);
                relativeLayout.setVisibility(View.INVISIBLE);
                error.printStackTrace();//Log.v(MainActivity.TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("from", MainActivity.source);
                params.put("to", MainActivity.destination);
                params.put("time", MainActivity.timeA);
                params.put("sort", String.valueOf(checkedItemId + 1));
                params.put("date", MainActivity.date);
                params.put("direct", String.valueOf(MainActivity.checked));
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
               error.printStackTrace(); //Log.v(MainActivity.TAG, "volley timeout");
            }
        });
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
        requestQueue.start();

    }
}