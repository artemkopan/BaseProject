package com.artemkopan.base_widget.vector;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Artem Kopan for jabrool
 * 30.12.16
 */

public class VectorCompatButton extends AppCompatButton {

    public VectorCompatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VectorCompatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        VectorCompatViewHelper.loadFromAttributes(this, attrs);
    }
}
