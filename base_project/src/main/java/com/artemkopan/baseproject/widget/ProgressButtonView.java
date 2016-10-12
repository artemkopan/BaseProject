package com.artemkopan.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.widget.drawable.CircularProgressDrawable;

public class ProgressButtonView extends AppCompatButton {

    private CircularProgressDrawable mProgressDrawable;
    private Drawable mBackgroundDrawable;
    private int mProgressSize, mProgressPadding;
    private boolean mShowProgress;

    public ProgressButtonView(Context context) {
        this(context, null);
    }

    public ProgressButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public ProgressButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        int color = Color.BLACK;
        int borderWidth = 4;

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressButtonView);

            try {
                color = array.getColor(R.styleable.ProgressButtonView_pbv_progressColor, color);
                borderWidth = array.getDimensionPixelSize(
                        R.styleable.ProgressButtonView_pbv_progressBorderWidth,
                        getResources().getDimensionPixelSize(R.dimen.base_progress_border_width));

                mProgressSize = array.getDimensionPixelSize(R.styleable.ProgressButtonView_pbv_progressSize,
                        mProgressSize);
                mProgressPadding = array.getDimensionPixelSize(
                        R.styleable.ProgressButtonView_pbv_progressPadding,
                        mProgressPadding);
            } finally {
                array.recycle();
            }
        }

        mProgressDrawable = new CircularProgressDrawable(color, borderWidth);
        mProgressDrawable.setCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mProgressSize == 0) mProgressSize = w > h ? h : w;

        int sizeHalfProgress = (mProgressSize - mProgressPadding) / 2;

        mProgressDrawable.setBounds(
                w / 2 - sizeHalfProgress, h / 2 - sizeHalfProgress,
                w / 2 + sizeHalfProgress, h / 2 + sizeHalfProgress);

    }

    public void showProgress(boolean show) {
        mShowProgress = show;

        if (show) {
            mBackgroundDrawable = getBackground();
            mProgressDrawable.start();
            setBackgroundDrawable(null);
            setEnabled(false);
        } else {
            mProgressDrawable.stop();
            setBackgroundDrawable(mBackgroundDrawable);
            setEnabled(true);
        }

        invalidate();
    }

    public boolean isShowProgress() {
        return mShowProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowProgress) {
            mProgressDrawable.draw(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mProgressDrawable || super.verifyDrawable(who);
    }
}
