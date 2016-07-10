package com.artemkopan.baseproject.recycler.listeners;

import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class OnRecyclerPaginationListener extends RecyclerView.OnScrollListener {

    public static final int VERTICAL = 0, HORIZONTAL = 1;

    protected LinearLayoutManager mLayoutManager;
    protected OnRecyclerPaginationResult onRecyclerPaginationResult;
    protected boolean loading = true;
    protected int type = VERTICAL;


    public OnRecyclerPaginationListener(LinearLayoutManager mLayoutManager,
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

        if (value > 0 && loading) //check for scroll down
        {
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loading = false;
                onRecyclerPaginationResult.onRecyclePaginationNextPage();
            }
        }
    }

    public void enablePagionation() {
        loading = true;
    }

    public void disablePagination() {
        loading = false;
    }

    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public interface OnRecyclerPaginationResult {
        void onRecyclePaginationNextPage();
    }


    @IntDef({VERTICAL, HORIZONTAL})
    public @interface TypePagination {
    }
}