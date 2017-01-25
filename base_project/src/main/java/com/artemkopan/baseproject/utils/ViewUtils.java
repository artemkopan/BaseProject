package com.artemkopan.baseproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.TextView;

import com.artemkopan.baseproject.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.ButterKnife;

import static com.artemkopan.baseproject.utils.ViewUtils.DrawablePosition.DRAWABLE_BOTTOM;
import static com.artemkopan.baseproject.utils.ViewUtils.DrawablePosition.DRAWABLE_LEFT;
import static com.artemkopan.baseproject.utils.ViewUtils.DrawablePosition.DRAWABLE_RIGHT;
import static com.artemkopan.baseproject.utils.ViewUtils.DrawablePosition.DRAWABLE_TOP;

public final class ViewUtils {

    /**
     * ButterKnife action for enable view {@link View#setEnabled(boolean)} or disable;
     */
    public static final ButterKnife.Setter<View, Boolean> SET_ENABLE =
            new ButterKnife.Setter<View, Boolean>() {
                @Override
                public void set(@NonNull View view, Boolean value, int index) {
                    view.setEnabled(value);
                }
            };

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
     * Check view size, if {@link View#getWidth()} or {@link View#getHeight()} > 0, then return true;
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
     * Firstly check view size. If size (w ro h) <= 0, then call preDraw.
     */
    public static void viewSizeInflated(View view, @NonNull Runnable ready) {
        if (checkSize(view)) {
            ready.run();
        } else {
            preDrawListener(view, ready);
        }
    }

    /**
     * Handle click on drawable textView; Set listener {@link TextView#onTouchEvent(MotionEvent)} add call this method.
     * If true, then drawable was clicked;
     * <p>
     * <b>IMPORTANT!!!</b> in {@link TextView#onTouchEvent(MotionEvent)} you must return true,
     * because it is work only if event action == {@link MotionEvent#ACTION_UP}
     * </p>
     */
    public static boolean onDrawableClick(MotionEvent event, TextView view, @DrawablePosition int pos) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int drawableWidth = view.getCompoundDrawables()[pos].getBounds().width();
            if (event.getRawX() >= (view.getRight() - drawableWidth)) {
                return true;
            }
        }
        return false;
    }

    public static void enableRecordTouchPos(final View view) {
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setTag(R.id.view_touch_x, event.getRawX());
                    view.setTag(R.id.view_touch_y, event.getRawX());
                }
                return true;
            }
        });
    }

    public static Point readRecordedTouchPos(View view) {
        Point point = new Point();
        Object x = view.getTag(R.id.view_touch_x);
        Object y = view.getTag(R.id.view_touch_y);
        if (ObjectUtils.instanceOfInt(x) && ObjectUtils.instanceOfInt(y)) {
            point.set((Integer) x, (Integer) y);
        } else {
            Log.e("firstly, you must enable record touch pos ViewUtils.enableRecordTouchPos()");
        }
        return point;
    }

    @IntDef({DRAWABLE_LEFT, DRAWABLE_TOP, DRAWABLE_RIGHT, DRAWABLE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawablePosition {

        int DRAWABLE_LEFT = 0;
        int DRAWABLE_TOP = 1;
        int DRAWABLE_RIGHT = 2;
        int DRAWABLE_BOTTOM = 3;
    }
}
