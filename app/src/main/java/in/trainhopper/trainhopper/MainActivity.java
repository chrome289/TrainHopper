package in.trainhopper.trainhopper;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static final public String IP="http://192.168.1.6:3001";
    // --Commented out by Inspection (19-08-2017 05:35 PM):private Context context;
    static public final String TAG = "Nero";
    static public String source = "";
    static public String destination = "";
    static public String sourceName = "";
    static public String destinationName = "";
    static public boolean[] bool = {true, true, true, true, true, true, true, true, true};
    static Date date = null;
    static boolean checked = false;

    static String formatDate1(Date date) {
        String str;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        str = simpleDateFormat.format(date);
        return str;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        //Log.v("nero",fm.getBackStackEntryCount()+"");
        if (fm.getBackStackEntryCount() > 1)
            fm.popBackStack();
        else
            super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, new MainActivityFragment());
        fragmentTransaction.commit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}