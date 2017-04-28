package com.artemkopan.baseproject.sample;

import android.location.Address;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.artemkopan.baseproject.utils.ExtraUtils;

import java.util.Collection;

/**
 * Created by Artem Kopan for jabrool
 * 27.01.17
 */

public class PlacesAutoCompleteAdapter extends ArrayAdapter<Address> {

    private final Filter NO_FILTER = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            filterResults.count = getCount();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return super.convertResultToString(ExtraUtils.getCompleteAddressLine((Address) resultValue));
        }
    };

    PlacesAutoCompleteAdapter(FragmentActivity activity) {
        super(activity, android.R.layout.simple_list_item_1);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return NO_FILTER;
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void addAll(@NonNull Collection<? extends Address> collection) {
        super.addAll(collection);
    }
}
