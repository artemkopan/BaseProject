package com.artemkopan.test.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.artemkopan.mvp.activity.BaseActivity;
import com.artemkopan.mvp.presenter.BasePresenter;
import com.artemkopan.mvp.presenter.lifecycle.PresenterProvider.Factory;
import com.artemkopan.mvp.presenter.lifecycle.PresentersProvider;
import com.artemkopan.mvp.view.BaseView;
import com.artemkopan.test.R;
import com.artemkopan.utils.router.ActivityBuilder;
import com.artemkopan.utils.router.Router;

public class RotateActivity extends BaseActivity<RotatePresenter, RotateView> implements RotateView {

    public static ActivityBuilder route() {
        return Router.activity(RotateActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Nullable
    @Override
    public RotatePresenter getPresenter() {
        return PresentersProvider.of(this, new Factory() {
            @Override
            public <T extends BasePresenter<? extends BaseView>> T create(Class<T> modelClass) {
                //noinspection unchecked
                return (T) new RotatePresenter();
            }
        }).get(RotatePresenter.class);
    }

    @Override
    public void showError(@Nullable Object tag, String error) {
        super.showError(tag, error);
    }

}
