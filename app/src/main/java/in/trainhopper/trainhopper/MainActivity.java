package in.trainhopper.trainhopper;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static final public String IP="http://192.168.1.6:3001";
    Context context;
    static public String TAG = "Nero", source = "", destination = "", sourceName = "", destinationName = "", timeA = "0", timeB = "0", date = "";
    static boolean checked = false;

    @Override
    public void onBackPressed() {
        if (MainActivityFragment.popup != null && MainActivityFragment.popup.getVisibility() == View.VISIBLE) {
            MainActivityFragment.popup.setVisibility(View.GONE);
        } else {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() != 1)
                fm.popBackStack();
            else
                super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, new MainActivityFragment(), "search").addToBackStack("sc1");
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  TrainHopper");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}