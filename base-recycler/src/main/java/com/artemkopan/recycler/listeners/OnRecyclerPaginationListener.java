package com.artemkopan.recycler.listeners;

import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OnRecyclerPaginationListener extends RecyclerView.OnScrollListener {

    public static final int VERTICAL = 0, HORIZONTAL = 1;

    protected RecyclerView.LayoutManager layoutManager;
    protected OnRecyclerPaginationResult onRecyclerPaginationResult;
    protected boolean loading = true;
    protected int type = VERTICAL;
    private int offset;

    public OnRecyclerPaginationListener(RecyclerView.LayoutManager layoutManager,
                                        @TypePagination int type,
                                        OnRecyclerPaginationResult onRecyclerPaginationResult) {
        this.layoutManager = layoutManager;
        this.type = type;
        this.onRecyclerPaginationResult = onRecyclerPaginationResult;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int value = type == VERTICAL ? dy : dx;

        if (value != 0 && loading) //check for scroll down
        {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisibleItems;
            if (layoutManager instanceof LinearLayoutManager) {
                pastVisibleItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                pastVisibleItems = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else {
                throw new UnsupportedOperationException("StaggerGridLayoutManager not support");
            }

            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loading = false;
                onRecyclerPaginationResult.onRecyclePaginationNextPage();
            }
        }
    }

    public void enablePagination() {
        loading = true;
    }

    public void disablePagination() {
        loading = false;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void incOffset(int value, boolean enablePagination) {
        incOffset(value);
        loading = enablePagination;
    }

    /**
     * Increment offset
     *
     * @param value Value for inc
     */
    public void incOffset(int value) {
        this.offset += value;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public interface OnRecyclerPaginationResult {

        void onRecyclePaginationNextPage();
    }

    @IntDef({VERTICAL, HORIZONTAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TypePagination {
    }
}