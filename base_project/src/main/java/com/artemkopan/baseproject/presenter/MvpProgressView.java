package com.artemkopan.baseproject.presenter;

import android.support.annotation.Nullable;

public interface MvpProgressView extends MvpView {

    /**
     * You can set specific tag for different progress bars;
     */
    void showProgress(@Nullable Object tag);

    void hideProgress(@Nullable Object tag);

}
