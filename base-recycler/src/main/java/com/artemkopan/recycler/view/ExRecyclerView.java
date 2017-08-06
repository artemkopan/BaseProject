package com.artemkopan.recycler.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.artemkopan.recycler.R;
import com.artemkopan.widget.drawable.CircularProgressDrawable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ExRecyclerView extends RecyclerView {

    private static final int NO_VALUE = -1;
    private static final int BACKGROUND_DURATION = 2000;
    private static final String TAG = "ExRecyclerView";
    private StaticLayout textLayout;
    private TextPaint textPaint;
    private CircularProgressDrawable progressDrawable;
    private Drawable backgroundDrawable;
    private Disposable errorTimer;
    private CharSequence textCurrent;
    private int progressSize = NO_VALUE;
    private int textGravity = Gravity.CENTER;
    private Rect textMargin = new Rect();
    private int backgroundDuration = BACKGROUND_DURATION;
    private boolean drawText, drawProgress;

    public ExRecyclerView(Context context) {
        this(context, null, 0);
    }

    public ExRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        int borderWidth = NO_VALUE, progressColor = NO_VALUE;
        int textSize = NO_VALUE, textColor = Color.BLACK;

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExRecyclerView);
            try {
                progressSize = array.getDimensionPixelSize(
                        R.styleable.ExRecyclerView_erv_progressSize,
                        context.getResources().getDimensionPixelSize(R.dimen.base_progress_size));
                borderWidth = array.getDimensionPixelSize(
                        R.styleable.ExRecyclerView_erv_progressBorderWidth,
                        context.getResources().getDimensionPixelSize(R.dimen.base_progress_border_width));

                progressColor = array.getColor(R.styleable.ExRecyclerView_erv_progressColor, NO_VALUE);

                textSize = array.getDimensionPixelSize(R.styleable.ExRecyclerView_erv_textSize, textSize);
                textColor = array.getColor(R.styleable.ExRecyclerView_erv_textColor, textColor);
                textGravity = array.getInteger(R.styleable.ExRecyclerView_erv_textGravity, textGravity);
                int textMarginLeft = array.getDimensionPixelOffset(R.styleable.ExRecyclerView_erv_textMarginLeft, 0);
                int textMarginTop = array.getDimensionPixelOffset(R.styleable.ExRecyclerView_erv_textMarginTop, 0);
                int textMarginRight = array.getDimensionPixelOffset(R.styleable.ExRecyclerView_erv_textMarginRight, 0);
                int textMarginBottom = array.getDimensionPixelOffset(R.styleable.ExRecyclerView_erv_textMarginBottom, 0);
                setTextMargin(textMarginLeft, textMarginTop, textMarginRight, textMarginBottom);

                backgroundDrawable = array.getDrawable(R.styleable.ExRecyclerView_erv_backgroundDrawable);
                backgroundDuration = array.getInt(R.styleable.ExRecyclerView_erv_background_duration,
                                                  BACKGROUND_DURATION);
            } finally {
                array.recycle();
            }
        } else {
            progressSize = context.getResources()
                                  .getDimensionPixelSize(R.dimen.base_progress_size);
            borderWidth = context.getResources()
                                 .getDimensionPixelSize(R.dimen.base_progress_border_width);
        }

        if (progressColor == NO_VALUE) {
            progressColor = getColorPrimary();
        }

        if (textSize == NO_VALUE) {
            textSize = getContext().getResources()
                                   .getDimensionPixelSize(R.dimen.base_recycler_text_size);
        }

        if (backgroundDrawable == null) {
            backgroundDrawable = new ColorDrawable(getThemeBackgroundColor());
        }

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        progressDrawable = new CircularProgressDrawable(progressColor, borderWidth);
        progressDrawable.setCallback(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            int w = getWidth();
            int h = getHeight();
            backgroundDrawable.setBounds(0, 0, w, h);
            progressDrawable.setBounds(
                    w / 2 - progressSize / 2, h / 2 - progressSize / 2,
                    w / 2 + progressSize / 2, h / 2 + progressSize / 2);
        }
    }

    public void setProgressColor(@ColorInt int color) {
        progressDrawable.setColor(color);
    }

    public void setTextMargin(int left, int top, int right, int bottom) {
        textMargin.set(left, top, right, bottom);
        textLayout = null;
        invalidate();
    }

    /**
     * Set the paint's text size. This value must more than 0
     *
     * @param textSize set the paint's text size.
     */
    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
        invalidate();
    }

    public void setTextTypeface(Typeface textTypeface) {
        textPaint.setTypeface(textTypeface);
    }

    public void showText(@StringRes int textRes, Object... arguments) {
        showText(getContext().getString(textRes, arguments));
    }

    public void showText(@StringRes int textRes) {
        showText(getContext().getString(textRes));
    }

    public void showText(final CharSequence text) {
        drawText = true;
        drawProgress = false;
        textCurrent = text;
        textLayout = null;
        postInvalidate();

        if (getAdapter() != null && getAdapter().getItemCount() != 0) {
            if (errorTimer != null) errorTimer.dispose();
            errorTimer = Observable.timer(backgroundDuration, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    hideText();
                }
            });
        }
    }

    public void hideText() {
        if (drawText) {
            drawText = false;
            postInvalidate();
        }
    }

    public void showProgress() {
        if (!drawProgress) {
            drawProgress = true;
            drawText = false;
            progressDrawable.start();
            postInvalidate();
        }
    }

    public void hideProgress() {
        if (drawProgress) {
            drawProgress = false;
            progressDrawable.stop();
            postInvalidate();
        }
    }

    public void hideAll() {
        drawProgress = false;
        drawText = false;
        postInvalidate();
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        drawBackground(c);
        drawProgress(c);
        drawText(c);
    }

    private void drawBackground(Canvas c) {
        if ((drawProgress || drawText) && getAdapter() != null && getAdapter().getItemCount() > 0) {
            final int restore = c.save();
            backgroundDrawable.draw(c);
            c.restoreToCount(restore);
        }
    }

    private void drawProgress(Canvas c) {
        if (drawProgress) {
            progressDrawable.draw(c);
        }
    }

    private void drawText(Canvas c) {
        if (drawText) {
            if (textLayout == null) createTextLayout();

            final int restore = c.save();
            //half sizes
            final int xHalf = c.getWidth() / 2;
            final int yHalf = c.getHeight() / 2;
            //start point
            final int xStart = xHalf - (textLayout.getWidth() / 2);

            switch (textGravity) {
                case Gravity.TOP:
                    c.translate(xStart, textMargin.top);
                    break;
                case Gravity.BOTTOM:
                    c.translate(xStart, c.getHeight() - textLayout.getHeight() - textMargin.bottom);
                    break;
                default:
                    c.translate(xStart, yHalf - ((textLayout.getHeight() / 2)) + textMargin.top);
            }

            textLayout.draw(c);
            c.restoreToCount(restore);
        }
    }

    /**
     * Create StaticLayout {@link StaticLayout}
     */
    private void createTextLayout() {
        textLayout = new StaticLayout(
                textCurrent,
                textPaint,
                getWidth() - (textMargin.left + textMargin.right),
                Layout.Alignment.ALIGN_CENTER,
                1,
                0,
                true);
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == progressDrawable || super.verifyDrawable(who);
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

    private int getThemeBackgroundColor() {
        TypedValue a = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            return a.data;
        } else {
            return NO_VALUE;
        }
    }

    private int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = getContext().obtainStyledAttributes(
                typedValue.data, new int[]{R.attr.colorPrimary});

        int progressColor;

        try {
            progressColor = a.getColor(0, Color.BLACK);
        } finally {
            a.recycle();
        }

        return progressColor;
    }
}
