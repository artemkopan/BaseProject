package com.artemkopan.test.mvp;

import com.artemkopan.mvp.presenter.PresenterFactory;

/**
 * Created by Artem Kopan for BaseProject
 * 15.05.2017
 */

public class RotatePresenterFactory implements PresenterFactory<RotatePresenter, RotateView> {

    @Override
    public RotatePresenter create() {
        return new RotatePresenter();
    }
}
