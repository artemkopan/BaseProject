package co.inteza.all.dialog;

import android.app.Dialog;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.artemkopan.mvp.dialog.BaseDialogFragment;
import com.artemkopan.mvp.presenter.BasePresenter;

import co.inteza.all.R;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), getTheme());
    }

    @Override
    public void onBaseDialogBackground(Window window) {
        super.onBaseDialogBackground(window);
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            window.addFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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

    @Nullable
    @Override
    public BasePresenter getPresenter() {
        return null;
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
