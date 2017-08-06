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
    private OnItemClickListener<M> onItemClickListener;
    private FooterAdapterDelegate footerDelegate = new FooterAdapterDelegate();
    private HeaderAdapterDelegate headerDelegate = new HeaderAdapterDelegate();

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (headerDelegate.isHeaderPosition(position))
            onBindHeaderHolder(holder);
        else if (footerDelegate.isFooterPosition(position,  headerDelegate.getItemCount(getListSize())))
            onBindFooterHolder(holder);
        else
            onBindViewHolder(holder, getItemByPos(position), position);
    }

    public abstract void onBindViewHolder(VH holder, M model, int position);

    public void onBindHeaderHolder(VH holder) {

    }

    public void onBindFooterHolder(VH holder) {

    }

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
        return footerDelegate.getItemCount(headerDelegate.getItemCount(getListSize()));
    }

    public M getItemByPos(int position) {
        return getListItemByPos(headerDelegate.getItemPosition(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (headerDelegate.isHeaderPosition(position)) {
            return HEADER;
        } else if (footerDelegate.isFooterPosition(position, headerDelegate.getItemCount(getListSize()))) {
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
        return headerDelegate.isShowHeader();
    }

    public void showHeader(boolean show) {
        headerDelegate.showHeader(this, show);
    }

    public int getHeaderOffset() {
        return headerDelegate.getItemPosition(0);
    }

    public boolean isShowFooter() {
        return footerDelegate.isShowFooter();
    }

    public void showFooter(boolean show) {
        footerDelegate.showFooter(this, show);
    }

}
