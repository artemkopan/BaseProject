package com.artemkopan.recycler.adapter;

import androidx.recyclerview.widget.SortedList;

/**
 * Adapted RecyclerSorted Callback for {@link RecyclerSortedAdapter}. You can safely use {@link RecyclerBaseAdapter#showHeader(boolean)}} or {@link RecyclerBaseAdapter#showFooter(boolean)}
 */
public abstract class RecyclerSortedCallback<T2> extends SortedList.Callback<T2> {

    private final RecyclerBaseAdapter adapter;

    public RecyclerSortedCallback(RecyclerBaseAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onInserted(int position, int count) {
        adapter.notifyItemRangeInserted(position + adapter.getHeaderOffset(), count);
    }

    @Override
    public void onRemoved(int position, int count) {
        adapter.notifyItemRangeRemoved(position + adapter.getHeaderOffset(), count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        int offset = adapter.getHeaderOffset();
        adapter.notifyItemMoved(fromPosition + offset, toPosition + offset);
    }

    @Override
    public void onChanged(int position, int count) {
        adapter.notifyItemRangeChanged(position + adapter.getHeaderOffset(), count);
    }

}
