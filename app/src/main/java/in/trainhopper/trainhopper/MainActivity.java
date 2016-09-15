package in.trainhopper.trainhopper;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toolbar;

public class MainActivity extends Activity {

    static final public String IP="http://192.168.1.102:8080";
    private static MainActivityFragment mainActivityFragment = new MainActivityFragment();
    static ResultFragment resultFragment = new ResultFragment();
    static SelectedOption selectedOption = new SelectedOption();
    static Context context;
    static public String TAG = "Nero", source = "", destination = "", sourceName = "", destinationName = "", timeA = "0", timeB = "0", date = "";
    static boolean checked = false;

    @Override
    public void onBackPressed() {
        if (MainActivityFragment.popup != null) {
            if (MainActivityFragment.popup.getVisibility() == View.VISIBLE) {
                MainActivityFragment.popup.setVisibility(View.GONE);
            } else
                super.onBackPressed();
        } else
            super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, mainActivityFragment, "search");
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  TrainHopper");
        toolbar.setTitleTextColor(Color.WHITE);
        setActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}