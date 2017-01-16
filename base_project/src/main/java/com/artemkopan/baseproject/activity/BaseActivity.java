package com.artemkopan.baseproject.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.View.OnClickListener;

import com.artemkopan.baseproject.fragment.BaseFragment;
import com.artemkopan.baseproject.internal.UiInterface;
import com.artemkopan.baseproject.internal.UiManager;
import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.MvpView;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.concurrent.TimeUnit;

import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter<V>, V extends MvpView>
        extends AppCompatActivity
        implements MvpView, UiInterface {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @SuppressWarnings("SpellCheckingInspection")
    protected P mPresenter;
    private UiManager mUiManager;
    private boolean mShouldFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mUiManager = UiManager.Factory.create(this);
        super.onCreate(savedInstanceState);
        mUiManager.onCreate();
        mUiManager.onCreateView(null, null, this, null);
    }

    @SuppressWarnings("unchecked")
    public void injectPresenter(P presenter, boolean attach) {
        mPresenter = presenter;
        if (attach) mPresenter.attachView((V) this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mShouldFinish) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUiManager.onDestroyView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount() - 1;
        if (backStackCount >= 0) {
            BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount);
            String str = backEntry.getName();

            Fragment fragment = fragmentManager.findFragmentByTag(str);

            if (fragment != null
                && fragment instanceof BaseFragment
                && !((BaseFragment) fragment).onBackPressed()) {
                return;
            } else {
                getSupportFragmentManager().popBackStackImmediate();
                return;
            }
        }

        super.onBackPressed();
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

    @Override
    public FragmentActivity getBaseActivity() {
        return this;
    }

    @Override
    public PublishRelay<Object> getDestroySubject() {
        return mUiManager.getDestroySubject();
    }

    @Override
    @Nullable
    public View getView() {
        return findViewById(android.R.id.content);
    }

    /**
     * Use this method if you want start new activity with transition and finish current;
     */
    public void shouldFinish() {
        mShouldFinish = true;
    }

    public void setUnbinder(Unbinder unbinder) {
        mUiManager.setUnbinder(unbinder);
    }

    public void onToolbarInit(@IdRes int toolbarId) {
        mUiManager.onToolbarInit(toolbarId, true);
    }

    public void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        mUiManager.onToolbarInit(toolbarId, homeDrawable, true);
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