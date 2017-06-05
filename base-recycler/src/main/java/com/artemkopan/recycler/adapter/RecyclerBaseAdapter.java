package com.artemkopan.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.artemkopan.recycler.holder.BaseHolder;
import com.artemkopan.recycler.listeners.OnItemClickListener;

/**
 * Created by Artem Kopan for jabrool
 * 25.02.2017
 */

public abstract class RecyclerBaseAdapter<M, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int HEADER = 98, FOOTER = 99;
    private boolean isShowHeader = false, isShowFooter = false;
    private OnItemClickListener<M> onItemClickListener;

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, getItemByPos(position), position);
    }

    public abstract void onBindViewHolder(VH holder, M model, int position);

    protected abstract M getListItemByPos(int pos);

    protected abstract int getListSize();

    public boolean isEmpty() {
        return getListSize() == 0;
    }

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
        if (holder != null && holder instanceof BaseHolder) {
            ((BaseHolder) holder).clear();
        }
    }

    @Override
    public int getItemCount() {
        return getListSize() + getPosOffset() + (isShowFooter ? 1 : 0);
    }

    public M getItemByPos(int position) {
        return getListItemByPos(position - getPosOffset());
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowHeader && position == 0) {
            return HEADER;
        } else if (isShowFooter && getItemCount() - 1 == position) {
            return FOOTER;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected void callOnItemClick(ViewHolder holder, View view, View... transactionViews) {
        int position = holder.getAdapterPosition();
        callOnItemClick(view, position, getItemByPos(position), transactionViews);
    }

    protected void callOnItemClick(View view, int pos, M item, View... transactionViews) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClickListener(view, pos, item, transactionViews);
        }
    }

    public boolean isShowHeader() {
        return isShowHeader;
    }

    public void showHeader(boolean show) {
        if (isShowHeader == show) return;
        isShowHeader = show;
        if (show) notifyItemInserted(0);
        else notifyItemRemoved(0);
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    public void showFooter(boolean show) {
        if (isShowFooter == show) return;
        isShowFooter = show;
        if (show) notifyItemInserted(getItemCount());
        else notifyItemRemoved(getItemCount() + 1); //remove with footer
    }

    public int getPosOffset() {
        return isShowHeader ? 1 : 0;
    }

}
