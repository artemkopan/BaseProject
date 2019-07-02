package com.artemkopan.recycler.listeners;

import androidx.recyclerview.widget.RecyclerView;

public abstract class TopScrollListener extends RecyclerView.OnScrollListener {

    private boolean mIsTop = true;

    public TopScrollListener() {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int topRowVerticalPosition =
                (recyclerView == null || recyclerView.getChildCount() == 0) ?
                        0 :
                        recyclerView.getChildAt(0).getTop();

        boolean isTop = topRowVerticalPosition >= 0;

        if (mIsTop != isTop) {
            mIsTop = isTop;
            onChangeTop(mIsTop);
        }
    }

    public boolean isTop() {
        return mIsTop;
    }

    public abstract void onChangeTop(boolean isTop);
}
