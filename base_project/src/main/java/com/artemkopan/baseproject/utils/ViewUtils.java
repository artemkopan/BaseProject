package com.artemkopan.baseproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;


public class ViewUtils {

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
     * Get relative value of X position from root View;
     * Default parentView is System DecorView;
     *
     * @param view currentView;
     * @return Relative X value;
     */
    public static float getRelativeX(View view, @Nullable View parentView) {
        if (view.getParent() == (parentView == null ? view.getRootView() : parentView))
            return view.getX();
        else
            return view.getX() + getRelativeX((View) view.getParent(), parentView);
    }

    /**
     * Get relative value of Y position from root View;
     * Default parentView is System DecorView;
     *
     * @param view currentView
     * @return Relative Y value;
     */
    public static float getRelativeY(View view, @Nullable View parentView) {
        if (view.getParent() == (parentView == null ? view.getRootView() : parentView))
            return view.getY();
        else
            return view.getY() + getRelativeY((View) view.getParent(), parentView);
    }

    /**
     * Get center of view and plus X position;
     */
    public static float getCenterViewXPos(View view) {
        int width = view.getWidth();
        return view.getX() + (width > 0 ? width / 2 : 0);
    }

    /**
     * Get center of view and plus Y position;
     */
    public static float getCenterViewYPos(View view) {
        int height = view.getHeight();
        return view.getY() + (height > 0 ? height / 2 : 0);
    }
}
