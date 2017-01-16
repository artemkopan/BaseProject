package com.artemkopan.baseproject.internal;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.jakewharton.rxrelay2.PublishRelay;

import java.util.concurrent.TimeUnit;

import butterknife.Unbinder;

/**
 * Created by Artem Kopan for jabrool
 * 04.01.17
 */

public interface UiManager {

    void onCreate();

    /**
     * @param inflater  if Activity set null
     * @param container if Activity set null
     * @param activity  if Activity set current Activity
     * @param fragment  if Fragment set current Fragment, else set null
     */
    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Activity activity, Fragment fragment);

    /**
     * Call in fragment {@link  Fragment#onDestroyView()}
     * in activity {@link Activity#onDestroy()}
     */
    void onDestroyView();

    PublishRelay<Object> getDestroySubject();

    /**
     * If you use custom implementation {@link #onCreateView(LayoutInflater, ViewGroup, Activity, Fragment)}
     * set butterknife unbinder
     */
    void setUnbinder(Unbinder unbinder);

    /**
     * Find toolbar by id and set to {@link android.support.v7.app.AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId R.id.{toolbar_id}
     */
    void onToolbarInit(@IdRes int toolbarId);

    /**
     * Find toolbar by id and set to {@link android.support.v7.app.AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param fromActivity if true, then find toolbar was in activity. For activities always true
     */
    void onToolbarInit(@IdRes int toolbarId, boolean fromActivity);

    /**
     * Find toolbar by id and set to {@link android.support.v7.app.AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param homeDrawable set drawable resource for home button (left button)
     */
    void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable);

    /**
     * Find toolbar by id and set to {@link android.support.v7.app.AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param homeDrawable set drawable resource for home button (left button)
     * @param fromActivity if true, then find toolbar was in activity. For activities always true
     */
    void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable, boolean fromActivity);

    /**
     * Set click listener for home button
     */
    void onToolbarNavigationClickListener(OnClickListener onClickListener);

    /**
     * Set title for toolbar
     */
    void onToolbarSetTitle(@StringRes int titleRes);

    /**
     * Set title for toolbar
     */
    void onToolbarSetTitle(CharSequence title);

    /**
     * Enable home button
     */
    void onToolbarHomeBtn(boolean show);

    /**
     * Set color for status bar. Work only for api21+
     */
    void setStatusBarColor(@ColorInt int color);

    /**
     * Set color for status bar with delay. Work only for api21+.
     */
    void setStatusBarColor(@ColorInt final int color, long delay, TimeUnit timeUnit);

    class Factory {

        public static UiManager create(UiInterface uiInterface) {
            return new UiManagerImpl(uiInterface);
        }

    }

}
