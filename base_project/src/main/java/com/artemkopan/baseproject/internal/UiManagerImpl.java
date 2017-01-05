package com.artemkopan.baseproject.internal;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.rx.BaseRx;
import com.artemkopan.baseproject.utils.ExtraUtils;
import com.jakewharton.rxrelay2.PublishRelay;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static butterknife.ButterKnife.findById;
import static com.artemkopan.baseproject.utils.ObjectUtils.castObject;

/**
 * Created by Artem Kopan for jabrool
 * 04.01.17
 */

@SuppressWarnings("WeakerAccess")
public final class UiManagerImpl implements UiManager {

    private final UiInterface mUiInterface;
    public PublishRelay<Object> mDestroySubject;
    private WeakReference<Toolbar> mToolbarReference;
    private Unbinder mUnbinder;

    public UiManagerImpl(@NonNull UiInterface uiInterface) {
        this.mUiInterface = uiInterface;
    }

    @Override
    public void onCreate() {
        mDestroySubject = PublishRelay.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Activity activity, Fragment fragment) {
        if (mUiInterface.onInflateLayout() > 0) {
            if (activity != null) {
                mUiInterface.getBaseActivity().setContentView(mUiInterface.onInflateLayout());
                mUnbinder = ButterKnife.bind(activity);
            } else if (fragment != null) {
                View view = inflater.inflate(mUiInterface.onInflateLayout(), container, false);
                mUnbinder = ButterKnife.bind(fragment, view);
                return view;
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        mDestroySubject.accept(BaseRx.TRIGGER);
    }

    @Override
    public void setUnbinder(Unbinder unbinder) {
        mUnbinder = unbinder;
    }

    @Override
    public PublishRelay<Object> getDestroySubject() {
        return mDestroySubject;
    }

    /**
     * Find toolbar by id and set to {@link AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId R.id.{toolbar_id}
     */
    @Override
    public void onToolbarInit(@IdRes int toolbarId) {
        onToolbarInit(toolbarId, false);
    }

    /**
     * Find toolbar by id and set to {@link AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param fromActivity if true, then find toolbar was in activity. For activities always true
     */
    @Override
    public void onToolbarInit(@IdRes int toolbarId, boolean fromActivity) {
        onToolbarInit(toolbarId, 0, fromActivity);
    }

    /**
     * Find toolbar by id and set to {@link AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param homeDrawable set drawable resource for home button (left button)
     */
    @Override
    public void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        onToolbarInit(toolbarId, homeDrawable, false);
    }

    /**
     * Find toolbar by id and set to {@link AppCompatActivity#setSupportActionBar(Toolbar)}
     *
     * @param toolbarId    R.id.{toolbar_id}
     * @param homeDrawable set drawable resource for home button (left button)
     * @param fromActivity if true, then find toolbar was in activity. For activities always true
     */
    @Override
    public void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable, boolean fromActivity) {
        Toolbar toolbar;

        if (fromActivity) {
            toolbar = findById(mUiInterface.getBaseActivity(), toolbarId);
        } else if (mUiInterface.getView() != null) {
            toolbar = findById(mUiInterface.getView(), toolbarId);
        } else {
            toolbar = null;
        }

        if (toolbar == null) {
            Log.e("onToolbarInit: Can't find toolbar");
            return;
        }

        setSupportToolbar(toolbar);

        if (homeDrawable > 0) {
            toolbar.setNavigationIcon(ContextCompat.getDrawable(mUiInterface.getBaseActivity(), homeDrawable));
            onToolbarHomeBtn(true);
        }

        mToolbarReference = new WeakReference<>(toolbar);

    }

    /**
     * Set click listener for home button
     */
    @Override
    public void onToolbarNavigationClickListener(OnClickListener onClickListener) {
        if (mToolbarReference != null && mToolbarReference.get() != null) {
            mToolbarReference.get().setNavigationOnClickListener(onClickListener);
        } else {
            Log.e("onToolbarNavigationClickListener: toolbar is null");
        }
    }

    /**
     * Set title for toolbar
     */
    @Override
    public void onToolbarSetTitle(@StringRes int titleRes) {
        AppCompatActivity activity = castObject(mUiInterface.getBaseActivity(), AppCompatActivity.class);
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(titleRes);
        } else {
            Log.e("onToolbarSetTitle: wrong instance activity " + mUiInterface.getBaseActivity() + " or action bar is" +
                  " null");
        }
    }

    /**
     * Set title for toolbar
     */
    @Override
    public void onToolbarSetTitle(CharSequence title) {
        AppCompatActivity activity = castObject(mUiInterface.getBaseActivity(), AppCompatActivity.class);
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        } else {
            Log.e("onToolbarSetTitle: wrong instance activity " + mUiInterface.getBaseActivity() + " or action bar is" +
                  " null");
        }
    }

    /**
     * Enable home button
     */
    @Override
    public void onToolbarHomeBtn(boolean show) {
        if (mUiInterface.getBaseActivity() instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) mUiInterface.getBaseActivity()).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(show);
            }
        }
    }

    private void setSupportToolbar(Toolbar toolbar) {
        if (mUiInterface.getBaseActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) mUiInterface.getBaseActivity()).setSupportActionBar(toolbar);
        }
    }

    /**
     * Set color for status bar. Work only for api21+
     */
    @Override
    public void setStatusBarColor(@ColorInt int color) {
        if (ExtraUtils.postLollipop()) {
            Window window = mUiInterface.getBaseActivity().getWindow();
            window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    /**
     * Set color for status bar with delay. Work only for api21+.
     */
    @Override
    public void setStatusBarColor(@ColorInt final int color, long delay, TimeUnit timeUnit) {
        if (ExtraUtils.postLollipop()) {
            Observable.timer(delay, timeUnit, AndroidSchedulers.mainThread())
                      .takeUntil(mDestroySubject)
                      .subscribe(new Consumer<Long>() {
                          @Override
                          public void accept(Long aLong) throws Exception {
                              setStatusBarColor(color);
                          }
                      });
        }
    }
}
