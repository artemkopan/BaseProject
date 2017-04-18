package com.artemkopan.widget.vector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.artemkopan.widget.R;

import static android.support.v4.content.ContextCompat.getDrawable;

/**
 * Created by Artem Kopan for jabrool
 * 30.12.16
 */

public final class VectorCompatViewHelper {

    private static final int NO_VALUE = -1;

    public static void loadFromAttributes(TextView textView, AttributeSet attrs) {

        if (textView == null || attrs == null) return;

        Context context = textView.getContext();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VectorCompatTextView);
        try {

            Drawable drawableStart = null;
            Drawable drawableEnd = null;
            Drawable drawableTop = null;
            Drawable drawableBottom = null;
            int width = NO_VALUE;
            int height = NO_VALUE;

            int drawableStartId = ta.getResourceId(R.styleable.VectorCompatTextView_drawableStartCompat, NO_VALUE);
            if (drawableStartId != NO_VALUE) {
                drawableStart = getDrawable(context, drawableStartId);
            }

            int drawableTopId = ta.getResourceId(R.styleable.VectorCompatTextView_drawableTopCompat, NO_VALUE);
            if (drawableTopId != NO_VALUE) {
                drawableTop = getDrawable(context, drawableTopId);
            }

            int drawableEndId = ta.getResourceId(R.styleable.VectorCompatTextView_drawableEndCompat, NO_VALUE);
            if (drawableEndId != NO_VALUE) {
                drawableEnd = getDrawable(context, drawableEndId);
            }

            int drawableBottomId = ta.getResourceId(R.styleable.VectorCompatTextView_drawableBottomCompat, NO_VALUE);
            if (drawableBottomId != NO_VALUE) {
                drawableBottom = getDrawable(context, drawableBottomId);
            }

            if (drawableStart != null || drawableEnd != null || drawableTop != null || drawableBottom != null) {
                width = ta.getDimensionPixelSize(R.styleable.VectorCompatTextView_drawableWidth, width);
                height = ta.getDimensionPixelSize(R.styleable.VectorCompatTextView_drawableHeight, height);

                if (width != NO_VALUE && height != NO_VALUE) {
                    setDrawableSize(drawableStart, width, height);
                    setDrawableSize(drawableEnd, width, height);
                    setDrawableSize(drawableTop, width, height);
                    setDrawableSize(drawableBottom, width, height);

                    TextViewCompat.setCompoundDrawablesRelative(textView,
                                                                drawableStart,
                                                                drawableTop,
                                                                drawableEnd,
                                                                drawableBottom);
                } else {
                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(textView,
                                                                                   drawableStart,
                                                                                   drawableTop,
                                                                                   drawableEnd,
                                                                                   drawableBottom);
                }
            }
        } finally {
            ta.recycle();
        }
    }

    private static void setDrawableSize(Drawable drawable, int width, int height) {
        if (drawable == null) return;
        Rect rect = drawable.getBounds();
        drawable.setBounds(rect.left, rect.top, width, height);
    }

}
