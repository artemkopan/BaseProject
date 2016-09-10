package com.artemkopan.baseproject.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.MvpView;
import com.artemkopan.baseproject.rx.Lifecycle;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.PublishSubject;

import static butterknife.ButterKnife.findById;

public abstract class BaseFragment<P extends BasePresenter<V>, V extends MvpView> extends Fragment implements MvpView {

    public PublishSubject<Lifecycle> mPublishSubject = PublishSubject.create();

    @Nullable
    protected Toolbar mToolbar;
    protected P mPresenter;
    @SuppressWarnings("SpellCheckingInspection")
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (onCreateInflateView() > 0) {
            View view = inflater.inflate(onCreateInflateView(), container, false);
            mUnbinder = ButterKnife.bind(this, view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        mPublishSubject.onNext(Lifecycle.ON_STOP);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mPublishSubject.onNext(Lifecycle.ON_DESTROY);
        super.onDestroy();
    }


    /**
     * Call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} with auto inflate
     *
     * @return {@link LayoutRes} layout res id
     */
    public abstract int onCreateInflateView();

    /**
     * @return if true = {@link BaseActivity#onBackPressed()} was called;
     */
    public boolean onBackPressed() {
        return true;
    }


    //region Toolbar methods

    /**
     * Toolbar init. Usually call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     *
     * @param toolbarId    Res id your toolbar;
     * @param fromActivity If need find toolbar in {@link AppCompatActivity}
     */
    protected void onToolbarInit(@IdRes int toolbarId, boolean fromActivity) {
        onToolbarInit(toolbarId, 0, fromActivity);
    }

    /**
     * Toolbar init. Usually call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     *
     * @param toolbarId    Res id your toolbar;
     * @param homeDrawable Set home image resources ( - optional)
     * @param fromActivity If need find toolbar in {@link AppCompatActivity}
     */
    protected void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable, boolean fromActivity) {

        if (fromActivity && getActivity() != null) {
            mToolbar = findById(getActivity(), toolbarId);
        } else if (!fromActivity && getView() != null) {
            mToolbar = findById(getView(), toolbarId);
        } else {
            Log.e("From Activity: " + fromActivity +
                    "\nActivity: " + getActivity() +
                    "\nView: " + getView());
        }

        if (mToolbar == null) {
            Log.e("onToolbarInit: Can't find toolbar", new IllegalArgumentException());
            return;
        } else if (homeDrawable > 0) {
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(getContext(), homeDrawable));
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
     * If you want enable home button. You can listen event in {@link #onOptionsItemSelected(MenuItem)} with item id {@link android.R.id#home}
     */
    protected void onToolbarHomeBtn(boolean show) {
        if (getActivity() != null
                && getActivity() instanceof AppCompatActivity
                && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    protected void setActionBar(@Nullable Toolbar toolbar) {
        if (toolbar != null && getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
    }

    //endregion

    //region status bar methods
    protected void setStatusBarColor(@ColorInt int color) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setStatusBarColor(color);
        } else {
            Log.e("Please check your activity on null or extends " + getActivity());
        }
    }

    protected void setStatusBarColor(@ColorInt int color, long delay, TimeUnit timeUnit) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setStatusBarColor(color, delay, timeUnit);
        } else {
            Log.e("Please check your activity on null or extends " + getActivity());
        }
    }
    //endregion
}
