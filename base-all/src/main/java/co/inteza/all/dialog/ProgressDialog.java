package co.inteza.all.dialog;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.artemkopan.mvp.dialog.BaseDialogFragment;

import co.inteza.all.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * @author Created by artem on 09.01.2017.
 */

public class ProgressDialog extends BaseDialogFragment {

    private static final String KEY_DESCRIPTION = "KEY.DESCRIPTION";
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
    public void onBaseDialogBackground(Window window) {
        window.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.color.dialog_progress_background));
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            window.setFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS, LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onBaseDialogAnim(Window window) {
        window.getAttributes().windowAnimations = R.style.DialogAnimationAlphaFast;
    }

    @Override
    public int onInflateLayout() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_dialog_progress, container, false);
        progressTxt = (TextView) rootView.findViewById(R.id.progress_txt);
        progressTxt.setText(description);
        return rootView;
    }

    public void setDescription(String description) {
        progressTxt.setText(description);
    }
}
