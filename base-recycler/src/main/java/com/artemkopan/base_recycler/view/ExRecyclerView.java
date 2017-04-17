package com.artemkopan.base_recycler.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.artemkopan.base_recycler.R;
import com.artemkopan.base_recycler.listeners.OnRecyclerPaginationListener;
import com.artemkopan.base_recycler.listeners.OnRecyclerPaginationListener.OnRecyclerPaginationResult;
import com.artemkopan.base_utils.ViewUtils;
import com.artemkopan.base_widget.drawable.CircularProgressDrawable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ExRecyclerView extends RecyclerView {

    private static final int NO_VALUE = -1;
    private static final int BACKGROUND_DURATION = 2000;
    private static final String TAG = "ExRecyclerView";
    private StaticLayout staticLayout;
    private TextPaint textPaint;
    private CircularProgressDrawable progressDrawable;
    private Drawable backgroundDrawable;
    private OnRecyclerPaginationListener paginationListener;
    private Disposable errorTimer;
    private String textDefault;
    private int progressSize = NO_VALUE;
    private int textPadding = NO_VALUE;
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
                textDefault = array.getString(R.styleable.ExRecyclerView_erv_textDefault);
                textPadding = array.getDimensionPixelSize(R.styleable.ExRecyclerView_erv_textPadding, textPadding);

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
        if (textPadding == NO_VALUE) {
            textPadding = context.getResources()
                                 .getDimensionPixelSize(R.dimen.base_recycler_text_padding);
        }

        if (backgroundDrawable == null) {
            backgroundDrawable = new ColorDrawable(getThemeBackgroundColor());
        }

        if (TextUtils.isEmpty(textDefault)) {
            textDefault = context.getString(R.string.base_info_items_not_found);
        }

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        progressDrawable = new CircularProgressDrawable(progressColor, borderWidth);
        progressDrawable.setCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        backgroundDrawable.setBounds(0, 0, w, h);
        progressDrawable.setBounds(
                w / 2 - progressSize / 2, h / 2 - progressSize / 2,
                w / 2 + progressSize / 2, h / 2 + progressSize / 2);
    }

    public OnRecyclerPaginationListener createPaginationListener(OnRecyclerPaginationResult listener) {
        setPaginationListener(new OnRecyclerPaginationListener(getLayoutManager(),
                                                               OnRecyclerPaginationListener.VERTICAL,
                                                               listener));
        return paginationListener;
    }

    public void setPaginationListener(OnRecyclerPaginationListener scrollListener) {
        if (paginationListener != null) removeOnScrollListener(paginationListener);
        paginationListener = scrollListener;
        addOnScrollListener(scrollListener);
    }

    public void setPaginationState(boolean isEnable) {
        if (isEnable) {
            enablePagination();
        } else {
            disablePagination();
        }
    }

    public void enablePagination() {
        if (paginationListener != null) {
            paginationListener.enablePagination();
        }
    }

    public void disablePagination() {
        if (paginationListener != null) {
            paginationListener.disablePagination();
        }
    }

    public void setProgressColor(@ColorInt int color) {
        progressDrawable.setColor(color);
    }

    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
    }

    public void showText(@StringRes int textRes, Object... arguments) {
        showText(getContext().getString(textRes, arguments));
    }

    public void showText(@StringRes int textRes) {
        showText(getContext().getString(textRes));
    }

    public void showText(final String text) {
        if (ViewUtils.checkSize(this)) {
            createTextLayout(text);
            Log.w(TAG, "showText: Recycler not init yet");
        } else {
            ViewUtils.preDrawListener(this, new Runnable() {
                @Override
                public void run() {
                    if (!ViewUtils.checkSize(ExRecyclerView.this)) {
                        Log.e(TAG, "showText: show text forbidden, because width or height == 0");
                        return;
                    }
                    createTextLayout(text);
                    showText(text);
                }
            });
            return;
        }

        drawText = true;
        drawProgress = false;

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
        drawText = false;
        postInvalidate();
    }

    public void showProgress() {
        drawProgress = true;
        drawText = false;
        progressDrawable.start();
        postInvalidate();
    }

    public void hideProgress() {
        drawProgress = false;
        progressDrawable.stop();
        postInvalidate();
    }

    public void hideAll() {
        drawProgress = false;
        drawText = false;
        postInvalidate();
    }

    /**
     * Create StaticLayout {@link StaticLayout}
     */
    private void createTextLayout(CharSequence text) {
        staticLayout = new StaticLayout(
                text,
                textPaint,
                getWidth() - textPadding * 2,
                Layout.Alignment.ALIGN_CENTER,
                1,
                0,
                true);
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);

        if ((drawProgress || drawText)
            && getAdapter() != null && getAdapter().getItemCount() > 0) {

            final int restore = c.save();
            backgroundDrawable.draw(c);
            c.restoreToCount(restore);
        }

        if (drawProgress) {
            progressDrawable.draw(c);
        }

        if (drawText && staticLayout != null) {
            final int restore = c.save();
            c.translate(
                    (c.getWidth() / 2) - (staticLayout.getWidth() / 2),
                    (c.getHeight() / 2) - ((staticLayout.getHeight() / 2)));
            staticLayout.draw(c);
            c.restoreToCount(restore);
        }
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
