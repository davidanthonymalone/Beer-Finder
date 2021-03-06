package dm.pivofinder.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import dm.pivofinder.R;
import dm.pivofinder.models.Beer;

import static dm.pivofinder.activities.Base.app;

public class MapFragment
        extends android.app.Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener,
        View.OnClickListener {

    private static final String[] LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST = 0;
    int locationCount;

    protected TextView titleBar;
    private String beerName, Bar;
    //GoogleMap Object
    private GoogleMap mMap;
    private String mlattext, mlngtext;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    TextView textView;
    Location getmLastLocation;
    SeekBar seekBar;
    int maxBeerPrice = 20;
    View view;


    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private List<Beer> mBeerList;


    private long UPDATE_INTERVAL = 30000; /* 30 secs */
    private long FASTEST_INTERVAL = 1000; /* 5 secs */


    private final int[] MAP_TYPES = {
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE
    };

    private int curMapTypeIndex = 2;


    // Required empty public constructor
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;

    }

    private boolean canAccessLocation() {
        return (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_map, container, false);

        } catch (InflateException e) {

        }
        seekBar = (SeekBar) view.findViewById(R.id.seekBar2);
        textView = (TextView) view.findViewById(R.id.textView2);
        TextView text = (TextView) getActivity().findViewById(R.id.beerTitle);
        text.setText("View all Beers on the Map");


        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        mMap.clear();
                        progress = progressValue;
                        maxBeerPrice = progressValue;
                        addMarersToMap(app.dbManager.getAll());

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                        textView.setText("€ " + progress + "/" + seekBar.getMax());
                    }
                });
        if (canAccessLocation()) {

        } else {
            requestPermissions(LOCATION, LOCATION_REQUEST);
        }

        //mapFragment gets managed by the getChildFragmentManager()
        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapz);
        mapFragment.getMapAsync(this);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            buildGoogleApiClient();

        }

        return view;


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(false);
        }
        addMarersToMap(app.dbManager.getAll());

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }
    }

    public void onConnected(Bundle dataBundle) {
// Display the connection status
        try {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {


            }
        } catch (SecurityException se) {
            Toast.makeText(getActivity(), "Your Permissions aren't working please check them", Toast.LENGTH_SHORT).show();
        }
        if (mCurrentLocation != null) {
            Toast.makeText(getActivity(), "Location was found", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        } else {
            Toast.makeText(getActivity(), "Location null so defaulting to Waterford", Toast.LENGTH_SHORT).show();
            mCurrentLocation = new Location("Waterford City Default");
            LatLng latLng = new LatLng(52.246452, -7.139518);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                mMap.addMarker(new MarkerOptions().position(point).title("" + point).snippet("Location"));

            }
        });

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onClick(View v) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());


    }

    public void addMarersToMap(List<Beer> list) {
        for (Beer beer : app.dbManager.getAll()) {
            if (beer.price <= maxBeerPrice) {
                this.mMap.addMarker(new MarkerOptions().position(new LatLng(beer.lat, beer.lng)).title(beer.name + " \u20ac" + beer.price).snippet(beer.bar + " " + beer.price).icon(BitmapDescriptorFactory.fromResource(R.drawable.bar)));
            }
        }
    }


}