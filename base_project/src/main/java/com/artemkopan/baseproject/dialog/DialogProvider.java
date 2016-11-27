package com.artemkopan.baseproject.dialog;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.utils.RunnableValue;

/**
 * Created for medimee
 * by Kopan Artem on 09.10.2016.
 */

public class DialogProvider {

    private InfoDialog mInfoDialog;

    public void showProgressDialog(FragmentActivity activity, final OnClickListener actionClick) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }
        showProgressDialog(activity,
                           activity.getString(R.string.base_info_loading),
                           activity.getString(R.string.cancel),
                           actionClick);
    }


    public void showProgressDialog(FragmentActivity activity,
                                   final String description,
                                   final String action,
                                   final OnClickListener actionClick) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }
        if (dialogInactive()) {
            mInfoDialog = InfoDialog.newInstance(new RunnableValue<InfoDialog>() {
                @Override
                public void run(InfoDialog dialog) {
                    mInfoDialog.showProgress();
                    mInfoDialog.setDescription(description);
                    mInfoDialog.setAction(action, actionClick);
                }
            });
            mInfoDialog.show(activity.getSupportFragmentManager());
        } else {
            mInfoDialog.showProgress();
            mInfoDialog.setDescription(description);
            mInfoDialog.setAction(action, actionClick);
        }
        mInfoDialog.setCancelable(false);
    }


    public void showMessageDialog(@Nullable FragmentActivity activity, String title) {
        showMessageDialog(activity, title, null, null);
    }

    public void showMessageDialog(@Nullable FragmentActivity activity, @StringRes int titleRes) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }
        showMessageDialog(activity, activity.getString(titleRes), null, null);
    }

    /**
     * Show message inform dialog; Usually use in warnings or errors;
     * Use on default {@link MessageDialog}, but you can set custom {@link MessageDialog}
     *
     * @param activity current CompatActivity
     * @return Dialog fragment; If Activity is null then return null!
     */
    public void showMessageDialog(@Nullable FragmentActivity activity,
                                  final String description,
                                  final String action,
                                  final OnClickListener onClickListener) {
        if (activity == null) {
            Log.d("Activity is null!");
            return;
        }

        if (dialogInactive()) {
            mInfoDialog = InfoDialog.newInstance(new RunnableValue<InfoDialog>() {
                @Override
                public void run(InfoDialog dialog) {
                    mInfoDialog.showMessage();
                    mInfoDialog.setDescription(description);
                    mInfoDialog.setAction(action, onClickListener);
                }
            });
            mInfoDialog.show(activity.getSupportFragmentManager());
        } else {
            mInfoDialog.showMessage();
            mInfoDialog.setDescription(description);
            mInfoDialog.setAction(action, onClickListener);
        }
        mInfoDialog.setCancelable(true);
    }

    public void dismissDialog() {
        if (!dialogInactive()) {
            mInfoDialog.dismiss();
            mInfoDialog = null;
        }
    }

    private boolean dialogInactive() {
        return mInfoDialog == null || !mInfoDialog.isShowing();
    }
}
