package com.artemkopan.baseproject.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.fragment.BaseDialogFragment;
import com.artemkopan.baseproject.fragment.BaseFragment;

import static butterknife.ButterKnife.findById;

@SuppressWarnings("WeakerAccess")
public class DialogController {

    private ProgressDialog mProgressDialog;
    private BaseDialogFragment mMessageDialog;

    public void onDestroy() {
        dismissProgressDialog();
        dismissMessageDialog();
    }

    public void dismissMessageDialog() {
        if (mMessageDialog != null) {
            mMessageDialog.dismiss();
            mMessageDialog = null;
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showProgressDialog(@Nullable Activity activity) {
        showProgressDialog(activity, R.string.base_info_loading);
    }

    public void showProgressDialog(@Nullable Activity activity, @StringRes int messageRes) {
        if (activity == null) {
            return;
        }
        showProgressDialog(activity, activity.getString(messageRes));
    }

    /**
     * Show default system progress dialog.
     * Dialog was be destroyed on {@link BaseFragment#onDestroy()} or {@link BaseActivity#onDestroy()}
     *
     * @param activity current activity
     * @param message  message (optional);
     */
    public void showProgressDialog(@Nullable Activity activity, String message) {
        if (activity == null) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
        } else if (mProgressDialog.isShowing()) {
            return;
        }

        mProgressDialog.show();
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

        if (mMessageDialog == null) {
            mMessageDialog = messageFragment;
        } else if ((mMessageDialog.isShowing())) {
            return null;
        }

        mMessageDialog.show(activity.getSupportFragmentManager());

        return mMessageDialog;
    }

    @SuppressWarnings("unused")
    public static class MessageDialog extends BaseDialogFragment {

        private static final String KEY_TITLE = "TITLE";
        private static final String KEY_DESCRIPTION = "DESCRIPTION";

        private Runnable mOnDismissAction;

        public static MessageDialog newInstance(String title, String description) {
            Bundle args = new Bundle();
            args.putString(KEY_TITLE, title);
            args.putString(KEY_DESCRIPTION, description);
            MessageDialog fragment = new MessageDialog();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onBaseDialogSize(Window window) {
            window.setLayout(
                    (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.8),
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.base_dialog_message, container, false);
            String title = getArguments().getString(KEY_TITLE);
            String description = getArguments().getString(KEY_DESCRIPTION);

            TextView titleView = findById(view, R.id.base_dialog_message_title);
            titleView.setText(title);
            TextView descriptionView = findById(view, R.id.base_dialog_message_description);
            descriptionView.setText(description);

            findById(view, R.id.base_dialog_message_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissAllowingStateLoss();
                }
            });

            return view;
        }

        @Override
        public int onCreateInflateView() {
            return 0;
        }

        public void setOnDismissAction(Runnable onDismissAction) {
            mOnDismissAction = onDismissAction;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (mOnDismissAction != null) {
                mOnDismissAction.run();
            }
            super.onDismiss(dialog);
        }
    }
}



