package com.artemkopan.baseproject.recycler.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean addFirst = true;
    private boolean addLast = true;
    private boolean addL = true;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public SpacesItemDecoration(int space, boolean useFirstTopSpace) {
        space = space;
        addFirst = useFirstTopSpace;
    }

    public void setUseFirstTopSpace(boolean useFirstTopSpace) {
        addFirst = useFirstTopSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0 && addFirst) {
            outRect.top = space;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        super.onDraw(c, parent, state);
    }
}
