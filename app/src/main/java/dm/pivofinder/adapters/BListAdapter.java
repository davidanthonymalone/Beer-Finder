package dm.pivofinder.adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import dm.pivofinder.R;
import dm.pivofinder.models.Beer;

public class BListAdapter extends ArrayAdapter<Beer> {
    private Context context;
    private OnClickListener deleteListener;
    public List<Beer> BeerList;

    public BListAdapter(Context context, OnClickListener deleteListener,
                        List<Beer> beerList) {
        super(context, R.layout.beer_row, beerList);

        this.context = context;
        this.deleteListener = deleteListener;
        this.BeerList = beerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BeerItem item = new BeerItem(context, parent, deleteListener,
                BeerList.get(position));
        return item.view;
    }

    @Override
    public int getCount() {
        return BeerList.size();
    }
    public List<Beer> getBeerList() {

        return this.BeerList;
    }
    @Override
    public Beer getItem(int position) {
        return BeerList.get(position);
    }
    @Override
    public long getItemId(int position) {

        return position;
    }
    @Override
    public int getPosition(Beer c) {

        return BeerList.indexOf(c);
    }
}
