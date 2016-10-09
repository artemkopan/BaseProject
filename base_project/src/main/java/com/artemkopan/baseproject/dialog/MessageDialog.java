package com.artemkopan.baseproject.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.StringUtils;

import static butterknife.ButterKnife.findById;

/**
 * Created for BaseProject
 * Author: Kopan Artem
 * 22.08.2016
 */
@SuppressWarnings("unused")
public class MessageDialog extends BaseDialogFragment {

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_DESCRIPTION = "DESCRIPTION";

    private Runnable mOnDismissAction;
    private TextView mTitleTxt;
    private TextView mDescriptionTxt;

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

        mTitleTxt = findById(view, R.id.base_dialog_message_title);
        mDescriptionTxt = findById(view, R.id.base_dialog_message_description);

        mTitleTxt.setText(title);

        if (StringUtils.isEmpty(description)) {
            mDescriptionTxt.setVisibility(View.GONE);
        } else {
            mDescriptionTxt.setVisibility(View.VISIBLE);
            mDescriptionTxt.setText(description);
        }

        findById(view, R.id.base_dialog_message_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });

        return view;
    }

    public TextView getTitleTxt() {
        return mTitleTxt;
    }

    public TextView getDescriptionTxt() {
        return mDescriptionTxt;
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
