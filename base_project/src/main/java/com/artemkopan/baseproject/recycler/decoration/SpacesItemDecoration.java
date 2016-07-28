package com.artemkopan.baseproject.recycler.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private boolean mUseFirstTopSpace = true;

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    public SpacesItemDecoration(int space, boolean useFirstTopSpace) {
        mSpace = space;
        mUseFirstTopSpace = useFirstTopSpace;
    }

    public void setUseFirstTopSpace(boolean useFirstTopSpace) {
        mUseFirstTopSpace = useFirstTopSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;

        // Add top margin only for the first item to avoid double mSpace between items
        if (parent.getChildLayoutPosition(view) == 0 && mUseFirstTopSpace) {
            outRect.top = mSpace;
        }
    }
}
