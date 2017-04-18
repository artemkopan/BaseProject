package com.artemkopan.recycler.adapter;

import android.support.v7.util.SortedList;

/**
 * Created by Artem Kopan for MyMoodAndMe
 * 29.03.17
 */

public abstract class RecyclerSortedCallback<T2> extends SortedList.Callback<T2> {

    private final RecyclerAdapter adapter;

    public RecyclerSortedCallback(RecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onInserted(int position, int count) {
        adapter.notifyItemRangeInserted(position + adapter.getPosOffset(), count);
    }

    @Override
    public void onRemoved(int position, int count) {
        adapter.notifyItemRangeRemoved(position + adapter.getPosOffset(), count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        int offset = adapter.getPosOffset();
        adapter.notifyItemMoved(fromPosition + offset, toPosition + offset);
    }

    @Override
    public void onChanged(int position, int count) {
        adapter.notifyItemRangeChanged(position + adapter.getPosOffset(), count);
    }

}
