package com.artemkopan.baseproject.dialog;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.helper.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created for medimee
 * by Kopan Artem on 09.10.2016.
 */

public class DialogProvider {

    private InfoDialog mInfoDialog;
    private AtomicBoolean mDismissCall = new AtomicBoolean(false);

    public InfoDialog showProgressDialog(FragmentActivity activity) {
        return showProgressDialog(activity, null);
    }

    public InfoDialog showProgressDialog(FragmentActivity activity, final OnClickListener actionClick) {
        if (activityIsNull(activity)) {
            return mInfoDialog;
        }

        return showProgressDialog(activity,
                                  activity.getString(R.string.base_info_loading),
                                  activity.getString(R.string.cancel),
                                  actionClick);
    }

    public InfoDialog showProgressDialog(FragmentActivity activity, String description,
                                         final OnClickListener actionClick) {
        if (activityIsNull(activity)) {
            return mInfoDialog;
        }

        return showProgressDialog(activity,
                                  description,
                                  activity.getString(R.string.cancel),
                                  actionClick);
    }

    public InfoDialog showProgressDialog(FragmentActivity activity,
                                         final String description,
                                         final String action,
                                         final OnClickListener actionClick) {
        if (activityIsNull(activity)) {
            return mInfoDialog;
        }

        if (mDismissCall.compareAndSet(true, false) || dialogInactive()) {
            mInfoDialog = InfoDialog.newInstance(description, action, true)
                    .setActionClick(new WeakReference<>(actionClick));
            mInfoDialog.show(activity.getSupportFragmentManager());
        } else {
            mInfoDialog.setDescription(description);
            mInfoDialog.setAction(action, new WeakReference<>(actionClick));
            mInfoDialog.showProgress();
        }
        mInfoDialog.setCancelable(false);
        return mInfoDialog;
    }

    public InfoDialog showMessageDialog(@Nullable FragmentActivity activity, String title) {
        return showMessageDialog(activity, title, null, null);
    }

    public InfoDialog showMessageDialog(@Nullable FragmentActivity activity, @StringRes int titleRes) {
        if (activityIsNull(activity)) {
            return mInfoDialog;
        }
        //noinspection ConstantConditions
        return showMessageDialog(activity, activity.getString(titleRes), null, null);
    }

    /**
     * Show message inform dialog; Usually use in warnings or errors;
     * Use on default {@link InfoDialog}
     *
     * @param activity current CompatActivity
     * @return Dialog fragment
     */
    @SuppressWarnings("ConstantConditions")
    public InfoDialog showMessageDialog(@Nullable FragmentActivity activity,
                                        final String description,
                                        final String action,
                                        final OnClickListener actionClick) {
        if (activityIsNull(activity)) {
            return mInfoDialog;
        }

        if (mDismissCall.compareAndSet(true, false) || dialogInactive()) {
            mInfoDialog = InfoDialog.newInstance(description, action, false)
                    .setActionClick(new WeakReference<>(actionClick));
            mInfoDialog.show(activity.getSupportFragmentManager());
        } else {
            mInfoDialog.setDescription(description);
            mInfoDialog.setAction(action, new WeakReference<>(actionClick));
            mInfoDialog.showMessage();
        }
        mInfoDialog.setCancelable(true);
        return mInfoDialog;
    }

    public void dismissDialog() {
        mDismissCall.set(true);
        if (!dialogInactive()) {
            mInfoDialog.dismissAllowingStateLoss();
        }
    }

    public void setCancelable(boolean cancelable) {
        if (mInfoDialog != null) mInfoDialog.setCancelable(cancelable);
    }

    private boolean dialogInactive() {
        return mInfoDialog == null || !mInfoDialog.isShowing();
    }

    private boolean activityIsNull(Activity activity) {
        if (activity == null) {
            Log.w("Activity is null!");
            return true;
        }
        return false;
    }

}
