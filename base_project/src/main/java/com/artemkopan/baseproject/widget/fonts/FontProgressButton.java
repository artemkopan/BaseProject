package com.artemkopan.baseproject.widget.fonts;

import android.content.Context;
import android.util.AttributeSet;

import com.artemkopan.baseproject.utils.fonts.FontUtils;
import com.artemkopan.baseproject.widget.ProgressButtonView;

/**
 * Created by Artem Kopan for MyMoodAndMe
 * 22.03.17
 */

public class FontProgressButton extends ProgressButtonView {

    public FontProgressButton(Context context) {
        super(context);
    }

    public FontProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        FontUtils.applyCustomFont(this, getContext(), attrs);
    }
}
