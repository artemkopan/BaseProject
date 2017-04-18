package com.artemkopan.widget.vector;

import android.content.Context;
import android.util.AttributeSet;

import com.artemkopan.widget.common.ErrorEditText;

/**
 * Created by Artem Kopan for jabrool
 * 30.12.16
 */

public class VectorCompatEditTextView extends ErrorEditText {

    public VectorCompatEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VectorCompatEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        VectorCompatViewHelper.loadFromAttributes(this, attrs);
    }
}
