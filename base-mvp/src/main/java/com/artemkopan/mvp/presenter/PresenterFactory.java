package com.artemkopan.mvp.presenter;

import com.artemkopan.mvp.view.BaseView;

/**
 * Creates a Presenter object.
 * @param <T> presenter type
 */
public interface PresenterFactory<T extends BasePresenter<V>, V extends BaseView> {
    T create();
}
