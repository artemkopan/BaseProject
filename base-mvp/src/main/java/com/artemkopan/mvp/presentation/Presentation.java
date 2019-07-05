package com.artemkopan.mvp.presentation;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.view.View;

import io.reactivex.disposables.CompositeDisposable;

public interface Presentation {

    FragmentActivity getBaseActivity();

    CompositeDisposable getOnDestroyDisposable();

    FragmentManager getSupportFragmentManager();

    /**
     * if int == 0 then wasn't load inflate
     *
     * @return {@link androidx.annotation.LayoutRes} layout res id
     */
    int onInflateLayout();

    @Nullable
    View getView();


}
