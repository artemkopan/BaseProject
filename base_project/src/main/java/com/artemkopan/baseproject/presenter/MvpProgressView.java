package com.artemkopan.baseproject.presenter;

import android.support.annotation.Nullable;

@Deprecated
public interface MvpProgressView extends MvpView {

    void startProgress();

    void stopProgress();

}
