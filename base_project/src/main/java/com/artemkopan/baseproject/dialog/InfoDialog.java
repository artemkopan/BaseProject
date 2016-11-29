package com.artemkopan.baseproject.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.widget.ExTextSwitcher;

/**
 * Created for BaseProject
 * by Kopan Artem on 27.11.2016.
 */

public class InfoDialog extends BaseDialogFragment {

    private static final String KEY_DESCRIPTION = "DESCRIPTION";
    private static final String KEY_ACTION = "ACTION";
    private static final String KEY_PROGRESS = "PROGRESS";
    private View progressView;
    private ExTextSwitcher descriptionTxt, actionBtn;
    private OnClickListener actionClickListener;

    public static InfoDialog newInstance(String description, String action, boolean showProgress) {
        InfoDialog fragment = new InfoDialog();
        Bundle args = new Bundle();
        args.putString(KEY_DESCRIPTION, description);
        args.putString(KEY_ACTION, action);
        args.putBoolean(KEY_PROGRESS, showProgress);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onBaseDialogSize(Window window) {
        window.setLayout(
                (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.8),
                WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            setDescription(savedInstanceState.getString(KEY_DESCRIPTION));
            setAction(savedInstanceState.getString(KEY_ACTION), null);
            if (savedInstanceState.getBoolean(KEY_PROGRESS)) {
                showProgress();
            } else {
                showMessage();
            }
        }
    }

    @Override
    public int onCreateInflateView() {
        return R.layout.base_dialog_info;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressView = view.findViewById(R.id.progress_view);
        descriptionTxt = (ExTextSwitcher) view.findViewById(R.id.description_txt);
        actionBtn = (ExTextSwitcher) view.findViewById(R.id.action_btn);
        Bundle args = getArguments();
        if (args.getBoolean(KEY_PROGRESS, true)) {
            showProgress();
        } else {
            showMessage();
        }
        setDescription(args.getString(KEY_DESCRIPTION));
        setAction(args.getString(KEY_ACTION), actionClickListener);
    }

    public void showProgress() {
        progressView.setVisibility(View.VISIBLE);
    }

    public void showMessage() {
        progressView.setVisibility(View.GONE);
    }

    public InfoDialog setDescription(String value) {
        descriptionTxt.setText(value, true);
        return this;
    }

    public InfoDialog setAction(String value, OnClickListener onActionClick) {
        actionBtn.setText(value, true);
        setActionClick(onActionClick);
        return this;
    }


    public InfoDialog setActionClick(OnClickListener onActionClick) {
        actionClickListener = onActionClick;
        if (actionBtn != null) {
            actionBtn.setVisibility(onActionClick == null ? View.GONE : View.VISIBLE);
            actionBtn.setOnClickListener(onActionClick);
        }
        return this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_DESCRIPTION, descriptionTxt.getText().toString());
        outState.putString(KEY_ACTION, actionBtn.getText().toString());
        outState.putBoolean(KEY_PROGRESS, getArguments().getBoolean(KEY_PROGRESS, true));
    }
}
