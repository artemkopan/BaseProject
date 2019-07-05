package com.artemkopan.widget.aspect;

import android.content.Context;
import android.graphics.Point;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Artem Kopan for jabrool
 * 06.12.16
 */

public class AspectImageView extends AppCompatImageView {

    private AspectViewHelper aspectViewHelper;

    public AspectImageView(Context context) {
        this(context, null);
    }

    public AspectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
