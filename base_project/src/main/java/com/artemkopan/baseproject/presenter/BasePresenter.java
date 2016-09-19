package com.artemkopan.baseproject.presenter;

import android.support.annotation.Nullable;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BasePresenter<T extends MvpView> implements Presenter<T> {

    @Nullable private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    @Nullable
    protected T getMvpView() {
        return mMvpView;
    }

    protected void onShowProgress() {
        onShowProgress(null);
    }

    protected void onShowProgress(Object tag) {
        if (mMvpView != null) {
            mMvpView.showProgress(tag);
        }
    }

    protected void onHideProgress() {
        onHideProgress(null);
    }

    protected void onHideProgress(Object tag) {
        if (mMvpView != null) {
            mMvpView.hideProgress(tag);
        }
    }

    protected void onShowError(Throwable throwable) {
        onShowError(null, throwable.getMessage());
    }

    protected void onShowError(String message) {
        onShowError(null, message);
    }

    protected void onShowError(Object tag, Throwable throwable) {
        onShowError(tag, throwable.getMessage());
    }

    protected void onShowError(Object tag, String message) {
        if (mMvpView != null) {
            mMvpView.hideProgress(null);
            mMvpView.showError(tag, message);
        }
    }

}
