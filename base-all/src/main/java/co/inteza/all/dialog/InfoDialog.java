package co.inteza.all.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.artemkopan.mvp.dialog.BaseDialogFragment;
import com.artemkopan.utils.ExtraUtils;

import co.inteza.all.R;

/**
 * Base info dialog with indeterminate progress bar.
 * You {@link #getRootClass(Class)} must implement {@link OnDismissListener} for dismiss event;
 */
public class InfoDialog extends BaseDialogFragment {

    public static final int REQ_CODE = 12312;
    private static final String KEY_DESCRIPTION = "DESCRIPTION";
    private static final String KEY_PROGRESS = "PROGRESS";
    private View progressView;
    private TextView descriptionTxt;

    public static InfoDialog newInstance(String description, boolean showProgress) {
        InfoDialog fragment = new InfoDialog();
        Bundle args = new Bundle();
        args.putString(KEY_DESCRIPTION, description);
        args.putBoolean(KEY_PROGRESS, showProgress);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onBaseDialogSize(Window window) {
        window.setLayout(ExtraUtils.getDialogWidth(getContext()), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            setDescription(savedInstanceState.getString(KEY_DESCRIPTION));
            showProgress(savedInstanceState.getBoolean(KEY_PROGRESS, true));
        }
    }

    @Override
    public int onInflateLayout() {
        return R.layout.base_dialog_info;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressView = view.findViewById(R.id.progress_bar);
        descriptionTxt = (TextView) view.findViewById(R.id.description_txt);
        Bundle args = getArguments();
        showProgress(args.getBoolean(KEY_PROGRESS, true));
        setDescription(args.getString(KEY_DESCRIPTION));
    }

    public void showProgress(boolean show) {
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public InfoDialog setDescription(String value) {
        descriptionTxt.setText(value);
        return this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_DESCRIPTION, descriptionTxt.getText().toString());
        outState.putBoolean(KEY_PROGRESS, progressView.getVisibility() == View.VISIBLE);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        OnDismissListener onDismissListener = (OnDismissListener) getRootClass(OnDismissListener.class);
        if (onDismissListener != null) onDismissListener.onDismiss(dialog);
    }
}
