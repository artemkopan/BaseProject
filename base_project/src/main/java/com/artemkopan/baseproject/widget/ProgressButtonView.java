package com.artemkopan.baseproject.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Property;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.ExtraUtils;
import com.artemkopan.baseproject.utils.animations.AnimUtils;
import com.artemkopan.baseproject.widget.drawable.CircularProgressDrawable;

public class ProgressButtonView extends AppCompatButton {

    private CircularProgressDrawable mProgressDrawable;
    private Rect mBackgroundBounds, mProgressBounds;
    private ObjectAnimator mAnimator;
    private int mProgressSize, mProgressPadding;
    private boolean mShowProgress;
    private Property<ProgressButtonView, Float> mBackgroundProperty =
            new Property<ProgressButtonView, Float>(Float.class, "background") {
                @Override
                public Float get(ProgressButtonView object) {
                    return null;
                }

                @Override
                public void set(ProgressButtonView object, Float value) {
                    object.animate(value);
                }
            };

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
        int borderWidth = getResources().getDimensionPixelSize(R.dimen.base_progress_border_width);

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

        mProgressBounds = mProgressDrawable.getBounds();

    }

    public void showProgress(boolean show) {
        mShowProgress = show;

        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(this, mBackgroundProperty, 0, 1);
            mAnimator.setDuration(AnimUtils.VERY_FAST_DURATION);
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mShowProgress) {
                        mProgressDrawable.setAlpha(255);
                        mProgressDrawable.start();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    mProgressDrawable.setAlpha(0);
                    if (mShowProgress && getBackground() != null) {
                        mBackgroundBounds = new Rect(getBackground().getBounds());
                    } else if (!mShowProgress) {
                        mProgressDrawable.stop();
                    }
                }
            });
        }

        setEnabled(!show);
        mAnimator.start();
    }

    public boolean isShowProgress() {
        return mShowProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowProgress && mProgressDrawable.getAlpha() == 255) {
            mProgressDrawable.draw(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mProgressDrawable || super.verifyDrawable(who);
    }

    private void animate(float percentage) {
        Rect from;
        Rect to;
        int alpha;

        if (mShowProgress) {
            from = mBackgroundBounds;
            to = mProgressBounds;
            alpha = (int) ExtraUtils.calculateValue(percentage, 255, 0);
        } else {
            to = mBackgroundBounds;
            from = mProgressBounds;
            alpha = (int) ExtraUtils.calculateValue(percentage, 0, 255);
        }
        getBackground().setAlpha(alpha);
        getBackground().setBounds((int) ExtraUtils.calculateValue(percentage, from.left, to.left),
                                  (int) ExtraUtils.calculateValue(percentage, from.top, to.top),
                                  (int) ExtraUtils.calculateValue(percentage, from.right, to.right),
                                  (int) ExtraUtils.calculateValue(percentage, from.bottom, to.bottom));
    }

}
