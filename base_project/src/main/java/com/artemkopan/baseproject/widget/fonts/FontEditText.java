package com.artemkopan.baseproject.widget.fonts;

import android.content.Context;
import android.util.AttributeSet;

import com.artemkopan.baseproject.utils.fonts.FontUtils;
import com.artemkopan.baseproject.widget.VectorCompatEditTextView;

/**
 * Created by Artem Kopan for MyMoodAndMe
 * 22.03.17
 */

public class FontEditText extends VectorCompatEditTextView {

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        FontUtils.applyCustomFont(this, getContext(), attrs);
    }
}

