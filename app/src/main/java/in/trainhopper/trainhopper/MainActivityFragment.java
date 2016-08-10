package in.trainhopper.trainhopper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    static public View fview;
    static String TAG = "Message";
    static public boolean[] bool = {true, true, true, true, true, true, true, true, true};

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fview == null)
            fview = inflater.inflate(R.layout.fragment_main, container, false);

        String[] stationList = getActivity().getResources().getStringArray(R.array.station_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stationList);

        final Calendar calendar = new GregorianCalendar();
        calendar.getTime();

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) fview.findViewById(R.id.source);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView = (AutoCompleteTextView) fview.findViewById(R.id.destination);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(2);

        final FloatingActionButton fab = (FloatingActionButton) fview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout1);
                relativeLayout.setAlpha(0.3f);
                relativeLayout = (RelativeLayout) fview.findViewById(R.id.layout2);
                relativeLayout.setVisibility(View.VISIBLE);

                EditText editText = (EditText) fview.findViewById(R.id.source);
                MainActivity.source = (String.valueOf(editText.getText()).split("\\(")[1]).split("\\)")[0].trim();
                MainActivity.sourceName = String.valueOf(editText.getText()).split("\\(")[0].trim();
                editText = (EditText) fview.findViewById(R.id.destination);
                MainActivity.destination = (String.valueOf(editText.getText()).split("\\(")[1]).split("\\)")[0].trim();
                MainActivity.destinationName = String.valueOf(editText.getText()).split("\\(")[0].trim();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                MainActivity.date = simpleDateFormat.format(calendar.getTime());

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

                                Log.v(TAG, "stored in arraylist");

                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, MainActivity.resultFragment, "result").addToBackStack(null);
                                fragmentTransaction.commit();
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
                        params.put("date", MainActivity.date);
                        params.put("sort", "1");
                        params.put("a1", String.valueOf(bool[0]));
                        params.put("a2", String.valueOf(bool[1]));
                        params.put("a3", String.valueOf(bool[2]));
                        params.put("sl", String.valueOf(bool[3]));
                        params.put("cc", String.valueOf(bool[4]));
                        params.put("s2", String.valueOf(bool[5]));
                        params.put("e3", String.valueOf(bool[6]));
                        params.put("fc", String.valueOf(bool[7]));
                        params.put("gen", String.valueOf(bool[8]));
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
        });


        final TextView textView = (TextView) fview.findViewById(R.id.date);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                        textView.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        TextView textView1 = (TextView) fview.findViewById(R.id.classes);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.context);
                builder.setTitle("Select Classes");
                builder.setMultiChoiceItems(R.array.class_list, bool, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        bool[i] = b;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });

        // final Calendar calendar2 = Calendar.getInstance();
        final Calendar calendar1 = Calendar.getInstance();
        Log.v(MainActivity.TAG, "hour" + calendar.HOUR + "--" + calendar.HOUR_OF_DAY);
        final TextView textView2 = (TextView) fview.findViewById(R.id.time);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.v(MainActivity.TAG, i + "---" + i1);
                        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                        MainActivity.time = String.valueOf(i * 3600 + (i1 * 60));
                        Date dateObj = null;
                        try {
                            dateObj = sdf.parse(i + ":" + i1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        textView2.setText(new SimpleDateFormat("h:mm a").format(dateObj));
                    }
                }, calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });
        return fview;
    }
}
