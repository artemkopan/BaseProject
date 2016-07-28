package com.artemkopan.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.artemkopan.baseproject.R;


public class SquareImageView extends AppCompatImageView {

    private boolean mUseHeight;

    public SquareImageView(Context context) {
        this(context, null, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SquareImageView);

        try {
            mUseHeight = array.getBoolean(R.styleable.SquareImageView_siv_by_height, false);
        } finally {
            array.recycle();
        }
    }

    public void setUseHeight(boolean mUseHeight) {
        this.mUseHeight = mUseHeight;
        requestLayout();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (mUseHeight) {
            setMeasuredDimension(height, height);
        } else {
            setMeasuredDimension(width, width);
        }
    }

}
