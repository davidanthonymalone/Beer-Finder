package dm.pivofinder.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import dm.pivofinder.R;
import dm.pivofinder.main.BeerApp;


public class Base extends AppCompatActivity {

    public static BeerApp	app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (BeerApp) getApplication();
       LocationManager locMan=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        


    }

    protected void goToActivity(Activity current,
                                Class<? extends Activity> activityClass,
                                Bundle bundle) {
        Intent newActivity = new Intent(current, activityClass);

        if (bundle != null) newActivity.putExtras(bundle);

        current.startActivity(newActivity);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    public void menuHome(MenuItem m)
    {
        goToActivity(this, Home.class, null);
    }


}
