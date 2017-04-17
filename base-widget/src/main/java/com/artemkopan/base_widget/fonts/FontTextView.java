package com.artemkopan.base_widget.fonts;

import android.content.Context;
import android.util.AttributeSet;

import com.artemkopan.base_widget.vector.VectorCompatTextView;

/**
 * Created by Artem Kopan for MyMoodAndMe
 * 22.03.17
 */

public class FontTextView extends VectorCompatTextView {

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        FontUtils.applyCustomFont(this, getContext(), attrs);
    }
}
