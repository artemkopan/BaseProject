package com.artemkopan.recycler.decoration;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.State;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private boolean isHorizontal = false;
    private boolean addFirst = true;
    private boolean addLast = true;

    public SpaceItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    public SpaceItemDecoration(int spacing, boolean isHorizontal) {
        this.spacing = spacing;
        this.isHorizontal = isHorizontal;
    }

    public SpaceItemDecoration(int spacing, boolean isHorizontal, boolean addFirst) {
        this.spacing = spacing;
        this.isHorizontal = isHorizontal;
        this.addFirst = addFirst;
    }

    public SpaceItemDecoration(int spacing, boolean isHorizontal, boolean addFirst, boolean addLast) {
        this.spacing = spacing;
        this.isHorizontal = isHorizontal;
        this.addFirst = addFirst;
        this.addLast = addLast;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int position = parent.getChildAdapterPosition(view);

        if (position == 0 && addFirst) {
            if (isHorizontal) {
                outRect.left = spacing;
            } else {
                outRect.top = spacing;
            }
        }

        if (!addLast) {
            if (position != parent.getAdapter().getItemCount() - 1) {
                if (isHorizontal) {
                    outRect.right = spacing;
                } else {
                    outRect.bottom = spacing;
                }
            }
        } else {
            if (isHorizontal) {
                outRect.right = spacing;
            } else {
                outRect.bottom = spacing;
            }
        }
    }
}
