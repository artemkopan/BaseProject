package com.artemkopan.mvp.view;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View BasePresenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface BaseView {

    @UiThread
    void showError(@Nullable Object tag, @StringRes int errorRes, Object... formatArgs);

    @UiThread
    void showError(@Nullable Object tag, @StringRes int errorRes);

    @UiThread
    void showError(@Nullable Object tag, String error);

    @UiThread
    void showProgress(@Nullable Object tag);

    @UiThread
    void hideProgress(@Nullable Object tag);

}