package com.artemkopan.baseproject.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.internal.UiInterface;
import com.artemkopan.baseproject.internal.UiManager;
import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.BasePresenterImpl;
import com.artemkopan.baseproject.presenter.MvpView;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.concurrent.TimeUnit;

import butterknife.Unbinder;

public abstract class BaseDialogFragment<P extends BasePresenter<V>, V extends MvpView> extends DialogFragment
        implements MvpView, UiInterface {

    public static final int REQ_CODE = 343;

    protected P mPresenter;
    private UiManager mUiManager;

    //==============================================================================================
    // Show dialog methods
    //==============================================================================================
    //region methods

    public void show(FragmentManager manager) {
        show(manager, this.getClass().getName());
    }

    public void show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), this.getClass().getName());
    }

    public void show(FragmentActivity activity, String tag) {
        show(activity.getSupportFragmentManager(), tag);
    }

    public void show(Fragment fragment) {
        show(fragment, this.getClass().getName());
    }

    public void show(Fragment fragment, String tag) {
        show(fragment, tag, REQ_CODE);
    }

    public void show(Fragment fragment, String tag, int reqCode) {
        show(fragment.getFragmentManager(), tag);
        setTargetFragment(fragment, reqCode);
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

    //endregion

    //==============================================================================================
    // Create Dialog methods
    //==============================================================================================
    //region methods

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog;
        if (onBaseBottomSheetDialog()) {
            dialog = new BottomSheetDialog(getContext(), getTheme());
        } else {
            dialog = super.onCreateDialog(savedInstanceState);
            onBaseDialogRequestFeature(dialog.getWindow());
        }
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
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void onBaseDialogRequestFeature(Window window) {
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    public boolean onBaseBottomSheetDialog() {
        return false;
    }

    //endregion

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mUiManager = UiManager.Factory.create(this);
        super.onCreate(savedInstanceState);
        mUiManager.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return mUiManager.onCreateView(inflater, container, null, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUiManager.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mUiManager.onDestroy();
        super.onDestroy();
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

    @Override
    public FragmentActivity getBaseActivity() {
        return getActivity();
    }

    @Override
    public PublishRelay<Object> getDestroySubject() {
        return mUiManager.getDestroySubject();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    public void setUnbinder(Unbinder unbinder) {
        mUiManager.setUnbinder(unbinder);
    }

    public void setStatusBarColor(@ColorInt int color) {
        mUiManager.setStatusBarColor(color);
    }

    public void setStatusBarColor(@ColorInt int color, long delay, TimeUnit timeUnit) {
        mUiManager.setStatusBarColor(color, delay, timeUnit);
    }
}
