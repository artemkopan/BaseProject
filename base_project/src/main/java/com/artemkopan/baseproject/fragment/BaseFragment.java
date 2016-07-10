package com.artemkopan.baseproject.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.rx.Lifecycle;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subjects.PublishSubject;


public abstract class BaseFragment extends Fragment {


    public PublishSubject<Lifecycle> mPublishSubject = PublishSubject.create();
    @SuppressWarnings("SpellCheckingInspection")
    protected Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (onCreateInflateView() > 0) {
            View view = inflater.inflate(onCreateInflateView(), container, false);
            mUnbinder = ButterKnife.bind(this, view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        mPublishSubject.onNext(Lifecycle.ON_STOP);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mPublishSubject.onNext(Lifecycle.ON_DESTROY);
        super.onDestroy();
    }

    /**
     * Call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} with auto inflate
     *
     * @return {@link LayoutRes} layout res id
     */
    public abstract int onCreateInflateView();

    /**
     * @return if true = {@link BaseActivity#onBackPressed()} was called;
     */
    public boolean onBackPressed() {
        return true;
    }

}
