package com.artemkopan.baseproject.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.RunnableValue;
import com.artemkopan.baseproject.widget.ExTextSwitcher;

/**
 * Created for BaseProject
 * by Kopan Artem on 27.11.2016.
 */

public class InfoDialog extends BaseDialogFragment {

    private static final String KEY_DESCRIPTION = "DESCRIPTION";
    private static final String KEY_ACTION = "ACTION";
    private View progressView;
    private ExTextSwitcher descriptionTxt, actionBtn;
    private RunnableValue<InfoDialog> fragmentReady;

    public static InfoDialog newInstance(RunnableValue<InfoDialog> fragmentReady) {
        InfoDialog fragment = new InfoDialog();
        fragment.fragmentReady = fragmentReady;
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
            descriptionTxt.setText(savedInstanceState.getString(KEY_DESCRIPTION));
            actionBtn.setText(savedInstanceState.getString(KEY_ACTION));
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

        if (fragmentReady != null) fragmentReady.run(this);
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
        actionBtn.setVisibility(onActionClick == null ? View.GONE : View.VISIBLE);
        actionBtn.setOnClickListener(onActionClick);
        actionBtn.setText(value, true);
        return this;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_DESCRIPTION, descriptionTxt.getText().toString());
        outState.putString(KEY_ACTION, actionBtn.getText().toString());
    }
}
