package com.artemkopan.baseproject.dialog;

import android.app.Dialog;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.MvpView;
import com.artemkopan.baseproject.rx.BaseRx;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseDialogFragment<P extends BasePresenter<V>, V extends MvpView> extends DialogFragment
        implements MvpView {

    private static final String KEY_IS_SHOWN = "IS_SHOWN";
    public PublishSubject<Object> mDestroySubject = PublishSubject.create();
    protected Unbinder mUnbinder;
    protected P mPresenter;

    public void show(FragmentManager manager) {
        show(manager, this.getClass().getName());
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager == null) {
            return;
        }
        FragmentTransaction transactionFragment = manager.beginTransaction();
        transactionFragment.add(this, tag);
        transactionFragment.commitAllowingStateLoss();
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

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (onCreateInflateView() > 0) {
            View view = inflater.inflate(onCreateInflateView(), container, false);
            mUnbinder = ButterKnife.bind(this, view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroy() {
        mDestroySubject.onNext(BaseRx.TRIGGER);
        super.onDestroy();
    }

    /**
     * Call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} with auto inflate
     *
     * @return {@link LayoutRes} layout res id
     */
    public abstract int onCreateInflateView();

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
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void onBaseDialogRequestFeature(Window window) {
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }

    @Override
    public void showError(@Nullable Object tag, String error) {}

    @Override
    public void showProgress(@Nullable Object tag) {}

    @Override
    public void hideProgress(@Nullable Object tag) {}

    /**
     * @return Check implemented class and return them. If fragment was started from another
     * fragment {@link #getTargetFragment()} or {@link #getParentFragment()},
     * then used {@link #getParentFragment()} , else {@link #getActivity()}
     */
    @Nullable
    public <T> T getParentClass(Class<T> clazz) {
        if (getTargetFragment() != null && clazz.isInstance(getTargetFragment())) {
            return clazz.cast(getTargetFragment());
        } else if (getParentFragment() != null && clazz.isInstance(getParentFragment())) {
            return clazz.cast(getParentFragment());
        } else if (getActivity() != null && clazz.isInstance(getActivity())) {
            return clazz.cast(getActivity());
        }
        return null;
    }
}
