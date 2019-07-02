package com.artemkopan.mvp.fragment;

import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.artemkopan.mvp.presentation.Presentation;
import com.artemkopan.mvp.presentation.PresentationManager;
import com.artemkopan.mvp.presenter.BasePresenter;
import com.artemkopan.mvp.presenter.lifecycle.PresenterProvider;
import com.artemkopan.mvp.presenter.lifecycle.PresentersProvider;
import com.artemkopan.mvp.view.BaseView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;

@SuppressWarnings("ALL")
public abstract class BaseFragment<P extends BasePresenter<V>, V extends BaseView> extends Fragment
        implements BaseView, Presentation {

    private static final String TAG = "BaseFragment";

    @Nullable protected P presenter;
    protected PresentationManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        manager = PresentationManager.Factory.create(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return manager.inflateLayout(inflater, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (presenter != null) //noinspection unchecked
            presenter.onViewAttached((V) this);
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) presenter.onViewDetached();
        manager.onDetach();
        super.onDestroyView();
    }

    public void setPresenter(@NonNull PresenterProvider.Factory factory,
                             Class<? extends BasePresenter<? extends BaseView>> clazz, boolean attach) {
        setPresenter((P) PresentersProvider.of(this, factory).get(clazz), attach);
    }

    public void setPresenter(@Nullable P presenter, boolean attach) {
        this.presenter = presenter;
        if (attach && presenter != null) presenter.onViewAttached((V) this);
    }

    @Override
    public void showError(@Nullable Object tag, @StringRes int errorRes, Object... formatArgs) {
        showError(tag, getString(errorRes, formatArgs));
    }

    @Override
    public void showError(@Nullable Object tag, @StringRes int errorRes) {
        showError(tag, getString(errorRes));
    }

    @Override
    public void showError(@Nullable Object tag, String error) {

    }

    /**
     * @return if true = {@link com.artemkopan.mvp.activity.BaseActivity#onBackPressed()} was called;
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

    //==============================================================================================
    // PresentationManagerImpl
    //==============================================================================================
    //region methods

    @Override
    public FragmentActivity getBaseActivity() {
        return getActivity();
    }

    @Override
    public CompositeDisposable getOnDestroyDisposable() {
        return manager.getDetachDisposable();
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

    //endregion

}
