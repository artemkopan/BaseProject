package com.artemkopan.baseproject.widget.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.utils.ViewUtils;


/**
 * Created for it-forum-2016.
 * Author Artem Kopan.
 * Date 08.09.2016 11:00
 * <p>
 * <b><p>Attributes:</p></b>
 * <p><b>vpb_end_view</b> - id of end view; From end view was get <b>relative X/Y position</b> and <b>Width/Height</b>;
 * <p><b>vpb_end_width</b> - set end width size;
 * <p><b>vpb_end_height</b> - set end height size;
 * <p><b>vpb_end_x</b> - set end X position;
 * <p><b>vpb_end_y</b> - set end Y position;
 * <p><b>DON'T USE WITH</b> app:layout_anchor because {@link #layoutDependsOn} enter in infinity cycle
 */
@SuppressWarnings("unused")
public class ViewPositionBehavior extends CoordinatorLayout.Behavior<View> {

    private static final int NO_VALUE = -1;

    private OnDependentViewChangedListener mChangedListener;
    private int mEndViewId;
    private int mStartWidth, mStartHeight, mEndWidth = NO_VALUE, mEndHeight = NO_VALUE;etX
    private float mStartX = NO_VALUE, mStartY = NO_VALUE, mEndX = NO_VALUE, mEndY = NO_VALUE;
    private boolean mUseEndViewWidth, mUseEndViewHeight, mUseEndViewX, mUseEndViewY;

    public ViewPositionBehavior() {
        init(null, null);
    }

    public ViewPositionBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public static float calculateValue(@FloatRange float percentage, float startValue, float endValue) {
        return ((startValue - endValue) * (1 - percentage) + endValue);
    }

    private void init(Context context, AttributeSet attrs) {
        if (context != null && attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPositionBehavior);
            try {
                mEndViewId = array.getResourceId(R.styleable.ViewPositionBehavior_vpb_end_view, NO_VALUE);
                mEndWidth = array.getDimensionPixelSize(R.styleable.ViewPositionBehavior_vpb_end_width, mEndWidth);
                mEndHeight = array.getDimensionPixelSize(R.styleable.ViewPositionBehavior_vpb_end_height, mEndHeight);
                mEndX = array.getDimensionPixelSize(R.styleable.ViewPositionBehavior_vpb_end_x, NO_VALUE);
                mEndY = array.getDimensionPixelSize(R.styleable.ViewPositionBehavior_vpb_end_y, NO_VALUE);
                mUseEndViewWidth = !array.hasValue(R.styleable.ViewPositionBehavior_vpb_end_width);
                mUseEndViewHeight = !array.hasValue(R.styleable.ViewPositionBehavior_vpb_end_height);
                mUseEndViewX = !array.hasValue(R.styleable.ViewPositionBehavior_vpb_end_x);
                mUseEndViewY = !array.hasValue(R.styleable.ViewPositionBehavior_vpb_end_y);
            } finally {
                array.recycle();
            }
        }

    }

    public void setChangedListener(OnDependentViewChangedListener changedListener) {
        mChangedListener = changedListener;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        initValues(parent, child);

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        params.height = (int) calculateValue(percentage, mStartHeight, mEndHeight);
        params.width = (int) calculateValue(percentage, mStartWidth, mEndWidth);

        child.setLayoutParams(params);

        child.setX(calculateValue(percentage, mStartX, mEndX));
        child.setY(calculateValue(percentage, mStartY, mEndY));

//        Log.i("onDependentViewChanged: height " + params.height + " width " + params.width + " x " + child.getX() + " y " + child.getY());

        if (mChangedListener != null) {
            mChangedListener.onDependentViewChanged(percentage);
        }

        return true;
    }

    private void initValues(CoordinatorLayout parent, View child) {

        if (mEndViewId != NO_VALUE && (mEndWidth == NO_VALUE || mEndHeight == NO_VALUE)) {
            View anchorView = parent.findViewById(mEndViewId);
            if (anchorView != null) {
                mEndWidth = mUseEndViewWidth ? anchorView.getWidth() : mEndWidth;
                mEndHeight = mUseEndViewHeight ? anchorView.getHeight() : mEndHeight;
                mEndX = mUseEndViewX ? ViewUtils.getRelativeX(anchorView, parent) : mEndX;
                mEndY = mUseEndViewY ? ViewUtils.getRelativeY(anchorView, parent) : mEndY;
            } else {
                Log.e("onLayoutChild: anchor view is null", new NullPointerException());
            }
        }

//        Log.i("initValues: width " + mEndWidth + " height " + mEndHeight + " x " + mEndX + " y " + mEndY);

        if (mStartWidth == 0) {
            mStartWidth = child.getWidth();
        }

        if (mStartHeight == 0) {
            mStartHeight = child.getHeight();
        }

        if (mStartX == NO_VALUE) {
            mStartX = child.getX();
        }

        if (mStartY == NO_VALUE) {
            mStartY = child.getY();
        }

//        Log.i("initValues2: width " + mStartWidth + " height " + mStartHeight + " x " + mStartX + " y " + mStartY);
    }

    public interface OnDependentViewChangedListener {
        void onDependentViewChanged(float percentage);
    }
}