package co.inteza.all.dialog;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import android.util.Log;

import com.artemkopan.mvp.presentation.Presentation;

import co.inteza.all.R;

/**
 * Created by Artem Kopan for BaseProject
 * 09.10.2016
 */

@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
public class DialogProvider {

    private static final String TAG = "DialogProvider";
    private ProgressDialog progressDialog;
    private MessageDialog messageDialog;

    //==============================================================================================
    // Progress Dialog
    //==============================================================================================
    //region methods
    public ProgressDialog showProgress(Presentation presentation) {
        return showProgress(presentation, R.string.base_info_loading);
    }

    public ProgressDialog showProgress(Presentation presentation, @StringRes int description) {
        return showProgress(presentation,
                            description > 0 ? presentation.getBaseActivity().getString(description) : null);
    }

    public ProgressDialog showProgress(Presentation presentation, String description) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            if (progressDialog != null) progressDialog.dismiss();
            progressDialog = ProgressDialog.newInstance(description);
            progressDialog.show(presentation.getSupportFragmentManager());
        } else {
            progressDialog.setDescription(description);
        }
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    //endregion

    public void dismissProgress() {
        dismissDialog(progressDialog);
        progressDialog = null;
    }

    //==============================================================================================
    // Message Dialog
    //==============================================================================================
    //region methods
    public MessageDialog showMessage(Presentation presentation, @StringRes int description) {
        return showMessage(presentation, R.string.base_info_something_wrong, description);
    }

    public MessageDialog showMessage(Presentation presentation, String description) {
        return showMessage(presentation, presentation.getBaseActivity()
                                                     .getString(R.string.base_info_something_wrong), description);
    }

    public MessageDialog showMessage(Presentation presentation, @StringRes int title, @StringRes int description) {
        return showMessage(presentation,
                           title > 0 ? presentation.getBaseActivity().getString(title) : null,
                           description > 0 ? presentation.getBaseActivity().getString(description) : null);
    }
    //endregion

    @SuppressWarnings("ConstantConditions")
    public MessageDialog showMessage(Presentation presentation, final String title, final String description) {
        if (messageDialog == null || !messageDialog.isShowing()) {
            if (messageDialog != null) messageDialog.dismiss();
            messageDialog = MessageDialog.newInstance(title, description);
            messageDialog.show(presentation.getSupportFragmentManager());
        } else {
            messageDialog.setTitle(title);
            messageDialog.setDescription(description);
        }
        return messageDialog;
    }

    public void dismissMessage() {
        dismissDialog(messageDialog);
        messageDialog = null;
    }

    //==============================================================================================
    // Common
    //==============================================================================================
    private void dismissDialog(@Nullable DialogFragment dialogFragment) {
        if (dialogFragment == null) return;
        try {
            dialogFragment.dismissAllowingStateLoss();
        } catch (Exception ex) {
            Log.e(TAG, "dismissProgress()", ex);
        }
    }

}
