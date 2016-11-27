package com.artemkopan.baseproject.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextSwitcher;
import android.widget.TextView;

/**
 * Created for BaseProject
 * by Kopan Artem on 27.11.2016.
 */

public class ExTextSwitcher extends TextSwitcher {


    public ExTextSwitcher(Context context) {
        super(context);
    }

    public ExTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Set new text. If equals true, then new text was checked with previous and ignore if texts similar
     */
    public void setText(CharSequence text, boolean equals) {
        if (!equals) {
            super.setText(text);
            return;
        }
        if (!TextUtils.equals(text, ((TextView) getNextView()).getText())) {
            super.setText(text);
        }
    }


    public CharSequence getText() {
        return ((TextView) getCurrentView()).getText();
    }
}
