package com.artemkopan.widget.fonts;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;

/**
 * Created by Artem Kopan for BaseProject
 * 30.05.17
 */

public class FontCheckedTextView extends AppCompatCheckedTextView {

    public FontCheckedTextView(Context context) {
        super(context);
    }

    public FontCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        FontUtils.applyCustomFont(this, getContext(), attrs);
    }

}
