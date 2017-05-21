package com.artemkopan.mvp.presentation;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

@SuppressWarnings("WeakerAccess")
public final class PresentationManagerImpl implements PresentationManager {

    private static final String TAG = "PresentationManagerImpl";

    private final Presentation presentation;

    private WeakReference<Toolbar> toolbarReference;
    private CompositeDisposable detachDisposable;

    public PresentationManagerImpl(@NonNull Presentation presentation) {
        this.presentation = presentation;
        detachDisposable = new CompositeDisposable();
    }

    @Override
    public View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
        if (presentation.onInflateLayout() > 0) {
            return inflater.inflate(presentation.onInflateLayout(), container, false);
        }
        return null;
    }

    @Override
    public void onDetach() {
        detachDisposable.clear();
    }

    @Override
    public CompositeDisposable getDetachDisposable() {
        return detachDisposable;
    }

    /**
     * Find toolbar by id and set to {@link AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId R.id.{toolbar_id}
     */
    @Override
    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId) {
        return onToolbarInit(toolbarId, false);
    }

    /**
     * Find toolbar by id and set to {@link AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param fromActivity if true, then find toolbar was in activity. For activities always true
     */
    @Override
    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId, boolean fromActivity) {
        return onToolbarInit(toolbarId, 0, fromActivity);
    }

    /**
     * Find toolbar by id and set to {@link AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param homeDrawable set drawable resource for home button (left button)
     */
    @Override
    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        return onToolbarInit(toolbarId, homeDrawable, false);
    }

    /**
     * Find toolbar by id and set to {@link AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param homeDrawable set drawable resource for home button (left button)
     * @param fromActivity if true, then find toolbar was in activity. For activities always true
     */
    @Override
    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable, boolean fromActivity) {
        Toolbar toolbar;

        if (fromActivity) {
            toolbar = (Toolbar) presentation.getBaseActivity().findViewById(toolbarId);
        } else if (presentation.getView() != null) {
            toolbar = (Toolbar) presentation.getView().findViewById(toolbarId);
        } else {
            toolbar = null;
        }

        if (toolbar == null) {
            Log.e(TAG, "onToolbarInit: Can't find toolbar");
            return null;
        }

        if (homeDrawable > 0) {
            toolbar.setNavigationIcon(ContextCompat.getDrawable(presentation.getBaseActivity(), homeDrawable));
        }

        setSupportToolbar(toolbar);

        toolbarReference = new WeakReference<>(toolbar);

        return toolbar;
    }

    /**
     * Set click listener for home button
     */
    @Override
    public void onToolbarNavigationClickListener(OnClickListener onClickListener) {
        if (toolbarReference != null && toolbarReference.get() != null) {
            toolbarReference.get().setNavigationOnClickListener(onClickListener);
        } else {
            Log.e(TAG, "onToolbarNavigationClickListener: toolbar is null");
        }
    }

    /**
     * Set title for toolbar
     */
    @Override
    public void onToolbarSetTitle(@StringRes int titleRes) {
        AppCompatActivity activity = castObject(presentation.getBaseActivity(), AppCompatActivity.class);

        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(titleRes);
        } else {
            Log.e(TAG, "onToolbarSetTitle: wrong instance activity " + presentation.getBaseActivity() + " or action bar is" +
                       " null");
        }
    }

    /**
     * Set title for toolbar
     */
    @Override
    public void onToolbarSetTitle(CharSequence title) {
        AppCompatActivity activity = castObject(presentation.getBaseActivity(), AppCompatActivity.class);
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        } else {
            Log.e(TAG, "onToolbarSetTitle: wrong instance activity " + presentation.getBaseActivity() + " or action bar is" +
                       " null");
        }
    }

    private void setSupportToolbar(Toolbar toolbar) {
        if (presentation.getBaseActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) presentation.getBaseActivity()).setSupportActionBar(toolbar);
        }
    }

    /**
     * Set color for status bar. Work only for api21+
     */
    @Override
    public void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = presentation.getBaseActivity().getWindow();
            window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    /**
     * Set color for status bar with delay. Work only for api21+.
     */
    @Override
    public void setStatusBarColor(@ColorInt final int color, long delay, TimeUnit timeUnit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            detachDisposable.add(Observable.timer(delay, timeUnit, AndroidSchedulers.mainThread())
                                           .subscribe(new Consumer<Long>() {
                                               @Override
                                               public void accept(Long aLong) throws Exception {
                                                   setStatusBarColor(color);
                                               }
                                           }));
        }
    }

    @Nullable
    private <T> T castObject(Activity activity, Class<T> clazz) {
        return clazz.isInstance(activity) ? clazz.cast(activity) : null;
    }
}
