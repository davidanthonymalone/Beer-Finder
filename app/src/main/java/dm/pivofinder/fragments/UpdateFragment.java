package dm.pivofinder.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import dm.pivofinder.R;
import dm.pivofinder.models.Beer;
import dm.pivofinder.activities.Base;

import static android.app.Activity.RESULT_OK;


public class UpdateFragment extends Fragment implements PlaceSelectionListener {

    TextView titleBar;
    Beer abeer;
    View view;

    public String newaddress = "", beerName, Bar;
    EditText bName, barz, price;
    private double beerPrice, aDouble, bDouble;

    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final LatLngBounds WATERFORD_BAR_VIEW = new LatLngBounds(
            new LatLng(52.254539, -7.149922), new LatLng(52.254700, -7.100484));
    private static final int REQUEST_SELECT_PLACE = 1000;
    private TextView locationTextView, bar;
    private TextView attributionsTextView;


    private OnFragmentInteractionListener mListener;

    public UpdateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(Bundle BeerBundle) {
        UpdateFragment fragment = new UpdateFragment();
        fragment.setArguments(BeerBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //stops the app from getting inflation erros
        if (getArguments() != null)
            abeer = getBeerObject(getArguments().getInt("BeerID"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_update, container, false);

        } catch (InflateException e) {

        }


        //references widgets in the xml
        TextView text = (TextView) getActivity().findViewById(R.id.beerTitle);

        bName = (EditText) view.findViewById(R.id.beerNameEditText);
        bName.setText(abeer.name);
        text.setText("Update Beer: " + abeer.name);


        price = (EditText) view.findViewById(R.id.priceEditText);
        price.setText("" + abeer.price);
        locationTextView = (TextView) view.findViewById(R.id.locationContentView);

        //places auto complete code
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.barEditText);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setBoundsBias(WATERFORD_BAR_VIEW);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        titleBar = (TextView) getActivity().findViewById(R.id.beerTitle);
    }

    @Override
    public void onPlaceSelected(Place place) {

        double beerPrice, aDouble, bDouble;
        Bar = place.getName().toString();
        //assings address to new address from the place selected object
        newaddress = place.getAddress().toString();
        aDouble = place.getLatLng().latitude;
        bDouble = place.getLatLng().longitude;
        Log.i(LOG_TAG, "It Worked " + newaddress);

        locationTextView.setText(place.getAddress() + " " + place.getName());


    }


    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(UpdateFragment.this.getActivity(), data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(UpdateFragment.this.getActivity(), data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public interface OnFragmentInteractionListener {
        void toggle(View v);

        void update(View v);
    }

    private Beer getBeerObject(int id) {

        for (Beer beer : Base.app.dbManager.getAll())
            if (beer.beerId == id)
                return beer;

        return null;
    }

    public void toggle(View v) {

        if (mListener != null) {
            String s = null;


            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        }
    }

    public void update(View v) {

        if (mListener != null) {
            String BeerName = bName.getText().toString();
            String BeerPriceStr = price.getText().toString();
            double BeerPrice;

            try {
                BeerPrice = Double.parseDouble(BeerPriceStr);
            } catch (NumberFormatException e) {
                BeerPrice = 0.0;
            }

            if ((BeerName.length() > 0) && (Bar.length() > 0) && (BeerPriceStr.length() > 0)) {
                abeer.name = BeerName;
                abeer.bar = Bar;
                abeer.price = BeerPrice;
                abeer.lat = aDouble;
                abeer.lng = bDouble;
                abeer.address = newaddress;


                //updates beer
                Base.app.dbManager.update(abeer);

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }

            } else
                Toast.makeText(getActivity(), "You need to enter all required fields before updating a beer", Toast.LENGTH_SHORT).show();
        }
    }

}
