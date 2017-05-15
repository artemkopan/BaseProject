package com.artemkopan.mvp.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.artemkopan.mvp.fragment.BaseFragment;
import com.artemkopan.mvp.presentation.Presentation;
import com.artemkopan.mvp.presentation.PresentationManager;
import com.artemkopan.mvp.presenter.BasePresenter;
import com.artemkopan.mvp.presenter.PresenterFactory;
import com.artemkopan.mvp.presenter.PresenterLoader;
import com.artemkopan.mvp.presenter.PresenterLoaderManager;
import com.artemkopan.mvp.view.BaseView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity<P extends BasePresenter<V>, V extends BaseView>
        extends AppCompatActivity
        implements BaseView, Presentation, PresenterLoaderManager<P, V> {

    private static final String TAG = "BaseActivity";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Nullable protected P presenter;
    protected PresentationManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        manager = PresentationManager.Factory.create(this);
        super.onCreate(savedInstanceState);
        setContentView(onInflateLayout());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            //noinspection unchecked
            presenter.onViewAttached((V) this);
        }
    }

    @Override
    protected void onStop() {
        if (presenter != null) {
            presenter.onViewDetached();
        }
        manager.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount() - 1;
        if (backStackCount >= 0) {
            BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount);
            String str = backEntry.getName();

            Fragment fragment = fragmentManager.findFragmentByTag(str);

            if (fragment != null && fragment instanceof BaseFragment && !((BaseFragment) fragment).onBackPressed()) {
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

    //==============================================================================================
    // PresenterLoader
    //==============================================================================================
    //region methods
    public void injectPresenter() {
        Loader<P> loader = getSupportLoaderManager().getLoader(loaderId());
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((PresenterLoader<P, V>) loader).getPresenter();
            onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader() {
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        getSupportLoaderManager().initLoader(loaderId(), null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                Log.i(TAG, "onCreateLoader");
                return new PresenterLoader<>(BaseActivity.this, getPresenterFactory());
            }

            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                Log.i(TAG, "onLoadFinished");
                BaseActivity.this.presenter = presenter;
                onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<P> loader) {
                Log.i(TAG, "onLoaderReset");
                BaseActivity.this.presenter = null;
            }
        });
    }

    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    @NonNull
    public abstract PresenterFactory<P, V> getPresenterFactory();

    /**
     * Hook for subclasses that deliver the {@link BasePresenter} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    public abstract void onPresenterCreatedOrRestored(@NonNull P presenter);

    /**
     * Use this method in case you want to specify a spefic ID for the {@link PresenterLoader}.
     * By default its value would be {@link #LOADER_ID}.
     */
    public int loaderId() {
        return LOADER_ID;
    }

    //endregion

    //==============================================================================================
    // PresentationManagerImpl
    //==============================================================================================
    //region methods

    @Override
    public FragmentActivity getBaseActivity() {
        return this;
    }

    @Override
    public CompositeDisposable getOnDestroyDisposable() {
        return manager.onStopDisposable();
    }

    @Override
    @Nullable
    public View getView() {
        return findViewById(android.R.id.content);
    }

    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId) {
        return manager.onToolbarInit(toolbarId, true);
    }

    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        return manager.onToolbarInit(toolbarId, homeDrawable, true);
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

    //endregion
}