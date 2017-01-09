package com.artemkopan.baseproject.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artemkopan.baseproject.R;

/**
 * Created by Artem Kopan for BaseProject
 * 09.01.2017
 */

public class MessageDialog extends BaseDialogFragment {

    private static final String KEY_TITLE = "KEY.TITLE";
    private static final String KEY_DESCRIPTION = "KEY.DESCRIPTION";

    private TextView titleTxt;
    private TextView descriptionTxt;

    private String title;
    private String description;

    public static MessageDialog newInstance(String title, String description) {
        MessageDialog fragment = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onBaseBottomSheetDialog() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(KEY_TITLE);
        description = getArguments().getString(KEY_DESCRIPTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_dialog_message, container, false);
        titleTxt = (TextView) rootView.findViewById(R.id.title_txt);
        descriptionTxt = (TextView) rootView.findViewById(R.id.description_txt);
        setTitle(title);
        setDescription(description);
        return rootView;
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            titleTxt.setVisibility(View.GONE);
        } else {
            titleTxt.setText(title);
            titleTxt.setVisibility(View.VISIBLE);
        }
    }

    public void setDescription(String description) {
        descriptionTxt.setText(description);
    }

    @Override
    public int onInflateLayout() {
        return 0;
    }
}
