package com.artemkopan.widget.fonts;

import android.content.Context;
import android.util.AttributeSet;

import com.artemkopan.widget.vector.VectorCompatButton;

/**
 * Created by Artem Kopan for MyMoodAndMe
 * 22.03.17
 */

public class FontButton extends VectorCompatButton {

    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        FontUtils.applyCustomFont(this, getContext(), attrs);
    }
}
