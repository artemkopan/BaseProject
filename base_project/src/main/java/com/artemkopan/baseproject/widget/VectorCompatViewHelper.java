package com.artemkopan.baseproject.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.artemkopan.baseproject.R;

import static android.support.v4.widget.TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds;

/**
 * Created by Artem Kopan for jabrool
 * 30.12.16
 */

final class VectorCompatViewHelper {

    private static final int NO_VALUE = -1;

    static void loadFromAttributes(TextView textView, AttributeSet attrs) {

        if (textView == null || attrs == null) return;

        Context context = textView.getContext();
        Resources res = textView.getResources();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VectorCompatTextView);
        try {

            Drawable drawableStart = null;
            Drawable drawableEnd = null;
            Drawable drawableTop = null;
            Drawable drawableBottom = null;

            int drawableStartId = ta.getResourceId(R.styleable.VectorCompatTextView_drawableStartCompat, NO_VALUE);
            if (drawableStartId != NO_VALUE) {
                drawableStart = VectorDrawableCompat.create(res, drawableStartId, context.getTheme());
            }

            int drawableTopId = ta.getResourceId(R.styleable.VectorCompatTextView_drawableTopCompat, NO_VALUE);
            if (drawableTopId != NO_VALUE) {
                drawableTop = VectorDrawableCompat.create(res, drawableTopId, context.getTheme());
            }

            int drawableEndId = ta.getResourceId(R.styleable.VectorCompatTextView_drawableEndCompat, NO_VALUE);
            if (drawableEndId != NO_VALUE) {
                drawableEnd = VectorDrawableCompat.create(res, drawableEndId, context.getTheme());
            }

            int drawableBottomId = ta.getResourceId(R.styleable.VectorCompatTextView_drawableBottomCompat, NO_VALUE);
            if (drawableBottomId != NO_VALUE) {
                drawableBottom = VectorDrawableCompat.create(res, drawableBottomId, context.getTheme());
            }

            setCompoundDrawablesRelativeWithIntrinsicBounds(textView,
                                                            drawableStart,
                                                            drawableTop,
                                                            drawableEnd,
                                                            drawableBottom);
        } finally {
            ta.recycle();
        }
    }

}
