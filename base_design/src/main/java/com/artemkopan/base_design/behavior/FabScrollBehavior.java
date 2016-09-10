package com.artemkopan.base_design.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.artemkopan.baseproject.utils.ExtraUtils;

/**
 * Created for it-forum-2016.
 * Author Artem Kopan.
 * Date 08.29.2016 16:46
 */
public class FabScrollBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    private int mToolbarHeight;

    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mToolbarHeight = ExtraUtils.getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = dependency.getY() / (float) mToolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}