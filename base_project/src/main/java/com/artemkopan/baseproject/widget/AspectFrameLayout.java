package com.artemkopan.baseproject.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;


/**
 * Created for jabrool
 * by Kopan Artem on 04.11.2016.
 */

public class AspectFrameLayout extends FrameLayout {

    private AspectViewHelper aspectViewHelper;

    public AspectFrameLayout(Context context) {
        this(context, null, 0);
    }

    public AspectFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractCustomAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AspectFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        extractCustomAttrs(context, attrs);

    }

    private void extractCustomAttrs(Context context, AttributeSet attrs) {
        aspectViewHelper = new AspectViewHelper();
        aspectViewHelper.loadFromAttributes(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Point point = aspectViewHelper.calculateNewMeasure(this);
        super.onMeasure(point.x, point.y);
    }
}
