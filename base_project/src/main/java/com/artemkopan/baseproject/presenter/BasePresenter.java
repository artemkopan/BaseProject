package com.artemkopan.baseproject.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
@SuppressWarnings("unused")
public abstract class BasePresenter<T extends MvpView> implements Presenter<T> {

    //    protected Subscription mSubscription;
    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
//        if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void saveInstance(Bundle bundle) {
        //Call if needed
    }

    @Override
    public void restoreInstance(Bundle bundle) {
        //Call if needed
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    @Nullable
    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
