package com.artemkopan.mvp.presenter;

import android.support.annotation.Nullable;

import com.artemkopan.mvp.view.BaseView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Base class that implements the BasePresenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {

    protected CompositeDisposable clearedDisposable = new CompositeDisposable();

    @Nullable private T view;

    @Override
    public void onViewAttached(T view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onCleared() {
        clearedDisposable.clear();
    }

    @Nullable
    public T getMvpView() {
        return view;
    }

    public void onShowProgress() {
        onShowProgress(null);
    }

    public void onShowProgress(Object tag) {
        if (view != null) {
            view.showProgress(tag);
        }
    }

    public void onHideProgress() {
        onHideProgress(null);
    }

    public void onHideProgress(Object tag) {
        if (view != null) {
            view.hideProgress(tag);
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
        if (view != null) {
            view.showError(errorTag, message);
        }
    }

}
