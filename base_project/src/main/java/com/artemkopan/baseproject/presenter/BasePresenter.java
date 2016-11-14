package com.artemkopan.baseproject.presenter;

import android.support.annotation.Nullable;

import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.rx.BaseRx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BasePresenter<T extends MvpView> implements Presenter<T> {

    protected PublishSubject<Object> mDestroyLoad = PublishSubject.create();

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

    public void onCancelLoad() {
        mDestroyLoad.onNext(BaseRx.TRIGGER);
    }

    public <V> ObservableTransformer<V, V> onCancelTransform() {
        return new ObservableTransformer<V, V>() {
            @Override
            public ObservableSource<V> apply(Observable<V> tObservable) {
                return tObservable.takeUntil(mDestroyLoad);
            }
        };
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
        Log.e("onShowError: " + message);
    }

    public void onShowError(Object tag, Throwable throwable) {
        onShowError(tag, throwable.getMessage());
        Log.e("onShowError: ", throwable);
    }

    public void onShowError(Object tag, String message) {
        if (mMvpView != null) {
            mMvpView.hideProgress(null);
            mMvpView.showError(tag, message);
        }
    }

}
