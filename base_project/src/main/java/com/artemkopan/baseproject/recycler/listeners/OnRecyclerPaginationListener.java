package com.artemkopan.baseproject.recycler.listeners;

import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OnRecyclerPaginationListener extends RecyclerView.OnScrollListener {

    public static final int VERTICAL = 0, HORIZONTAL = 1;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected OnRecyclerPaginationResult onRecyclerPaginationResult;
    protected boolean loading = true;
    protected int type = VERTICAL;
    private int offset;

    public OnRecyclerPaginationListener(RecyclerView.LayoutManager mLayoutManager,
                                        @TypePagination int type,
                                        OnRecyclerPaginationResult onRecyclerPaginationResult) {
        this.mLayoutManager = mLayoutManager;
        this.type = type;
        this.onRecyclerPaginationResult = onRecyclerPaginationResult;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int value = type == VERTICAL ? dy : dx;

        if (value != 0 && loading) //check for scroll down
        {
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int pastVisibleItems;
            if (mLayoutManager instanceof LinearLayoutManager) {
                pastVisibleItems = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else if (mLayoutManager instanceof GridLayoutManager) {
                pastVisibleItems = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
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

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    /**
     * Increment offset
     * @param value Value for inc
     */
    public void incOffset(int value) {
        this.offset += value;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public interface OnRecyclerPaginationResult {

        void onRecyclePaginationNextPage();
    }

    @IntDef({VERTICAL, HORIZONTAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TypePagination {
    }
}