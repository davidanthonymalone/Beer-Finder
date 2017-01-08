package dm.pivofinder.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import dm.pivofinder.activities.Base;
import dm.pivofinder.adapters.BeerFilter;
import dm.pivofinder.adapters.BListAdapter;
import dm.pivofinder.R;
import dm.pivofinder.models.Beer;

public class BeerFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemSelectedListener, View.OnClickListener, TextWatcher {
    //protected         Base                activity;
    public static BListAdapter listAdapter;
    protected dm.pivofinder.adapters.BeerFilter BeerFilter;
    public ListView bListView;
    BeerFilter beerFilter;


    public BeerFragment() {
        // Required empty public constructor
    }


    public static BeerFragment newInstance() {
        BeerFragment fragment = new BeerFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //this.activity = (Base) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = null;


        v = inflater.inflate(R.layout.fragment_beer, container, false);
        //references the widgets in xml
        EditText nameText = (EditText) v.findViewById(R.id.searchForBeer);
        nameText.addTextChangedListener(this);
        bListView = (ListView) v.findViewById(R.id.beerList);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


        //sets addapter with data from the beer database
        listAdapter = new BListAdapter(getActivity(), this, Base.app.dbManager.getAll());
        BeerFilter = new BeerFilter(Base.app.dbManager.getAll(), "all", listAdapter);
        bListView.setAdapter(listAdapter);
        bListView.setOnItemClickListener(this);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Beer) {
            beerDelete((Beer) view.getTag());
        }
    }

    public void beerDelete(final Beer beer) {
        String stringName = beer.name;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to Delete the \'beer\' " + stringName + "?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Base.app.dbManager.delete(beer.beerId); // remove from our list
                listAdapter.BeerList.remove(beer); // update adapters data
                listAdapter.notifyDataSetChanged(); // refresh adapter
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Bundle activityInfo = new Bundle();
        activityInfo.putInt("BeerID", view.getId());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to update this beer?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = UpdateFragment.newInstance(activityInfo);
                ft.replace(R.id.homeFrame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        BeerFilter.filter(charSequence);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private Beer getBeerObject(int id) {

        for (Beer beer : Base.app.dbManager.getAll())
            if (beer.beerId == id)
                return beer;

        return null;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

