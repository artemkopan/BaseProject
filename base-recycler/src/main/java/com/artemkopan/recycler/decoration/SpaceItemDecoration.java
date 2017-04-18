package com.artemkopan.recycler.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean isHorizontal = true;
    private boolean addFirst = true;
    private boolean addLast = true;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, boolean addFirst, boolean addLast) {
        this.space = space;
        this.addFirst = addFirst;
        this.addLast = addLast;
    }

    public SpaceItemDecoration setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
        return this;
    }

    public SpaceItemDecoration setAddFirst(boolean addFirst) {
        this.addFirst = addFirst;
        return this;
    }

    public SpaceItemDecoration setAddLast(boolean addLast) {
        this.addLast = addLast;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {

        if (addFirst && parent.getChildLayoutPosition(view) == 0) {
            if (isHorizontal) {
                outRect.left = space;
            } else {
                outRect.top = space;
            }
        }

        if (!addLast || parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1) {
            if (isHorizontal) {
                outRect.right = space;
            } else {
                outRect.bottom = space;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        super.onDraw(c, parent, state);
    }
}
