package com.artemkopan.mvp.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.artemkopan.mvp.presentation.Presentation;
import com.artemkopan.mvp.presentation.PresentationManager;
import com.artemkopan.mvp.presenter.BasePresenter;
import com.artemkopan.mvp.presenter.lifecycle.PresenterProvider;
import com.artemkopan.mvp.presenter.lifecycle.PresentersProvider;
import com.artemkopan.mvp.view.BaseView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;

@SuppressWarnings({"unchecked", "unused"})
public abstract class BaseDialogFragment<P extends BasePresenter<V>, V extends BaseView> extends AppCompatDialogFragment
        implements BaseView, Presentation {

    public static final int REQ_CODE = 343;
    private static final String TAG = "BaseDialogFragment";

    @Nullable protected P presenter;
    private PresentationManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        manager = PresentationManager.Factory.create(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return manager.inflateLayout(inflater, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        onBaseDialogAnim(window);
        onBaseDialogBackground(window);
        onBaseDialogGravity(window);
        onBaseDialogSize(window);
        if (presenter != null) presenter.onViewAttached((V) this);
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) presenter.onViewDetached();
        manager.onDetach();
        super.onDestroyView();
    }

    public void setPresenter(@NonNull PresenterProvider.Factory factory,
                             Class<? extends BasePresenter<? extends BaseView>> clazz, boolean attach) {
        setPresenter((P) PresentersProvider.of(this, factory).get(clazz), attach);
    }

    public void setPresenter(@Nullable P presenter, boolean attach) {
        this.presenter = presenter;
        if (attach && presenter != null) presenter.onViewAttached((V) this);
    }

    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }

    @Override
    public void showError(@Nullable Object tag, @StringRes int errorRes, Object... formatArgs) {
        showError(tag, getString(errorRes, formatArgs));
    }

    @Override
    public void showError(@Nullable Object tag, @StringRes int errorRes) {
        showError(tag, getString(errorRes));
    }

    @Override
    public void showError(@Nullable Object tag, String error) {
    }

    @Override
    public void showProgress(@Nullable Object tag) {
    }

    @Override
    public void hideProgress(@Nullable Object tag) {
    }

    /**
     * @return Check implemented class and return them. If fragment was started from another
     * fragment {@link #getTargetFragment()} or {@link #getParentFragment()},
     * then used {@link #getParentFragment()} , else {@link #getActivity()}
     */
    @Nullable
    public <T> T getRootClass(Class<T> clazz) {
        if (getTargetFragment() != null && clazz.isInstance(getTargetFragment())) {
            return clazz.cast(getTargetFragment());
        } else if (getParentFragment() != null && clazz.isInstance(getParentFragment())) {
            return clazz.cast(getParentFragment());
        } else if (getActivity() != null && clazz.isInstance(getActivity())) {
            return clazz.cast(getActivity());
        }
        return null;
    }

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
    public void show(@Nullable FragmentManager manager, String tag) {
        if (manager == null) {
            Log.e(TAG, "show: Fragment Manager is null!!!");
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
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        onBaseDialogRequestFeature(dialog.getWindow());
        return dialog;
    }

    /**
     * Example: <p>{@code window.getAttributes().windowAnimations = R.style.DialogAnimationUpDown;}</p>
     *
     * @param window Current window
     */
    public void onBaseDialogAnim(Window window) {
        // Not implemented
    }

    /**
     * Example <p>{@code window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));} </p>
     *
     * @param window Current window
     */
    public void onBaseDialogBackground(Window window) {
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * Example <p>{@code window.setGravity(Gravity.CENTER)} </p>
     *
     * @param window Current window
     */
    public void onBaseDialogGravity(Window window) {
        window.setGravity(Gravity.CENTER);
    }

    /**
     * Example <p>{@code window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)} </p>
     *
     * @param window Current window
     */
    public void onBaseDialogSize(Window window) {
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Example <p>{@code window.requestFeature(Window.FEATURE_NO_TITLE)} </p>
     *
     * @param window Current window
     */
    public void onBaseDialogRequestFeature(Window window) {
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    //endregion

    //==============================================================================================
    // PresentationManagerImpl
    //==============================================================================================
    //region methods

    @Override
    public FragmentActivity getBaseActivity() {
        return getActivity();
    }

    @Override
    public CompositeDisposable getOnDestroyDisposable() {
        return manager.getDetachDisposable();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    public void setStatusBarColor(@ColorInt int color) {
        manager.setStatusBarColor(color);
    }

    public void setStatusBarColor(@ColorInt int color, long delay, TimeUnit timeUnit) {
        manager.setStatusBarColor(color, delay, timeUnit);
    }
    //endregion
}
