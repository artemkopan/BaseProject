package com.artemkopan.baseproject.presenter;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenterImpl
 * indicating the MvpView type that wants to be attached with.
 */
@SuppressWarnings("WeakerAccess")
public interface BasePresenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}