package com.artemkopan.baseproject.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.widget.CircularProgressView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * @author Created by artem on 09.01.2017.
 */

public class ProgressDialog extends BaseDialogFragment {

    private static final String KEY_DESCRIPTION = "KEY.DESCRIPTION";
    private CircularProgressView progressView;
    private TextView progressTxt;
    private String description;

    public static ProgressDialog newInstance(String description) {
        ProgressDialog fragment = new ProgressDialog();
        Bundle args = new Bundle();
        args.putString(KEY_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        description = getArguments().getString(KEY_DESCRIPTION);
    }

    public void onBaseDialogSize(Window window) {
        window.setLayout(MATCH_PARENT, MATCH_PARENT);
    }

    @Override
    public void onBaseDialogAnim(Window window) {
        window.getAttributes().windowAnimations = R.style.DialogAnimationAlpha;
    }

    @Override
    public int onInflateLayout() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_dialog_progress, container, false);
        progressView = (CircularProgressView) rootView.findViewById(R.id.progress_view);
        progressTxt = (TextView) rootView.findViewById(R.id.progress_txt);
        progressTxt.setText(description);
        return rootView;
    }

    public void setDescription(String description) {
        progressTxt.setText(description);
    }
}
