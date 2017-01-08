package dm.pivofinder.main;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dm.pivofinder.db.DBManager;
import dm.pivofinder.models.Beer;


public class BeerApp extends Application {
    //public List <Beer>  BeerList = new ArrayList<Beer>();
    public DBManager dbManager = new DBManager(this);

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        Log.v("Beer", "Suds started");
        dbManager.open();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dbManager.close();
    }
}