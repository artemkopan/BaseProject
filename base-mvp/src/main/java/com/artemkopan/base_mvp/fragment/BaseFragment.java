package com.artemkopan.base_mvp.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.artemkopan.base_mvp.presentation.Presentation;
import com.artemkopan.base_mvp.presentation.PresentationManager;
import com.artemkopan.base_mvp.presenter.BasePresenter;
import com.artemkopan.base_mvp.view.BaseView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;

@SuppressWarnings("ALL")
public abstract class BaseFragment<P extends BasePresenter<V>, V extends BaseView> extends Fragment
        implements BaseView, Presentation {

    protected P presenter;
    protected PresentationManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        manager = PresentationManager.Factory.create(this);
        super.onCreate(savedInstanceState);
        manager.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return manager.onCreateView(inflater, container, null, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (presenter != null) {
            presenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();
        }
        manager.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        manager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showError(@Nullable Object tag, String error) {

    }

    @Override
    public void showProgress(@Nullable Object tag) {

    }

    @Override
    public void hideProgress(@Nullable Object tag) {

    }

    /**
     * @return if true = {@link com.artemkopan.base_mvp.activity.BaseActivity#onBackPressed()} was called;
     */
    public boolean onBackPressed() {
        return true;
    }

    /**
     * @return Check implemented class and return them. If fragment was started from another
     * fragment {@link #getTargetFragment()} or {@link #getParentFragment()},
     * then used {@link #getParentFragment()} , else {@link #getActivity()}
     */
    @Nullable
    public <T> T getRootClass(Class<T> clazz) {
        if (getTargetFragment() != null && clazz.isInstance(getTargetFragment())) {
            return clazz.cast(getTargetFragment());
        } else if (getParentFragment() != null && clazz.isInstance(getParentFragment())) {
            return clazz.cast(getParentFragment());
        } else if (getActivity() != null && clazz.isInstance(getActivity())) {
            return clazz.cast(getActivity());
        }
        return null;
    }

    @Override
    public FragmentActivity getBaseActivity() {
        return getActivity();
    }

    @Override
    public CompositeDisposable getOnDestroyDisposable() {
        return manager.getOnDestroyDisposable();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId) {
        return manager.onToolbarInit(toolbarId, false);
    }

    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId, boolean fromActivity) {
        return manager.onToolbarInit(toolbarId, fromActivity);
    }

    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        return manager.onToolbarInit(toolbarId, homeDrawable, false);
    }

    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable, boolean fromActivity) {
        return manager.onToolbarInit(toolbarId, homeDrawable, fromActivity);
    }

    public void onToolbarNavigationClickListener(OnClickListener onClickListener) {
        manager.onToolbarNavigationClickListener(onClickListener);
    }

    public void onToolbarSetTitle(@StringRes int titleRes) {
        manager.onToolbarSetTitle(titleRes);
    }

    public void onToolbarSetTitle(CharSequence title) {
        manager.onToolbarSetTitle(title);
    }

    public void setStatusBarColor(@ColorInt int color) {
        manager.setStatusBarColor(color);
    }

    public void setStatusBarColor(@ColorInt int color, long delay, TimeUnit timeUnit) {
        manager.setStatusBarColor(color, delay, timeUnit);
    }

}
