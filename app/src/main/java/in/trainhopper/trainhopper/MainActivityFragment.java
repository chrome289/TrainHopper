package in.trainhopper.trainhopper;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class MainActivityFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View fview;
    private static String TAG = "Message";
    RelativeLayout popup;
    static public boolean[] bool = {true, true, true, true, true, true, true, true, true};
    static boolean isAdvancedOptionsOpen = false;
    static DrawerLayout drawerLayout;

    public MainActivityFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.v("nero", "paused");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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
        //Log.v("nero","started");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("TrainHopper");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
       ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        final Calendar calendar = new GregorianCalendar();
        calendar.getTime();

        MainActivity.dateMILLIs = calendar.getTimeInMillis();

        textView = fview.findViewById(R.id.source);
        textView.setOnClickListener(this);
        textView = fview.findViewById(R.id.destination);
        textView.setOnClickListener(this);

        final Button button = fview.findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MainActivity.sourceName.equals("")) {
                    Snackbar.make(getView(), "Enter Source", Snackbar.LENGTH_SHORT).show();
                } else if (MainActivity.destinationName.equals("")) {
                    Snackbar.make(getView(), "Enter Destination", Snackbar.LENGTH_SHORT).show();
                } else if (MainActivity.date.equals("")) {
                    Snackbar.make(fview, "Enter Date", Snackbar.LENGTH_SHORT).show();
                    //Log.v(MainActivity.TAG, "snackbar date");
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
                                    JsonParser.resultContainerArrayList.clear();
                                    try {
                                        new JsonParser().parseResponse1(getActivity(), response);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    RelativeLayout relativeLayout = fview.findViewById(R.id.cover);
                                    relativeLayout.setVisibility(View.GONE);

                                    // Log.v(TAG, "stored in arraylist");

                                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, new ResultFragment()).addToBackStack("sc2");
                                    fragmentTransaction.commit();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            RelativeLayout relativeLayout = fview.findViewById(R.id.cover);
                            relativeLayout.setVisibility(View.GONE);
                            //Log.v(MainActivity.TAG, error.toString());
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("from", MainActivity.source);
                            params.put("to", MainActivity.destination);
                            params.put("time", MainActivity.timeA);
                            // params.put("timeB", MainActivity.timeB);
                            params.put("date", MainActivity.date);
                            params.put("direct", String.valueOf(MainActivity.checked));
                            params.put("sort", "1");

                            String classes = "";
                            classes += (bool[0]) ? ",a1," : "";
                            classes += (bool[1]) ? ",a2," : "";
                            classes += (bool[2]) ? ",a3," : "";
                            classes += (bool[3]) ? ",sl," : "";
                            classes += (bool[4]) ? ",cc," : "";
                            classes += (bool[5]) ? ",s2," : "";
                            classes += (bool[6]) ? ",e3," : "";
                            classes += (bool[7]) ? ",fc," : "";
                            classes += (bool[8]) ? ",gen," : "";
                            params.put("classes", '[' + classes.substring(1, classes.length() - 1) + ']');
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
                            //Log.v(MainActivity.TAG, "volley timeout");
                            error.printStackTrace();
                        }
                    });
                    // Add the request to the RequestQueue.
                    requestQueue.add(stringRequest);
                    requestQueue.start();

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        MainActivity.date = simpleDateFormat.format(calendar.getTime());
        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        textView2.setText(simpleDateFormat.format(calendar.getTime()));

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        MainActivity.date = simpleDateFormat.format(calendar.getTime());
                        MainActivity.dateMILLIs = calendar.getTimeInMillis();
                        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                        textView2.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        /*final Button button1 = (Button) fview.findViewById(R.id.classes);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                builder.setCancelable(false);

                builder.show();
            }
        });*/

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

        // final Calendar calendar2 = Calendar.getInstance();
        final Calendar calendar1 = Calendar.getInstance();
        Log.v(MainActivity.TAG, "hour" + calendar1.HOUR + "--" + calendar1.HOUR_OF_DAY);
        final Button button3 = fview.findViewById(R.id.timeA);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.v(MainActivity.TAG, i + "---" + i1);
                        MainActivity.timeA = String.valueOf(i * 3600 + (i1 * 60));

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                        Date date= null;
                        try {
                            date = simpleDateFormat.parse(i+":"+i1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
                        button3.setText(simpleDateFormat1.format(date));
                    }
                }, calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), false);
                timePickerDialog.updateTime(Integer.parseInt(MainActivity.timeA) / 3600
                        , (Integer.parseInt(MainActivity.timeA) % 3600)/60);
                timePickerDialog.show();
            }
        });

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a", Locale.US);
        Date date = calendar1.getTime();
        button3.setText(simpleDateFormat1.format(date));

        // final Calendar calendar2 = Calendar.getInstance();
       /* final Calendar calendar1b = Calendar.getInstance();
        Log.v(MainActivity.TAG, "hour" + calendar.HOUR + "--" + calendar.HOUR_OF_DAY);
        final TextView textView2b = (TextView) fview.findViewById(R.id.timeB);
        textView2b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.v(MainActivity.TAG, i + "---" + i1);
                        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                        MainActivity.timeB = String.valueOf(i * 3600 + (i1 * 60));
                        Date dateObj = null;
                        try {
                            dateObj = sdf.parse(i + ":" + i1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        textView2b.setText("Select a Time\n"+new SimpleDateFormat("h:mm a").format(dateObj));
                    }
                }, calendar1b.get(Calendar.HOUR_OF_DAY), calendar1b.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });*/

        CheckBox checkBox = (CheckBox) fview.findViewById(R.id.checkBox10);
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

    @Override
    public void onClick(View view) {
       /*popup = (RelativeLayout) fview.findViewById(R.id.popup);
        popup.setVisibility(View.VISIBLE);
        final EditText editText = (EditText) fview.findViewById(R.id.citySelector);
        editText.requestFocus();
        editText.setText("");
        final ListView listView = (ListView) fview.findViewById(R.id.cityList);
        final String[] arr = getActivity().getResources().getStringArray(R.array.station_name);
        final View tview = view;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, Arrays.asList(getActivity().getResources().getStringArray(R.array.imp_station_name)));
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(MainActivity.TAG, "what " + tview.getId() + "       " + R.id.source);
                if (tview.getId() == R.id.source) {
                    Log.v(MainActivity.TAG, view.getId() + "       " + R.id.source + R.id.destination);
                    TextView textView = (TextView) fview.findViewById(R.id.source);
                    textView.setText(arrayAdapter.getItem(i));
                    MainActivity.source = (String.valueOf(arrayAdapter.getItem(i)).split("\\(")[1]).split("\\)")[0].trim();
                    MainActivity.sourceName = String.valueOf(arrayAdapter.getItem(i)).split("\\(")[0].trim();
                    popup.setVisibility(View.GONE);
                } else if (tview.getId() == R.id.destination) {
                    Log.v(MainActivity.TAG, view.getId() + "       " + R.id.destination);
                    TextView textView = (TextView) fview.findViewById(R.id.destination);
                    textView.setText(arrayAdapter.getItem(i));
                    MainActivity.destination = (String.valueOf(arrayAdapter.getItem(i)).split("\\(")[1]).split("\\)")[0].trim();
                    MainActivity.destinationName = String.valueOf(arrayAdapter.getItem(i)).split("\\(")[0].trim();
                    popup.setVisibility(View.GONE);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.v(MainActivity.TAG, "bubalu entered" + charSequence);
                final ArrayList<String> tempList = new ArrayList<>();
                boolean zeroMatch = true;
                for (String anArr : arr) {
                    if (anArr.contains(charSequence)) {
                        tempList.add(anArr);
                        zeroMatch = false;
                        //Log.v(MainActivity.TAG, "bubalu " + arr[x] + "::" + charSequence);
                    }
                }
                TextView textView = (TextView) fview.findViewById(R.id.error);
                if (zeroMatch)
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.GONE);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, tempList);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.v(MainActivity.TAG, "what " + tview.getId() + "       " + R.id.source);
                        if (tview.getId() == R.id.source) {
                            Log.v(MainActivity.TAG, view.getId() + "       " + R.id.source + R.id.destination);
                            TextView textView = (TextView) fview.findViewById(R.id.source);
                            textView.setText(arrayAdapter.getItem(i));
                            MainActivity.source = (String.valueOf(arrayAdapter.getItem(i)).split("\\(")[1]).split("\\)")[0].trim();
                            MainActivity.sourceName = String.valueOf(arrayAdapter.getItem(i)).split("\\(")[0].trim();
                            popup.setVisibility(View.GONE);
                        } else if (tview.getId() == R.id.destination) {
                            Log.v(MainActivity.TAG, view.getId() + "       " + R.id.destination);
                            TextView textView = (TextView) fview.findViewById(R.id.destination);
                            textView.setText(arrayAdapter.getItem(i));
                            MainActivity.destination = (String.valueOf(arrayAdapter.getItem(i)).split("\\(")[1]).split("\\)")[0].trim();
                            MainActivity.destinationName = String.valueOf(arrayAdapter.getItem(i)).split("\\(")[0].trim();
                            popup.setVisibility(View.GONE);
                        }
                    }
                });
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
    }

    public static void closeAdvancedOptions() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        isAdvancedOptionsOpen = false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        bool[Integer.parseInt(String.valueOf(compoundButton.getContentDescription()))] = b;
    }
}
