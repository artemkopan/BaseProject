package com.artemkopan.baseproject.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.rx.Lifecycle;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subjects.PublishSubject;


public abstract class BaseDialogFragment extends DialogFragment {

    public PublishSubject<Lifecycle> mPublishSubject = PublishSubject.create();
    protected Unbinder mUnbinder;
    private boolean mShown = false;

    public void show(FragmentManager manager) {
        show(manager, this.getClass().getSimpleName());
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager == null) {
            return;
        }
        FragmentTransaction transactionFragment = manager.beginTransaction();
        transactionFragment.add(this, tag);
        transactionFragment.commitAllowingStateLoss();

        mShown = true;
    }

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

    /**
     * Call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} with auto inflate
     *
     * @return {@link LayoutRes} layout res id
     */
    public abstract int onCreateInflateView();


    @Override
    public void onStop() {
        mPublishSubject.onNext(Lifecycle.ON_STOP);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mPublishSubject.onNext(Lifecycle.ON_DESTROY);
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mShown = false;
        super.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        onBaseDialogRequestFeature(dialog.getWindow());
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        onBaseDialogAnim(window);
        onBaseDialogBackground(window);
        onBaseDialogGravity(window);
        onBaseDialogSize(window);
    }

    public void onBaseDialogAnim(Window window) {
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpDown;
    }

    public void onBaseDialogBackground(Window window) {
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onBaseDialogGravity(Window window) {
        window.setGravity(Gravity.CENTER);
    }

    public void onBaseDialogSize(Window window) {
        window.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void onBaseDialogRequestFeature(Window window) {
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    public boolean isShowing() {
        return mShown;
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
            ignored.printStackTrace();
        }
    }

}
