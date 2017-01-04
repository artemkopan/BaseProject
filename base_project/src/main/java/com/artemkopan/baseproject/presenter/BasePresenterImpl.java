package com.artemkopan.baseproject.presenter;

import android.support.annotation.Nullable;

/**
 * Base class that implements the BasePresenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BasePresenterImpl<T extends MvpView> implements BasePresenter<T> {

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
    public T getMvpView() {
        return mMvpView;
    }

    public void onShowProgress() {
        onShowProgress(null);
    }

    public void onShowProgress(Object tag) {
        if (mMvpView != null) {
            mMvpView.showProgress(tag);
        }
    }

    public void onHideProgress() {
        onHideProgress(null);
    }

    public void onHideProgress(Object tag) {
        if (mMvpView != null) {
            mMvpView.hideProgress(tag);
        }
    }

    public void onShowError(Throwable throwable) {
        onShowError(null, throwable.getMessage());
    }

    public void onShowError(String message) {
        onShowError(null, message);
    }

    public void onShowError(Object errorTag, Throwable throwable) {
        onShowError(errorTag, throwable.getMessage());
    }

    public void onShowError(Object errorTag, String message) {
        if (mMvpView != null) {
            mMvpView.showError(errorTag, message);
        }
    }

}
