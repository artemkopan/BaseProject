package com.artemkopan.test.mvp;

import android.util.Log;

import com.artemkopan.mvp.presenter.BasePresenterImpl;

class RotatePresenter extends BasePresenterImpl<RotateView> {

    private int count = 0;

    @Override
    public void onViewAttached(RotateView view) {
        Log.i("Test", "" + (++count));
    }

    @Override
    public void onCleared() {
        Log.i("Test", "Cleared");
    }
}
