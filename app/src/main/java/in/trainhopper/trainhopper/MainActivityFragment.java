package in.trainhopper.trainhopper;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class MainActivityFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    // --Commented out by Inspection (19-08-2017 05:35 PM):private static String TAG = "Message";
    // --Commented out by Inspection (19-08-2017 05:35 PM):RelativeLayout popup;
    static boolean isAdvancedOptionsOpen = false;
    ParseResults parseResults = new ParseResults();
    private View fview;
    private DrawerLayout drawerLayout;
    private Calendar calendar;

    public MainActivityFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
        parseResults.cancel(true);
    }
    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setTitle("TrainHopper");
            actionBar.setSubtitle("");
            actionBar.hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_main, container, false);

        TextView textView = fview.findViewById(R.id.textView);
        textView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/dosis.ttf"));
        textView = fview.findViewById(R.id.textView10);
        textView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/dosis3.ttf"));
        Log.v("nero", "started");


        if (MainActivity.date == null) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            MainActivity.date = calendar.getTime();
        } else {
            calendar = Calendar.getInstance();
            calendar.setTime(MainActivity.date);
        }

        final Button button = fview.findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MainActivity.sourceName.equals("")) {
                    Snackbar.make(fview, "Enter Source", Snackbar.LENGTH_SHORT).show();
                } else if (MainActivity.destinationName.equals("")) {
                    Snackbar.make(fview, "Enter Destination", Snackbar.LENGTH_SHORT).show();
                } else {
                    RelativeLayout relativeLayout = fview.findViewById(R.id.cover);
                    relativeLayout.setVisibility(View.VISIBLE);

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.IP + "/resultsdroid",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    // Log.v(MainActivity.TAG, String.valueOf(response.length()));
                                    parseResults = new ParseResults();
                                    parseResults.execute(response);
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
                            params.put("sort", "1");
                            params.put("utcoffset", TimeZone.getDefault().getOffset(MainActivity.date.getTime()) + "");
                            Log.v("nero", TimeZone.getDefault().getOffset(MainActivity.date.getTime()) + " offset");
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
                }
            }
        });

        drawerLayout = fview.findViewById(R.id.drawer);

        Button button1 = fview.findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
                isAdvancedOptionsOpen = true;
            }
        });

        ImageButton imageButton = fview.findViewById(R.id.imageButton6);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAdvancedOptions();
            }
        });

        AutoCompleteTextView editText = fview.findViewById(R.id.source);
        final String[] arr = getActivity().getResources().getStringArray(R.array.station_name);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_list_item_1, android.R.id.text1
                , arr);
        editText.setAdapter(arrayAdapter);
        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.source = (String.valueOf(parent.getItemAtPosition(position))
                        .split("\\(")[1])
                        .split("\\)")[0]
                        .trim();
                MainActivity.sourceName = String.valueOf(parent.getItemAtPosition(position))
                        .split("\\(")[0]
                        .trim();
            }
        });

        editText = fview.findViewById(R.id.destination);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_list_item_1, android.R.id.text1
                , arr);
        editText.setAdapter(arrayAdapter2);
        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("nero",id+"$"+position+"%"+parent.getItemAtPosition(position));
                MainActivity.destination = (String.valueOf(parent.getItemAtPosition(position))
                        .split("\\(")[1])
                        .split("\\)")[0]
                        .trim();
                MainActivity.destinationName = String.valueOf(parent.getItemAtPosition(position))
                        .split("\\(")[0]
                        .trim();
            }
        });

        final Button textView2 = fview.findViewById(R.id.date);

        new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        textView2.setText(simpleDateFormat.format(MainActivity.date));

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        SimpleDateFormat simpleDateFormat;
                        MainActivity.date = calendar.getTime();
                        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                        textView2.setText(simpleDateFormat.format(MainActivity.date));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        FloatingActionButton floatingActionButton = fview.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = MainActivity.source;
                MainActivity.source = MainActivity.destination;
                MainActivity.destination = temp;

                temp = MainActivity.sourceName;
                MainActivity.sourceName = MainActivity.destinationName;
                MainActivity.destinationName = temp;

                EditText tempText = fview.findViewById(R.id.source);
                EditText tempText2 = fview.findViewById(R.id.destination);
                temp = String.valueOf(tempText.getText());
                tempText.setText(tempText2.getText());
                tempText2.setText(temp);
            }
        });

        Log.v(MainActivity.TAG, "hour" + Calendar.HOUR + "--" + Calendar.HOUR_OF_DAY);
        final Button button3 = fview.findViewById(R.id.timeA);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.v(MainActivity.TAG, i + "---" + i1);
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);
                        MainActivity.date = calendar.getTime();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        Date date = null;
                        try {
                            date = simpleDateFormat.parse(i + ":" + i1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
                        button3.setText(simpleDateFormat1.format(date));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                //Log.v("nero",MainActivity.timeA+"");
                timePickerDialog.updateTime(calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE));
                timePickerDialog.show();
            }
        });

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a", Locale.US);

        button3.setText(simpleDateFormat1.format(MainActivity.date));

        CheckBox checkBox = fview.findViewById(R.id.checkBox10);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.checked = b;
                //  Log.v(MainActivity.TAG, "direct " + b + "::" + MainActivity.checked);
            }
        });

        checkBox = fview.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(this);
        checkBox = fview.findViewById(R.id.checkBox2);
        checkBox.setOnCheckedChangeListener(this);
        checkBox = fview.findViewById(R.id.checkBox3);
        checkBox.setOnCheckedChangeListener(this);
        checkBox = fview.findViewById(R.id.checkBox4);
        checkBox.setOnCheckedChangeListener(this);
        checkBox = fview.findViewById(R.id.checkBox5);
        checkBox.setOnCheckedChangeListener(this);
        checkBox = fview.findViewById(R.id.checkBox6);
        checkBox.setOnCheckedChangeListener(this);
        checkBox = fview.findViewById(R.id.checkBox7);
        checkBox.setOnCheckedChangeListener(this);
        checkBox = fview.findViewById(R.id.checkBox8);
        checkBox.setOnCheckedChangeListener(this);
        checkBox = fview.findViewById(R.id.checkBox9);
        checkBox.setOnCheckedChangeListener(this);

        return fview;
    }


    private void closeAdvancedOptions() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        isAdvancedOptionsOpen = false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        MainActivity.bool[Integer.parseInt(String.valueOf(compoundButton.getContentDescription()))] = b;
    }

    private void removeLoadingScreen() {
        RelativeLayout relativeLayout = fview.findViewById(R.id.cover);
        relativeLayout.setVisibility(View.GONE);
    }

    class ParseResults extends AsyncTask<String, Integer, Integer> {

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
            removeLoadingScreen();

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);

                actionBar.setHomeButtonEnabled(true);

                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

                actionBar.show();
            }

            FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, new ResultFragment()).addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}

