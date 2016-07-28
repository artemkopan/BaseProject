package com.artemkopan.baseproject.recycler.adapter;


import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artemkopan.baseproject.R;

public abstract class RecyclerPaginationAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerBaseAdapter<M, VH> {

    public final static int TYPE_PROGRESS = -1;

    @LayoutRes
    private int mDefaultProgressLayout = R.layout.base_item_progress;

    public void setDefaultProgressLayout(int defaultProgressLayout) {
        mDefaultProgressLayout = defaultProgressLayout;
    }

    @Override
    @SuppressWarnings("unchecked")
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PROGRESS) {
            return (VH) new ProgressViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(mDefaultProgressLayout, parent, false));
        } else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (!(holder instanceof ProgressViewHolder)) {
            onBindItemViewHolder(holder, position);
        }
    }

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(VH holder, int position);

    public void starProgress() {
        if (mList != null  && (mList.get(mList.size() - 1) != null)) {
            addItem(null);
        }
    }

    /**
     * You must call stop progress before set new data
     */
    public void stopProgress() {
        if (mList != null && mList.size() > 0 && (mList.get(mList.size() - 1) == null)) {
            removeItem(mList.size() - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList != null && mList.get(position) == null ? TYPE_PROGRESS : super.getItemViewType(position);
    }

    protected static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}


