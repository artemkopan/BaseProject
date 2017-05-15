package com.artemkopan.test.mvp;

import android.util.Log;

import com.artemkopan.mvp.presenter.BasePresenterImpl;

import com.artemkopan.test.mvp.RotateView;

class RotatePresenter extends BasePresenterImpl<RotateView> {

    int count = 0;

    @Override
    public void onViewAttached(RotateView view) {
        Log.i("Test", "" + (++count));
    }
}
