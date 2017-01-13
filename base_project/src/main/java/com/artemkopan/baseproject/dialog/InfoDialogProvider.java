package com.artemkopan.baseproject.dialog;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.artemkopan.baseproject.dialog.InfoDialog.REQ_CODE;

/**
 * Created for medimee
 * by Kopan Artem on 09.10.2016.
 */

@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
public class InfoDialogProvider {

    private InfoDialog mInfoDialog;
    private AtomicBoolean mDismissCall = new AtomicBoolean(false);

    public InfoDialog showProgress(Object obj) {
        return showProgress(obj, R.string.base_info_loading);
    }

    public InfoDialog showProgress(Object obj, @StringRes int description) {
        if (obj instanceof Fragment) {
            return showDialog((Fragment) obj, description, true);
        } else if (obj instanceof FragmentActivity) {
            return showDialog((FragmentActivity) obj, description, true);
        } else {
            throw new IllegalArgumentException("obj must be instance of Fragment or FragmentActivity");
        }
    }

    public InfoDialog showProgress(Object obj, String description) {
        if (obj instanceof Fragment) {
            return showDialog((Fragment) obj, description, true);
        } else if (obj instanceof FragmentActivity) {
            return showDialog((FragmentActivity) obj, description, true);
        } else {
            throw new IllegalArgumentException("obj must be instance of Fragment or FragmentActivity");
        }
    }

    public InfoDialog showMessageDialog(Object obj, @StringRes int description) {
        if (obj instanceof Fragment) {
            return showDialog((Fragment) obj, description, false);
        } else if (obj instanceof FragmentActivity) {
            return showDialog((FragmentActivity) obj, description, false);
        } else {
            throw new IllegalArgumentException("obj must be instance of Fragment or FragmentActivity");
        }
    }

    public InfoDialog showMessageDialog(Object obj, String description) {
        if (obj instanceof Fragment) {
            return showDialog((Fragment) obj, description, false);
        } else if (obj instanceof FragmentActivity) {
            return showDialog((FragmentActivity) obj, description, false);
        } else {
            throw new IllegalArgumentException("obj must be instance of Fragment or FragmentActivity");
        }
    }

    public InfoDialog showDialog(@Nullable Fragment fragment, @StringRes int descriptionRes, boolean isProgress) {
        if (checkNull(fragment)) {
            return mInfoDialog;
        }
        return showDialog(fragment, fragment.getString(descriptionRes), isProgress);
    }

    public InfoDialog showDialog(@Nullable Fragment fragment, String description, boolean isProgress) {
        InfoDialog infoDialog = showDialog(fragment.getFragmentManager(), description, isProgress);
        infoDialog.setTargetFragment(fragment, REQ_CODE);
        return infoDialog;
    }

    public InfoDialog showDialog(@Nullable FragmentActivity activity, @StringRes int descriptionRes, boolean isProgress) {
        if (checkNull(activity)) {
            return mInfoDialog;
        }
        return showDialog(activity, activity.getString(descriptionRes), isProgress);
    }

    public InfoDialog showDialog(@Nullable FragmentActivity activity, String description, boolean isProgress) {
        if (checkNull(activity)) {
            return mInfoDialog;
        }
        return showDialog(activity.getSupportFragmentManager(), description, isProgress);
    }

    /**
     * Show message inform dialog; Usually use in warnings or errors;
     * Use on default {@link InfoDialog}
     *
     * @return Dialog fragment
     */
    @SuppressWarnings("ConstantConditions")
    public InfoDialog showDialog(FragmentManager fragmentManager,
                                 final String description,
                                 boolean isProgress) {
        if (mDismissCall.compareAndSet(true, false) || dialogInactive()) {
            mInfoDialog = InfoDialog.newInstance(description, isProgress);
            mInfoDialog.show(fragmentManager);
        } else {
            mInfoDialog.setDescription(description);
            mInfoDialog.showProgress(isProgress);
        }
        mInfoDialog.setCancelable(!isProgress);

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

    private boolean checkNull(Object o) {
        if (o == null) {
            Log.w("Activity is null!");
            return true;
        }
        return false;
    }

}
