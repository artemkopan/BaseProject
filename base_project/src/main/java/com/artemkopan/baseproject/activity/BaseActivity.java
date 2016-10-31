package com.artemkopan.baseproject.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.artemkopan.baseproject.fragment.BaseFragment;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.MvpView;
import com.artemkopan.baseproject.rx.BaseRx;
import com.artemkopan.baseproject.utils.ExtraUtils;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static butterknife.ButterKnife.findById;

public abstract class BaseActivity<P extends BasePresenter<V>, V extends MvpView> extends
        AppCompatActivity implements MvpView {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public PublishSubject<Object> mDestroySubject = PublishSubject.create();

    @SuppressWarnings("SpellCheckingInspection")
    protected Unbinder mUnbinder;
    protected Toolbar mToolbar;
    protected P mPresenter;
    private boolean mShouldFinish = false;

    public void bindButterKnife() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        if (mShouldFinish) {
            supportFinishAfterTransition();
        }
        super.onStop();
    }

    /**
     * This method need for finish activity after transition without bug.
     * {@link #supportFinishAfterTransition()} work incorrect
     */
    public void shouldFinishActivity() {
        mShouldFinish = true;
    }

    @Override
    protected void onDestroy() {
        mDestroySubject.onNext(BaseRx.TRIGGER);
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }
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

    public void setStatusBarColor(@ColorInt int color) {
        if (ExtraUtils.postLollipop()) {
            Window window = getWindow();
            window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

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


    /**
     * Toolbar init. Usually call {@link #onCreate(Bundle)}
     *
     * @param toolbarId Res id your toolbar;
     */
    protected void onToolbarInit(@IdRes int toolbarId) {
        onToolbarInit(toolbarId, 0);
    }

    /**
     * Toolbar init. Usually call {@link #onCreate(Bundle)}
     *
     * @param toolbarId    Res id your toolbar;
     * @param homeDrawable Set home image resources ( - optional)
     */
    protected void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        mToolbar = findById(this, toolbarId);

        if (mToolbar == null) {
            Log.e("onToolbarInit: Can't find toolbar", new IllegalArgumentException());
            return;
        } else if (homeDrawable > 0) {
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, homeDrawable));
        }

        setActionBar(mToolbar);
    }

    /**
     * Set toolbar title
     *
     * @param titleRes string res value
     */
    protected void onToolbarSetTitle(@StringRes int titleRes) {
        onToolbarSetTitle(getString(titleRes));
    }

    /**
     * Set toolbar title
     *
     * @param title title string value
     */
    protected void onToolbarSetTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    /**
     * If you want enable home button. You can listen event in
     * {@link #onOptionsItemSelected(MenuItem)}
     * with item id {@link android.R.id#home}
     */
    protected void onToolbarHomeBtn(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    protected void setActionBar(@Nullable Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    //endregion
}