package com.artemkopan.baseproject.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.fragment.BaseFragment;

@SuppressWarnings({"WeakerAccess", "unused"})
@Deprecated
public class DialogController {

    private ProgressDialog mProgressDialog;
    private BaseDialogFragment mMessageDialog;

    public void onDestroy() {
        dismissProgressDialog();
        dismissMessageDialog();
    }

    public void dismissMessageDialog() {
        if (mMessageDialog != null) {
            mMessageDialog.dismissAllowingStateLoss();
            mMessageDialog = null;
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Nullable
    public ProgressDialog showProgressDialog(@Nullable Activity activity) {
        return showProgressDialog(activity, R.string.base_info_loading);
    }

    @Nullable
    public ProgressDialog showProgressDialog(@Nullable Activity activity, @StringRes int messageRes) {
        if (activity == null) {
            return null;
        }
        showProgressDialog(activity, activity.getString(messageRes));
        return mProgressDialog;
    }

    /**
     * Show default system progress dialog.
     * Dialog was be destroyed on {@link BaseFragment#onDestroy()} or {@link BaseActivity#onDestroy()}
     *
     * @param activity current activity
     * @param message  message (optional);
     */
    @Nullable
    public ProgressDialog showProgressDialog(@Nullable Activity activity, String message) {
        if (activity == null) {
            return null;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
        } else if (mProgressDialog.isShowing()) {
            return mProgressDialog;
        }

        mProgressDialog.show();
        return mProgressDialog;
    }

    @Nullable
    public BaseDialogFragment showMessageDialog(@Nullable FragmentActivity activity, String title) {
        if (activity == null) {
            return null;
        }
        return showMessageDialog(activity, title, activity.getString(R.string.base_info_try_again));
    }

    @Nullable
    public BaseDialogFragment showMessageDialog(@Nullable FragmentActivity activity, @StringRes int titleRes) {
        return showMessageDialog(activity, titleRes, R.string.base_info_try_again);
    }

    @Nullable
    public BaseDialogFragment showMessageDialog(@Nullable FragmentActivity activity, @StringRes int titleRes, @StringRes int descriptionRes) {
        if (activity == null) {
            return null;
        }
        return showMessageDialog(activity, activity.getString(titleRes), activity.getString(descriptionRes));
    }

    @Nullable
    public BaseDialogFragment showMessageDialog(@Nullable FragmentActivity activity, String title, String description) {
        if (activity == null) {
            return null;
        }
        showMessageDialog(activity, MessageDialog.newInstance(title, description));

        return mMessageDialog;
    }

    /**
     * Show message inform dialog; Usually use in warnings or errors;
     * Use on default {@link MessageDialog}, but you can set custom {@link BaseDialogFragment}
     *
     * @param activity        current CompatActivity
     * @param messageFragment Dialog fragment
     * @return Dialog fragment; If Activity is null then return null!
     */
    @Nullable
    public BaseDialogFragment showMessageDialog(@Nullable FragmentActivity activity, BaseDialogFragment messageFragment) {
        if (activity == null) {
            return null;
        }

        if (mMessageDialog != null) {
            mMessageDialog.dismiss();
        }
        mMessageDialog = messageFragment;

        mMessageDialog.show(activity.getSupportFragmentManager());

        return mMessageDialog;
    }
}



