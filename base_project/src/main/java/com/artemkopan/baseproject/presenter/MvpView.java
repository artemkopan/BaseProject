package com.artemkopan.baseproject.presenter;

import android.support.annotation.Nullable;

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface MvpView {

    void showError(String error);

    /**
     * You can set specific tag for different progress bars;
     */
    void showProgress(@Nullable Object tag);

    void hideProgress(@Nullable Object tag);
}