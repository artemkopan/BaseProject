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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.artemkopan.baseproject.utils.ExtraUtils;
import com.artemkopan.baseproject.utils.Log;
import com.jakewharton.rxrelay2.PublishRelay;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static butterknife.ButterKnife.findById;
import static com.artemkopan.baseproject.utils.ObjectUtils.castObject;

/**
 * Created by Artem Kopan for jabrool
 * 04.01.17
 */

@SuppressWarnings("WeakerAccess")
public final class UiManagerImpl implements UiManager {

    private final Predicate<RxLifeCycle> onDestroyFilter;
    private final Predicate<RxLifeCycle> onDestroyViewFilter;
    private final UiInterface uiInterface;

    public PublishRelay<RxLifeCycle> rxLifeCycle;

    private WeakReference<Toolbar> toolbarReference;
    private Unbinder unbinder;

    public UiManagerImpl(@NonNull UiInterface uiInterface) {
        this.uiInterface = uiInterface;
        onDestroyFilter = new Predicate<RxLifeCycle>() {
            @Override
            public boolean test(RxLifeCycle rxLifeCycle) throws Exception {
                return rxLifeCycle == RxLifeCycle.ON_DESTROY;
            }
        };
        onDestroyViewFilter = new Predicate<RxLifeCycle>() {
            @Override
            public boolean test(RxLifeCycle rxLifeCycle) throws Exception {
                return rxLifeCycle == RxLifeCycle.ON_DESTROY_VIEW;
            }
        };
    }

    @Override
    public void onCreate() {
        rxLifeCycle = PublishRelay.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Activity activity, Fragment fragment) {
        if (uiInterface.onInflateLayout() > 0) {
            if (activity != null) {
                uiInterface.getBaseActivity().setContentView(uiInterface.onInflateLayout());
                unbinder = ButterKnife.bind(activity);
            } else if (fragment != null) {
                View view = inflater.inflate(uiInterface.onInflateLayout(), container, false);
                unbinder = ButterKnife.bind(fragment, view);
                return view;
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        rxLifeCycle.accept(RxLifeCycle.ON_DESTROY_VIEW);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        rxLifeCycle.accept(RxLifeCycle.ON_DESTROY);
    }

    @Override
    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    @Override
    public PublishRelay<RxLifeCycle> getRxLifeCycleSubject() {
        return rxLifeCycle;
    }

    @Override
    public Observable<RxLifeCycle> getOnDestroySubject() {
        return rxLifeCycle.filter(onDestroyFilter);
    }

    @Override
    public Observable<RxLifeCycle> getOnDestroyViewSubject() {
        return rxLifeCycle.filter(onDestroyViewFilter);
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
            toolbar = findById(uiInterface.getBaseActivity(), toolbarId);
        } else if (uiInterface.getView() != null) {
            toolbar = findById(uiInterface.getView(), toolbarId);
        } else {
            toolbar = null;
        }

        if (toolbar == null) {
            Log.e("onToolbarInit: Can't find toolbar");
            return null;
        }

        if (homeDrawable > 0) {
            toolbar.setNavigationIcon(ContextCompat.getDrawable(uiInterface.getBaseActivity(), homeDrawable));
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
            Log.e("onToolbarNavigationClickListener: toolbar is null");
        }
    }

    /**
     * Set title for toolbar
     */
    @Override
    public void onToolbarSetTitle(@StringRes int titleRes) {
        AppCompatActivity activity = castObject(uiInterface.getBaseActivity(), AppCompatActivity.class);
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(titleRes);
        } else {
            Log.e("onToolbarSetTitle: wrong instance activity " + uiInterface.getBaseActivity() + " or action bar is" +
                  " null");
        }
    }

    /**
     * Set title for toolbar
     */
    @Override
    public void onToolbarSetTitle(CharSequence title) {
        AppCompatActivity activity = castObject(uiInterface.getBaseActivity(), AppCompatActivity.class);
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        } else {
            Log.e("onToolbarSetTitle: wrong instance activity " + uiInterface.getBaseActivity() + " or action bar is" +
                  " null");
        }
    }

    private void setSupportToolbar(Toolbar toolbar) {
        if (uiInterface.getBaseActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) uiInterface.getBaseActivity()).setSupportActionBar(toolbar);
        }
    }

    /**
     * Set color for status bar. Work only for api21+
     */
    @Override
    public void setStatusBarColor(@ColorInt int color) {
        if (ExtraUtils.postLollipop()) {
            Window window = uiInterface.getBaseActivity().getWindow();
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
                      .takeUntil(rxLifeCycle)
                      .subscribe(new Consumer<Long>() {
                          @Override
                          public void accept(Long aLong) throws Exception {
                              setStatusBarColor(color);
                          }
                      });
        }
    }
}
