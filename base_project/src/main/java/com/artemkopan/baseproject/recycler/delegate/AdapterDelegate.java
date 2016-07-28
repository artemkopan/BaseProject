package com.artemkopan.baseproject.recycler.delegate;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * This delegate provide method to hook in this delegate to {@link RecyclerView.Adapter} lifecycle.
 * This "hook in" mechanism is provided by {@link AdapterDelegatesManager} and that is the
 * component
 * you have to use.
 *
 * @param <T> The type of the data source
 * @author Hannes Dorfmann
 * @since 1.0
 */
public interface AdapterDelegate<T> {

    /**
     * Get the view type integer. Must be unique within every Adapter
     *
     * @return the integer representing the view type
     */
    int getItemViewType();

    /**
     * Called to determine whether this AdapterDelegate is the responsible for the given data
     * element.
     *
     * @param items The data source of the Adapter
     * @param position The position in the datasource
     * @return true, if this item is responsible,  otherwise false
     */
    boolean isForViewType(@NonNull T items, int position);

    /**
     * Creates the  {@link RecyclerView.ViewHolder} for the given data source item
     *
     * @param parent The ViewGroup parent of the given datasource
     * @return The new instantiated {@link RecyclerView.ViewHolder}
     */
    @NonNull RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

    /**
     * Called to bind the {@link RecyclerView.ViewHolder} to the item of the datas source set
     *
     * @param items The data source
     * @param position The position in the datasource
     * @param holder The {@link RecyclerView.ViewHolder} to bind
     */
    void onBindViewHolder(@NonNull T items, int position, @NonNull RecyclerView.ViewHolder holder);
}
