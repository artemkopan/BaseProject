package com.artemkopan.baseproject.internal;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.artemkopan.baseproject.internal.UiManager.RxLifeCycle;
import com.jakewharton.rxrelay2.PublishRelay;

import io.reactivex.Observable;

/**
 * Created by Artem Kopan for jabrool
 * 04.01.17
 */

public interface UiInterface {

    FragmentActivity getBaseActivity();

    PublishRelay<RxLifeCycle> getRxLifeCycleSubject();

    Observable<RxLifeCycle> getOnDestroySubject();

    Observable<RxLifeCycle> getOnDestroyViewSubject();

    FragmentManager getSupportFragmentManager();

    /**
     * if int == 0 then wasn't load inflate
     *
     * @return {@link android.support.annotation.LayoutRes} layout res id
     */
    int onInflateLayout();

    @Nullable
    View getView();
}
