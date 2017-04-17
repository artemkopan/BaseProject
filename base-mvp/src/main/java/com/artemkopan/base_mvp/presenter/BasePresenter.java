package com.artemkopan.base_mvp.presenter;

import com.artemkopan.base_mvp.view.BaseView;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenterImpl
 * indicating the BaseView type that wants to be attached with.
 */
@SuppressWarnings("WeakerAccess")
public interface BasePresenter<V extends BaseView> {

    void attachView(V mvpView);

    void detachView();

}