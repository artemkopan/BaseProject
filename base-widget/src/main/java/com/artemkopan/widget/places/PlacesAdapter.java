package com.artemkopan.widget.places;

import android.content.Context;
import android.location.Address;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Artem Kopan for BaseProject
 * 18.07.2017
 */

public class PlacesAdapter extends ArrayAdapter<Address> {

    @LayoutRes
    private final int resource;

    PlacesAdapter(Context context, @LayoutRes int resource, List<Address> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    protected View createView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = (TextView) LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        } else {
            view = (TextView) convertView;
        }

        view.setText(PlacesAutoCompleteUtils.getCompleteAddressLine(getItem(position)));

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return NO_FILTER;
    }

    //==============================================================================================
    // Filter
    //==============================================================================================
    //region methods

    public static final Filter NO_FILTER = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return super.convertResultToString(PlacesAutoCompleteUtils.getCompleteAddressLine((Address) resultValue));
        }
    };

    //endregion
}
