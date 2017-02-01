package com.artemkopan.baseproject.dialog;

import android.support.annotation.StringRes;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.internal.UiInterface;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

/**
 * Created by Artem Kopan for BaseProject
 * 09.10.2016
 */

@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
public class DialogProvider {

    private ProgressDialog progressDialog;
    private MessageDialog messageDialog;

    //==============================================================================================
    // Progress Dialog
    //==============================================================================================
    //region methods
    public ProgressDialog showProgress(UiInterface uiInterface) {
        return showProgress(uiInterface, R.string.base_info_loading);
    }

    public ProgressDialog showProgress(UiInterface uiInterface, @StringRes int description) {
        return showProgress(uiInterface, description > 0 ? uiInterface.getBaseActivity().getString(description) : null);
    }

    public ProgressDialog showProgress(UiInterface uiInterface, String description) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            if (progressDialog != null) progressDialog.dismiss();
            progressDialog = ProgressDialog.newInstance(description);
            progressDialog.show(uiInterface.getSupportFragmentManager());
        } else {
            progressDialog.setDescription(description);
        }
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public void dismissProgress() {
        boolean destroyed = false;
        if (progressDialog != null && SDK_INT >= JELLY_BEAN_MR1 && progressDialog.getActivity() != null) {
            destroyed = progressDialog.getBaseActivity().isDestroyed();
        }
        if (progressDialog != null && progressDialog.getActivity() != null && !destroyed) {
            progressDialog.dismissAllowingStateLoss();
            progressDialog = null;
        }
    }
    //endregion

    //==============================================================================================
    // Message Dialog
    //==============================================================================================
    //region methods
    public MessageDialog showMessage(UiInterface uiInterface, @StringRes int description) {
        return showMessage(uiInterface, R.string.base_info_something_wrong, description);
    }

    public MessageDialog showMessage(UiInterface uiInterface, String description) {
        return showMessage(uiInterface, uiInterface.getBaseActivity()
                                                   .getString(R.string.base_info_something_wrong), description);
    }

    public MessageDialog showMessage(UiInterface uiInterface, @StringRes int title, @StringRes int description) {
        return showMessage(uiInterface,
                           title > 0 ? uiInterface.getBaseActivity().getString(title) : null,
                           description > 0 ? uiInterface.getBaseActivity().getString(description) : null);
    }

    @SuppressWarnings("ConstantConditions")
    public MessageDialog showMessage(UiInterface uiInterface, final String title, final String description) {
        if (messageDialog == null || !messageDialog.isShowing()) {
            if (messageDialog != null) messageDialog.dismiss();
            messageDialog = MessageDialog.newInstance(title, description);
            messageDialog.show(uiInterface.getSupportFragmentManager());
        } else {
            messageDialog.setTitle(title);
            messageDialog.setDescription(description);
        }
        return messageDialog;
    }
    //endregion

    public void dismissMessage() {
        boolean destroyed = false;
        if (messageDialog != null && SDK_INT >= JELLY_BEAN_MR1 && messageDialog.getActivity() != null) {
            destroyed = messageDialog.getActivity().isDestroyed();
        }
        if (messageDialog != null && messageDialog.getActivity() != null && !destroyed) {
            messageDialog.dismissAllowingStateLoss();
            messageDialog = null;
        }
    }
}
