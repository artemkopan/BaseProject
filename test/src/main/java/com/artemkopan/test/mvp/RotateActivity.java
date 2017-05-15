package com.artemkopan.test.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.artemkopan.mvp.activity.BaseActivity;
import com.artemkopan.mvp.presenter.PresenterFactory;
import com.artemkopan.test.R;

public class RotateActivity extends BaseActivity<RotatePresenter, RotateView> implements RotateView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectPresenter();
    }

    @Override
    public int onInflateLayout() {
        return R.layout.activity_rotate;
    }

    @Override
    public void showProgress(@Nullable Object tag) {
        super.showProgress(tag);
    }

    @Override
    public void hideProgress(@Nullable Object tag) {
        super.hideProgress(tag);
    }

    @NonNull
    @Override
    public PresenterFactory<RotatePresenter, RotateView> getPresenterFactory() {
        return new RotatePresenterFactory();
    }

    @Override
    public void onPresenterCreatedOrRestored(@NonNull RotatePresenter presenter) {

    }

    @Override
    public void showError(@Nullable Object tag, String error) {
        super.showError(tag, error);
    }

}
