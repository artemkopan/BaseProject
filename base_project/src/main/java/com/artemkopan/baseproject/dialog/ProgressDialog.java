package com.artemkopan.baseproject.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.rx.RxViewClick;
import com.artemkopan.baseproject.utils.StringUtils;
import com.artemkopan.baseproject.widget.CircularProgressView;

import io.reactivex.functions.Consumer;

/**
 * Created for BaseProject
 * by Kopan Artem on 09.10.2016.
 */

public class ProgressDialog extends BaseDialogFragment {


    private static final String KEY_DESCRIPTION = "KEY.DESCRIPTION";

    private CircularProgressView mProgressView;
    private TextView mDescriptionTxt;
    private TextView mCancelBtn;
    private String mDescription;
    private Runnable mCancelAction;

    public static ProgressDialog newInstance(Runnable cancelAction) {
        ProgressDialog fragment = new ProgressDialog();
        fragment.mCancelAction = cancelAction;
        return fragment;
    }

    public static ProgressDialog newInstance(String description, Runnable cancelAction) {
        ProgressDialog fragment = new ProgressDialog();
        Bundle args = new Bundle();
        args.putString(KEY_DESCRIPTION, description);
        fragment.setArguments(args);
        fragment.mCancelAction = cancelAction;
        return fragment;
    }

    @Override
    public void onBaseDialogSize(Window window) {
        window.setLayout(
                (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.8),
                WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(KEY_DESCRIPTION)) {
            mDescription = getArguments().getString(KEY_DESCRIPTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_dialog_progress, container, false);

        mProgressView = (CircularProgressView) view.findViewById(R.id.progress_view);
        mDescriptionTxt = (TextView) view.findViewById(R.id.description_txt);
        mCancelBtn = (TextView) view.findViewById(R.id.cancel_btn);

        if (StringUtils.isEmpty(mDescription)) {
            mDescriptionTxt.setText(R.string.base_info_loading);
        } else {
            mDescriptionTxt.setText(mDescription);
        }

        //noinspection unchecked
        RxViewClick.create(mCancelBtn, mDestroySubject).subscribe(new Consumer<View>() {
            @Override
            public void accept(View view) throws Exception {
                if (mCancelAction != null) {
                    mCancelAction.run();
                }
                dismiss();
            }
        });

        return view;
    }

    public TextView getDescriptionTxt() {
        return mDescriptionTxt;
    }

    public TextView getCancelBtn() {
        return mCancelBtn;
    }

    public CircularProgressView getProgressView() {
        return mProgressView;
    }

    @Override
    public int onCreateInflateView() {
        return 0;
    }
}
