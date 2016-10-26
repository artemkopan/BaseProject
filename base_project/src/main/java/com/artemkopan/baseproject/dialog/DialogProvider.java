package com.artemkopan.baseproject.dialog;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import com.artemkopan.baseproject.helper.Log;

/**
 * Created for medimee
 * by Kopan Artem on 09.10.2016.
 */

public class DialogProvider {

    private ProgressDialog mProgressDialog;
    private MessageDialog mMessageDialog;

    public void showProgressDialog(FragmentActivity activity, String description, Runnable actionRunnable) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }
        mProgressDialog = ProgressDialog.newInstance(description, actionRunnable);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show(activity.getSupportFragmentManager());
    }

    public void showProgressDialog(FragmentActivity activity, Runnable actionRunnable) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }
        mProgressDialog = ProgressDialog.newInstance(actionRunnable);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show(activity.getSupportFragmentManager());
    }

    public void showMessageDialog(@Nullable FragmentActivity activity, String title) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }
        showMessageDialog(activity, title, null);
    }

    public void showMessageDialog(@Nullable FragmentActivity activity, @StringRes int titleRes) {
        showMessageDialog(activity, titleRes, 0);
    }

    public void showMessageDialog(@Nullable FragmentActivity activity, @StringRes int titleRes,
                                  @StringRes int descriptionRes) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }
        showMessageDialog(activity, titleRes > 0 ? activity.getString(titleRes) : null,
                          descriptionRes > 0 ? activity.getString(descriptionRes) : null);
    }

    /**
     * Show message inform dialog; Usually use in warnings or errors;
     * Use on default {@link MessageDialog}, but you can set custom {@link MessageDialog}
     *
     * @param activity current CompatActivity
     * @return Dialog fragment; If Activity is null then return null!
     */
    public void showMessageDialog(@Nullable FragmentActivity activity, String title, String description) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }

        if (mMessageDialog != null && mMessageDialog.isShowing()) {
            mMessageDialog.setTitle(title);
            mMessageDialog.setDescription(description);
        } else {
            mMessageDialog = MessageDialog.newInstance(title, description);
            mMessageDialog.show(activity.getSupportFragmentManager());
        }
    }

    public void dismissMessageDialog() {
        if (mMessageDialog != null) {
            mMessageDialog.dismiss();
            mMessageDialog = null;
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    public MessageDialog getMessageDialog() {
        return mMessageDialog;
    }
}
