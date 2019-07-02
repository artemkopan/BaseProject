package com.artemkopan.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import static com.artemkopan.utils.ViewUtils.DrawableIndex.DRAWABLE_BOTTOM;
import static com.artemkopan.utils.ViewUtils.DrawableIndex.DRAWABLE_LEFT;
import static com.artemkopan.utils.ViewUtils.DrawableIndex.DRAWABLE_RIGHT;
import static com.artemkopan.utils.ViewUtils.DrawableIndex.DRAWABLE_TOP;

public final class ViewUtils {

    private static final String TAG = "ViewUtils";

    /**
     * Get activity from view
     *
     * @param view current view
     * @return Current activity
     */
    @Nullable
    public static Activity getActivity(View view) {
        if (view == null) {
            return null;
        }
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * @return content view from activity.
     */
    public static View findContentView(@NonNull Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    /**
     * Inflate view
     */
    public static View inflateView(@NonNull ViewGroup parent, @LayoutRes int res) {
        return LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
    }

    /**
     * Get relative value of X position from root View;
     * Default parentView is System DecorView;
     *
     * @param view currentView;
     * @return Relative X value;
     */
    public static float getRelativeX(View view, @Nullable View parentView) {
        if (view.getParent() == (parentView == null ? view.getRootView() : parentView)) {
            return view.getX();
        } else {
            return view.getX() + getRelativeX((View) view.getParent(), parentView);
        }
    }

    /**
     * Get relative value of Y position from root View;
     * Default parentView is System DecorView;
     *
     * @param view currentView
     * @return Relative Y value;
     */
    public static float getRelativeY(View view, @Nullable View parentView) {
        if (view.getParent() == (parentView == null ? view.getRootView() : parentView)) {
            return view.getY();
        } else {
            return view.getY() + getRelativeY((View) view.getParent(), parentView);
        }
    }

    /**
     * Get relative center of view;
     */
    public static Point getCenterRelativeView(View view, @Nullable View parentView) {
        int x = (int) getRelativeX(view, parentView);
        int y = (int) getRelativeY(view, parentView);
        return new Point(x + view.getWidth() / 2, y + view.getHeight() / 2);
    }

    /**
     * Get center of view ;
     */
    public static Point getCenterView(View view) {
        return new Point((int) getCenterViewXPos(view), (int) getCenterViewYPos(view));
    }

    /**
     * Get center of view by X position;
     */
    public static float getCenterViewXPos(View view) {
        int width = view.getWidth();
        return view.getX() + (width > 0 ? width / 2 : 0);
    }

    /**
     * Get center of view by Y position;
     */
    public static float getCenterViewYPos(View view) {
        int height = view.getHeight();
        return view.getY() + (height > 0 ? height / 2 : 0);
    }

    /**
     * Check view size, if {@link View#getWidth()} or {@link View#getHeight()} more then 0, then return true;
     */
    public static boolean checkSize(View view) {
        return view.getWidth() > 0 || view.getHeight() > 0;
    }

    /**
     * View on pre draw call
     */
    public static void preDrawListener(final View view, @NonNull final Runnable onPreDraw) {
        if (view.getViewTreeObserver().isAlive()) {
            view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (view.getViewTreeObserver().isAlive()) {
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        onPreDraw.run();
                    }
                    return true;
                }
            });
        }
    }

    /**
     * Firstly check view size. If size {@code (w ro h) <= 0 }, then call preDraw.
     */
    public static void viewSizeInflated(View view, @NonNull Runnable ready) {
        if (checkSize(view)) {
            ready.run();
        } else {
            preDrawListener(view, ready);
        }
    }

    /**
     * Add new input filters to already exists
     *
     * @param text       - view
     * @param newFilters - new Input filters;
     */
    public static void addInputFilter(TextView text, InputFilter... newFilters) {
        InputFilter[] filters = text.getFilters();

        if (filters == null || filters.length == 0) {
            text.setFilters(newFilters);
            return;
        }

        int length = filters.length;

        filters = Arrays.copyOf(filters, length + newFilters.length);
        System.arraycopy(newFilters, 0, filters, length, newFilters.length);

        text.setFilters(filters);
    }

    public static boolean onDrawableClick(MotionEvent event, TextView view, @DrawableIndex int pos, @Px int fuzz) {
        return onDrawableClick(event, MotionEvent.ACTION_UP, view, pos, fuzz);
    }

    /**
     * Handle click on drawable textView; Set listener {@link TextView#onTouchEvent(MotionEvent)} add call this method.
     * If true, then drawable was clicked;
     * <p>
     * <b>IMPORTANT!!!</b> in {@link TextView#onTouchEvent(MotionEvent)} you must return true,
     * because it is work only if event action == {@link MotionEvent#ACTION_UP}
     * </p>
     */
    public static boolean onDrawableClick(MotionEvent event, int motionEvent, TextView view, @DrawableIndex int pos, @Px int fuzz) {
        if (event.getAction() == motionEvent) {
            Drawable drawable = view.getCompoundDrawables()[pos];

            if (drawable == null) {
                Log.e(TAG, "Drawable is null. Please set another position");
                return false;
            }

            final int x = (int) event.getX();
            final int y = (int) event.getY();
            final Rect bounds = drawable.getBounds();

            switch (pos) {
                case DRAWABLE_LEFT:
                    if (x >= (view.getPaddingLeft() - fuzz)) {
                        if (x <= (view.getPaddingLeft() + bounds.width() + fuzz)) {
                            if (y >= (view.getPaddingTop() - fuzz)) {
                                if (y <= (view.getHeight() - view.getPaddingBottom() + fuzz)) {
                                    return true;
                                }
                            }
                        }
                    }
                    break;
                case DRAWABLE_TOP:
                    if (x >= (view.getPaddingLeft() - fuzz)) {
                        if (x <= (view.getWidth() - view.getPaddingRight() + fuzz)) {
                            if (y >= (view.getPaddingTop() - fuzz)) {
                                if (y <= (view.getPaddingTop() + bounds.height() + fuzz)) {
                                    return true;
                                }
                            }
                        }
                    }
                    break;
                case DRAWABLE_RIGHT:
                    if (x >= (view.getWidth() - view.getPaddingRight() - bounds.width() - fuzz)) {
                        if (x <= (view.getWidth() - view.getPaddingRight() + fuzz)) {
                            if (y >= (view.getPaddingTop() - fuzz)) {
                                if (y <= (view.getHeight() - view.getPaddingBottom() + fuzz)) {
                                    return true;
                                }
                            }
                        }
                    }
                    break;
                case DRAWABLE_BOTTOM:
                    if (x >= (view.getPaddingLeft() - fuzz)) {
                        if (x <= (view.getWidth() - view.getPaddingRight() + fuzz)) {
                            if (y >= (view.getHeight() - view.getPaddingBottom() - bounds.height() - fuzz)) {
                                if (y <= (view.getHeight() - view.getPaddingBottom() + fuzz)) {
                                    return true;
                                }
                            }
                        }
                    }
                    break;
            }

        }
        return false;
    }

    public static void rememberLastClickPos(View view) {
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setTag(R.id.view_touch_x, (int) event.getRawX());
                    v.setTag(R.id.view_touch_y, (int) event.getRawY());
                }
                return false;
            }
        });
    }

    public static Point remindLastClickPost(View view) {
        Object rawX = view.getTag(R.id.view_touch_x);
        Object rawY = view.getTag(R.id.view_touch_y);
        return new Point(ObjectUtils.castToInteger(rawX, 0), ObjectUtils.castToInteger(rawY, 0));
    }

    @IntDef({DRAWABLE_LEFT, DRAWABLE_TOP, DRAWABLE_RIGHT, DRAWABLE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawableIndex {

        int DRAWABLE_LEFT = 0;
        int DRAWABLE_TOP = 1;
        int DRAWABLE_RIGHT = 2;
        int DRAWABLE_BOTTOM = 3;
    }
}
