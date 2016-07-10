package com.artemkopan.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.drawable.CircularProgressDrawable;


public class CircularProgressView extends View {
    private CircularProgressDrawable mDrawable;

    public CircularProgressView(Context context) {
        this(context, null);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircularProgressView,
                0, 0);
        int color = attributes.getColor(R.styleable.CircularProgressView_cpv_progressColor, -1);
        int borderWidth = attributes.getDimensionPixelSize(R.styleable.CircularProgressView_cpv_border_width, -1);
        TypedValue typedValue = new TypedValue();
        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        try {
            if (color == -1)
                color = a.getColor(0, 0);
            if (borderWidth == -1)
                borderWidth = 5;
        } finally {
            a.recycle();
        }

        mDrawable = new CircularProgressDrawable(color, borderWidth);
        mDrawable.setCallback(this);
        mDrawable.start();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            mDrawable.start();
        } else {
            mDrawable.stop();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawable.setBounds(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mDrawable.draw(canvas);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mDrawable || super.verifyDrawable(who);
    }
}
