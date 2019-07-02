package com.artemkopan.widget.vector;

import android.content.Context;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Artem Kopan for jabrool
 * 30.12.16
 */

public class VectorCompatCheckedTextView extends AppCompatCheckedTextView {

    public VectorCompatCheckedTextView(Context context) {
        super(context);
    }

    public VectorCompatCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VectorCompatCheckedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        VectorCompatViewHelper.loadFromAttributes(this, attrs);
    }
}
