package dm.pivofinder.activities;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import dm.pivofinder.R;
import dm.pivofinder.fragments.BeerFragment;
import dm.pivofinder.fragments.UpdateFragment;
import dm.pivofinder.fragments.UpdateFragment.OnFragmentInteractionListener;
import dm.pivofinder.fragments.AddFragment;
import dm.pivofinder.fragments.InformationFragment;
import dm.pivofinder.fragments.MapFragment;


public class Home extends Base implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    android.app.FragmentManager fragmentManager = getFragmentManager();
    private static final String[] LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST = 0;
    private boolean canAccessLocation() {

        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (canAccessLocation()) {

        } else {
            requestPermissions(LOCATION, LOCATION_REQUEST);
        }
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        BeerFragment fragment = BeerFragment.newInstance();
        ft.replace(R.id.homeFrame, fragment);
        ft.commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();
        Fragment fragment;
        FragmentTransaction fragt = getFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
            fragment = BeerFragment.newInstance();
            fragt.replace(R.id.homeFrame, fragment);
            fragt.addToBackStack(null);
            fragt.commit();

        } else if (id == R.id.nav_add) {
            fragt.replace(R.id.homeFrame, new AddFragment()).addToBackStack("").commit();




        } else if (id == R.id.nav_map) {
            fragt.replace(R.id.homeFrame, new MapFragment()).addToBackStack("").commit();



        }else if (id == R.id.about_us) {
            fragt.replace(R.id.homeFrame, new InformationFragment()).addToBackStack("").commit();



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void toggle(View v) {
        UpdateFragment editFrag = (UpdateFragment)
                getFragmentManager().findFragmentById(R.id.homeFrame);

        if (editFrag != null) {
            editFrag.toggle(v);
        }
    }

    @Override
    public void update(View v) {
        UpdateFragment editFrag = (UpdateFragment)
                getFragmentManager().findFragmentById(R.id.homeFrame);

        if (editFrag != null) {
            editFrag.update(v);
        }
    }




}
