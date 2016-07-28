package com.artemkopan.baseproject.fragment;

import android.os.Bundle;
import android.view.View;

import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.MvpView;

public abstract class BasePresenterFragment<P extends BasePresenter<V>, V extends MvpView> extends BaseFragment {

    protected P mPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mPresenter != null && this instanceof MvpView) {
            mPresenter.attachView((V) this);
        } else if (mPresenter != null) {
            throw new RuntimeException("You must implement mvp view");
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }
}