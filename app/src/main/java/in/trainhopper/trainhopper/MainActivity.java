package in.trainhopper.trainhopper;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
    public static long dateMILLIs;
    Context context;
    static public String TAG = "Nero", source = "", destination = "", sourceName = "", destinationName = "", timeA = "0", timeB = "0", date = "";
    static boolean checked = false;
    Toolbar toolbar;

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        //Log.v("nero",fm.getBackStackEntryCount()+"");
        if(MainActivityFragment.isAdvancedOptionsOpen){
            MainActivityFragment.closeAdvancedOptions();
        }else if (fm.getBackStackEntryCount() > 1)
            fm.popBackStack();
        else
            super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, new MainActivityFragment());
        fragmentTransaction.commit();


         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    static String formatDate1(long date){
        String str="";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM dd, yyyy",Locale.US);
        Date date1=new Date(date);
        str=simpleDateFormat.format(date1);
        return str;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}