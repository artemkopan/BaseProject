package com.artemkopan.baseproject.presenter;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

/**
 * You can set specific tag for different progress bars;
 */
public interface MvpProgressView extends MvpView {

    @UiThread
    void showProgress(@Nullable Object tag);

    @UiThread
    void hideProgress(@Nullable Object tag);

}
