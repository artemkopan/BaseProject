package com.artemkopan.mvp.presentation;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import io.reactivex.disposables.CompositeDisposable;


public interface Presentation {

    FragmentActivity getBaseActivity();

    CompositeDisposable getOnDestroyDisposable();

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
