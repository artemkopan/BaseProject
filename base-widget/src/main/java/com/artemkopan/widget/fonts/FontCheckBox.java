package com.artemkopan.widget.fonts;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * Created by Artem Kopan for BaseProject
 * 30.05.17
 */

public class FontCheckBox extends AppCompatCheckBox {

    public FontCheckBox(Context context) {
        super(context);
    }

    public FontCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        FontUtils.applyCustomFont(this, getContext(), attrs);
    }

}
