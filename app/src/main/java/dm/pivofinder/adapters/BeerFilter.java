package dm.pivofinder.adapters;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import dm.pivofinder.fragments.BeerFragment;
import dm.pivofinder.models.Beer;

public class BeerFilter extends Filter {
    private List<Beer>          beerList;
    private String 				filterText;
    private BListAdapter        badapter;

    public BeerFilter(List<Beer> beerList, String filterText,
                      BListAdapter badapter) {
        super();
        this.beerList = beerList;
        this.filterText = filterText;
        this.badapter = badapter;
    }

    public void setFilter(String filterText) {
        this.filterText = filterText;
    }

    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();

        if (beerList == null) {
            beerList = new ArrayList<Beer>();
        }
        if (prefix == null || prefix.length() == 0) {
            List<Beer> newBeers = new ArrayList<Beer>();
            if (filterText.equals("all")) {
                results.values = beerList;
                results.count = beerList.size();
            }
        } else {
            String prefixString = prefix.toString().toLowerCase();
            final ArrayList<Beer> newBeers = new ArrayList<Beer>();

            for (Beer b : beerList) {
                final String beerName = b.name.toLowerCase();
                final String barName = b.bar.toLowerCase();

                if (beerName.contains(prefixString) || barName.contains(prefixString)) {
                    if (filterText.equals("all")) {
                        newBeers.add(b);
                    } else if (b.equals("all")) {
                        newBeers.add(b);
                    }}}
            results.values = newBeers;
            results.count = newBeers.size();
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence prefix, FilterResults results) {

        badapter.BeerList = (ArrayList<Beer>) results.values;
            BeerFragment frag = new BeerFragment();
        if (results.count >= 0)
            frag.listAdapter.notifyDataSetChanged();
        else {
            badapter.notifyDataSetInvalidated();
            badapter.BeerList = beerList;
        }
    }
}
