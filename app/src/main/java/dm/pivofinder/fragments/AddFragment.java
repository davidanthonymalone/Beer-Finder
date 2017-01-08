package dm.pivofinder.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import dm.pivofinder.R;
import dm.pivofinder.activities.Home;
import dm.pivofinder.models.Beer;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static dm.pivofinder.activities.Base.app;

public class AddFragment extends android.app.Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener,
        PlaceSelectionListener
{

    private static final String[] LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST = 0;
    public String newaddress = "", beerName, Bar;
    TextView textView;
    private String barz;
    Spinner spinner;
    Location mLastLocation;
    String county;
    View view;
    private double beerPrice, beerLat, beerLng;
    private EditText name,  price;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final LatLngBounds WATERFORD_BAR_VIEW = new LatLngBounds(
            new LatLng(52.254539, -7.149922), new LatLng(52.254700, -7.100484));
    private static final int REQUEST_SELECT_PLACE = 1000;
    private TextView locationTextView, bar;



    // Required empty public constructor
    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;

    }

    private boolean canAccessLocation() {
        return (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try{
            view = inflater.inflate(R.layout.fragment_add, container, false);

        }catch(InflateException e){

        }
        buildGApiClient();


        if (canAccessLocation()) {

        } else {
            requestPermissions(LOCATION, LOCATION_REQUEST);
        }
        TextView text = (TextView) getActivity().findViewById(R.id.beerTitle);
        text.setText("Add a Beer");


        //mapFragment gets managed by the getChildFragmentManager()
        Button saveButton = (Button) view.findViewById(R.id.saveBeerBtn);
        name = (EditText) view.findViewById(R.id.beerNameEditText);
        bar = (TextView) view.findViewById(R.id.barEditText);
       // address = (EditText) view.findViewById(R.id.editTextCity);
       // textView = (TextView) view.findViewById(R.id.textView3);
       // spinner = (Spinner) view.findViewById(R.id.spinner);
        locationTextView = (TextView) view.findViewById(R.id.locationContentView);
        locationTextView.setText("");

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setBoundsBias(WATERFORD_BAR_VIEW);







        price = (EditText) view.findViewById(R.id.priceEditText);



        saveButton.setOnClickListener(this);

        return view;
    }


    protected synchronized void buildGApiClient() {
        //adding items into the google api client
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        //connecting to the google api client
        mGoogleApiClient.connect();
        //checking for permissions and getting permissions
        if (ActivityCompat.checkSelfPermission(getActivity()
                .getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //getting the last known location
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }
    }

    //deals with items when connected
    public void onConnected(Bundle dataBundle) {


// Display the connection status
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //location request
        LocationRequest  lr = LocationRequest.create();
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lr.setInterval(10000); // Update location every sec
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, lr, this);
        Log.d(TAG, "Location Check Start");
        // gets the current location
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


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

        //Place current location marker


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }


    //handles on click
    @Override
    public void onClick(View v) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);





        beerName = name.getText().toString();
        try {
            beerPrice = Double.parseDouble(price.getText().toString());
        } catch (NumberFormatException e) {
            beerPrice = 0.0;
        }

        //app validation
        if ((beerName.length() > 0)  && (price.length() > 0) && (beerLat != 0.0) && (beerLng != 0.0) ) {


            try {
                LatLng point = new LatLng(beerLat, beerLng);

                if (beerLat != 0 || beerLng != 0 && locationTextView.equals("")) {
                   ;
                    final Beer d = new Beer(beerName, barz,
                            beerPrice, newaddress, beerLat, beerLng);

                    app.dbManager.insert(d);
                    Intent intent = new Intent(getActivity(), Home.class);
                    getActivity().startActivity(intent);

                    name.setText("");
                    this.bar.setText("");
                    price.setText("");

                } else {


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (beerLat == 0.0 || beerLng == 0 || newaddress.equals("")) {
            Toast.makeText(getActivity(), "Please Enter All Fields Before trying to add a beer",
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_us) {
            // Method #3
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder
                        (PlaceAutocomplete.MODE_OVERLAY)
                        .setBoundsBias(WATERFORD_BAR_VIEW)
                        .build(AddFragment.this.getActivity());
                startActivityForResult(intent, REQUEST_SELECT_PLACE);
            } catch (GooglePlayServicesRepairableException |
                    GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //on place selected dealing with the google places
    @Override
    public void onPlaceSelected(Place place) {

         barz = place.getName().toString();

        newaddress = place.getAddress().toString();
        beerLat = place.getLatLng().latitude;
        beerLng = place.getLatLng().longitude;
        Log.i(LOG_TAG, "WHooo it worked!: " + newaddress);

        locationTextView.setText(place.getAddress() + " " + place.getName());


        }


    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(AddFragment.this.getActivity(), data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(AddFragment.this.getActivity(), data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}