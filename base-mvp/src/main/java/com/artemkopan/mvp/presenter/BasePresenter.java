package com.artemkopan.mvp.presenter;

import com.artemkopan.mvp.view.BaseView;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenterImpl
 * indicating the BaseView type that wants to be attached with.
 */
@SuppressWarnings("WeakerAccess")
public interface BasePresenter<V extends BaseView> {

    void onViewAttached(V view);

    void onViewDetached();

    void onCleared();

}