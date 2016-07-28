package com.artemkopan.baseproject.recycler.delegate;

public abstract class AbsAdapterDelegate<T> implements AdapterDelegate<T> {
    /**
     * The item view type
     */
    protected int viewType;

    public AbsAdapterDelegate(int viewType) {
        this.viewType = viewType;
    }

    @Override public int getItemViewType() {
        return viewType;
    }
}