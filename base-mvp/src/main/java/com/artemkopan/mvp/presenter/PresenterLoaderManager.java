package com.artemkopan.mvp.presenter;

import android.support.annotation.NonNull;

import com.artemkopan.mvp.view.BaseView;

/**
 * Created by Artem Kopan for BaseProject
 * 15.05.2017
 */

public interface PresenterLoaderManager<P extends BasePresenter<V>, V extends BaseView> {

    int LOADER_ID = 101;


    /**
     * Hook for subclasses that deliver the {@link BasePresenter} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    void onPresenterCreatedOrRestored(@NonNull P presenter);

    /**
     * Use this method in case you want to specify a spefic ID for the {@link PresenterLoader}.
     * By default its value would be {@link #LOADER_ID}.
     */
    int loaderId();

}

