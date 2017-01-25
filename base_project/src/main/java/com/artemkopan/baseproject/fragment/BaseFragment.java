package com.artemkopan.baseproject.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.internal.UiInterface;
import com.artemkopan.baseproject.internal.UiManager;
import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.MvpView;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.concurrent.TimeUnit;

import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter<V>, V extends MvpView> extends Fragment
        implements MvpView, UiInterface {

    protected P mPresenter;
    protected UiManager mUiManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mUiManager = UiManager.Factory.create(this);
        super.onCreate(savedInstanceState);
        mUiManager.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return mUiManager.onCreateView(inflater, container, null, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUiManager.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mUiManager.onDestroy();
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
     * @return if true = {@link BaseActivity#onBackPressed()} was called;
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
    public PublishRelay<Object> getDestroySubject() {
        return mUiManager.getDestroySubject();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    public void setUnbinder(Unbinder unbinder) {
        mUiManager.setUnbinder(unbinder);
    }

    public void onToolbarInit(@IdRes int toolbarId) {
        mUiManager.onToolbarInit(toolbarId, false);
    }

    public void onToolbarInit(@IdRes int toolbarId, boolean fromActivity) {
        mUiManager.onToolbarInit(toolbarId, fromActivity);
    }

    public void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        mUiManager.onToolbarInit(toolbarId, homeDrawable, false);
    }

    public void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable, boolean fromActivity) {
        mUiManager.onToolbarInit(toolbarId, homeDrawable, fromActivity);
    }

    public void onToolbarNavigationClickListener(OnClickListener onClickListener) {
        mUiManager.onToolbarNavigationClickListener(onClickListener);
    }

    public void onToolbarSetTitle(@StringRes int titleRes) {
        mUiManager.onToolbarSetTitle(titleRes);
    }

    public void onToolbarSetTitle(CharSequence title) {
        mUiManager.onToolbarSetTitle(title);
    }

    public void onToolbarHomeBtn(boolean show) {
        mUiManager.onToolbarHomeBtn(show);
    }

    public void setStatusBarColor(@ColorInt int color) {
        mUiManager.setStatusBarColor(color);
    }

    public void setStatusBarColor(@ColorInt int color, long delay, TimeUnit timeUnit) {
        mUiManager.setStatusBarColor(color, delay, timeUnit);
    }

}
