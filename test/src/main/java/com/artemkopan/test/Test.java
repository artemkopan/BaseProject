package com.artemkopan.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Artem Kopan for BaseProject
 * 12.05.2017
 */

public class Test extends AppCompatImageView {

    public Test(Context context) {
        super(context);
    }

    public Test(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Test(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("TEST", "w " + MeasureSpec.toString(widthMeasureSpec) + " h " + MeasureSpec.toString(heightMeasureSpec));
        Log.i("TEST", "w " + getMeasuredWidth() + " h " + getMeasuredHeight());
    }
}
