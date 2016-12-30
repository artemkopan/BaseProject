package com.artemkopan.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

import com.artemkopan.baseproject.R;

/**
 * Created by Artem Kopan for jabrool
 * 06.12.16
 */

final class AspectViewHelper {

    private int aspectWidth = 1;
    private int aspectHeight = 1;
    private boolean aspectByHeight = false;

    void loadFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = context.getResources().obtainAttributes(attrs, R.styleable.AspectHelper);
        try {
            aspectWidth = a.getInteger(R.styleable.AspectHelper_aspect_width, 1);
            aspectHeight = a.getInteger(R.styleable.AspectHelper_aspect_height, 1);
            aspectByHeight = a.getBoolean(R.styleable.AspectHelper_aspect_by_height, false);
        } finally {
            a.recycle();
        }
    }

    Point calculateNewMeasure(View view) {
        int newSpecWidth;
        int newSpecHeight;

        if (aspectByHeight) {
            newSpecHeight = MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), MeasureSpec.EXACTLY);
            int newW = Math.round(((float) view.getMeasuredHeight()) * aspectWidth / aspectHeight);
            newSpecWidth = MeasureSpec.makeMeasureSpec(newW, MeasureSpec.EXACTLY);
        } else {
            newSpecWidth = MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), MeasureSpec.EXACTLY);
            int newH = Math.round(((float) view.getMeasuredWidth()) * aspectHeight / aspectWidth);
            newSpecHeight = MeasureSpec.makeMeasureSpec(newH, MeasureSpec.EXACTLY);
        }

        return new Point(newSpecWidth, newSpecHeight);
    }
}
