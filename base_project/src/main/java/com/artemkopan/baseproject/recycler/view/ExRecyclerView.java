package com.artemkopan.baseproject.recycler.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewTreeObserver;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.widget.drawable.CircularProgressDrawable;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.recycler.listeners.OnRecyclerPaginationListener;


public class ExRecyclerView extends RecyclerView {

    private static final String TAG = "ExRecyclerView";
    private StaticLayout mTextLayout;
    private TextPaint mTextPaint;
    private CircularProgressDrawable mProgressDrawable;
    private int mProgressSize;
    private int mTextPadding;
    private boolean mDrawText, mDrawProgress;
    private OnRecyclerPaginationListener mPaginationListener;

    public ExRecyclerView(Context context) {
        this(context, null, 0);
    }

    public ExRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        int borderWidth = -1, progressColor = -1;
        int textSize = -1, textColor = Color.BLACK;
        String textDefault = null;

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExRecyclerView);
            try {
                mProgressSize = array.getDimensionPixelSize(R.styleable.ExRecyclerView_erv_progressSize,
                        context.getResources().getDimensionPixelSize(R.dimen.base_progress_size));
                borderWidth = array.getDimensionPixelSize(R.styleable.ExRecyclerView_erv_progressBorderWidth,
                        context.getResources().getDimensionPixelSize(R.dimen.base_progress_border_width));
                progressColor = array.getColor(R.styleable.ExRecyclerView_erv_progressColor, -1);

                textSize = array.getDimensionPixelSize(R.styleable.ExRecyclerView_erv_textSize, textSize);
                textColor = array.getColor(R.styleable.ExRecyclerView_erv_textColor, textColor);
                textDefault = array.getString(R.styleable.ExRecyclerView_erv_textDefault);
                mTextPadding = array.getDimensionPixelSize(R.styleable.ExRecyclerView_erv_textPadding, mTextPadding);
            } finally {
                array.recycle();
            }
        } else {
            mProgressSize = context.getResources().getDimensionPixelSize(R.dimen.base_progress_size);
            borderWidth = context.getResources().getDimensionPixelSize(R.dimen.base_progress_border_width);
        }

        if (progressColor == -1) {
            TypedValue typedValue = new TypedValue();
            TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
            try {
                progressColor = a.getColor(0, 0);
            } finally {
                a.recycle();
            }
        }

        if (textSize == -1) {
            textSize = getContext().getResources().getDimensionPixelSize(R.dimen.base_recycler_text_size);
        }
        if (mTextPadding == 0) {
            mTextPadding = context.getResources().getDimensionPixelSize(R.dimen.base_recycler_text_padding);
        }

        if (TextUtils.isEmpty(textDefault)) {
            textDefault = context.getString(R.string.base_info_items_not_found);
        }

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);

        mProgressDrawable = new CircularProgressDrawable(progressColor, borderWidth);
        mProgressDrawable.setGravity(Gravity.CENTER);
        mProgressDrawable.setCallback(this);

        setText(textDefault);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mProgressDrawable.setBounds(0, 0, mProgressSize, mProgressSize);
    }

    public void addPaginationListener(OnRecyclerPaginationListener scrollListener) {
        mPaginationListener = scrollListener;
        addOnScrollListener(scrollListener);
    }

    public void setPaginationState(boolean isEnable) {
        if (isEnable) {
            enablePagination();
        } else {
            disablePagination();
        }
    }

    public void setProgressColor(@ColorInt int color) {
        mProgressDrawable.setColor(color);
    }

    public void enablePagination() {
        if (mPaginationListener != null) {
            mPaginationListener.enablePagionation();
        }
    }

    public void disablePagination() {
        if (mPaginationListener != null) {
            mPaginationListener.disablePagination();
        }
    }

    public void setText(@StringRes int textRes, Object... arguments) {
        setText(getContext().getString(textRes, arguments));
    }

    public void setTextPadding(int textPadding) {
        mTextPadding = textPadding;
    }

    public void setText(@StringRes int textRes) {
        setText(getContext().getString(textRes));
    }

    public void setText(final String text) {
        Log.i("setText: " + getWidth());
        if (getWidth() <= 0) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    setText(text);
                    return false;
                }
            });
        } else {
            mTextLayout = new StaticLayout(
                    text,
                    mTextPaint,
                    getWidth() - mTextPadding * 2,
                    Layout.Alignment.ALIGN_CENTER,
                    1,
                    0,
                    true);
        }
    }

    public void showText() {
        mDrawText = true;
        hideProgress();
    }

    public void hideText() {
        mDrawText = false;
        invalidate();
    }

    public void showProgress() {
        mDrawProgress = true;
        mProgressDrawable.start();
        hideText();
    }

    public void hideProgress() {
        mDrawProgress = false;
        mProgressDrawable.stop();
        invalidate();
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if (mDrawProgress) {
            mProgressDrawable.draw(c);
        }
        if (mDrawText && mTextLayout != null) {
            c.save();

            c.translate(
                    (c.getWidth() / 2) - (mTextLayout.getWidth() / 2),
                    (c.getHeight() / 2) - ((mTextLayout.getHeight() / 2)));

            mTextLayout.draw(c);

            c.restore();
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mProgressDrawable || super.verifyDrawable(who);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        // check if scrolling up
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return !original && getChildAt(0) != null && getChildAt(0).getTop() < 0 || original;
        }
        return super.canScrollVertically(direction);

    }
}
