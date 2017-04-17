package com.artemkopan.base_widget.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.artemkopan.base_widget.R;
import com.artemkopan.base_widget.drawable.CircularProgressDrawable;

public class CircularProgressView extends View {

    private CircularProgressDrawable drawable;
    private boolean heightAccent = false;

    public CircularProgressView(Context context) {
        this(context, null, 0);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int color = Color.BLACK;
        int borderWidth = -1;

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs,
                                                                   R.styleable.CircularProgressView);
            try {
                color = array.getColor(R.styleable.CircularProgressView_cpv_progressColor, color);
                borderWidth = array.getDimensionPixelSize(
                        R.styleable.CircularProgressView_cpv_border_width,
                        getContext().getResources().getDimensionPixelSize(R.dimen.base_progress_border_width));
                heightAccent = array.getBoolean(R.styleable.CircularProgressView_cpv_height_accent,
                                                heightAccent);
            } finally {
                array.recycle();
            }
        }

        drawable = new CircularProgressDrawable(color, borderWidth);
        drawable.setCallback(this);

        if (getVisibility() == VISIBLE) {
            drawable.start();
        }
    }

    public void setProgressColor(@ColorInt int color) {
        if (drawable != null) {
            drawable.setColor(color);
        }
    }

    public void setBorderWidth(int borderWidth) {
        if (drawable != null) {
            drawable.setStrokeWidth(borderWidth);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (drawable == null) {
            return;
        }
        if (visibility == VISIBLE) {
            drawable.start();
        } else {
            drawable.stop();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (heightAccent) {
            drawable.setBounds(getPaddingLeft(), getPaddingTop(), h - getPaddingRight(), h - getPaddingBottom());
        } else {
            drawable.setBounds(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), w - getPaddingBottom());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawable.draw(canvas);
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == drawable || super.verifyDrawable(who);
    }
}
