package com.artemkopan.utils.transitions;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build.VERSION_CODES;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.artemkopan.utils.R;
import com.artemkopan.utils.AnimUtils;

/**
 * Example of use:
 * <p>
 * <pre>
 * {@code
 * <transitionSet
 *      android:interpolator="@android:interpolator/linear_out_slow_in">
 *      <targets>
 *          <target android:targetId="@id/results_scrim" />
 *      </targets>
 *      <fade android:duration="500" />
 *      <transition
 *          class="com.artemkopan.baseproject.utils.transitions.CircularRevealTransition"
 *          app:centerOn="@id/fab"
 *          android:duration="280" />
 *    </transitionSet>
 * }
 * </pre>
 */

@SuppressWarnings("unused")
@RequiresApi(VERSION_CODES.LOLLIPOP)
public class CircularRevealTransition extends Visibility {

    private Point center;
    private float startRadius;
    private float endRadius;
    private @IdRes int centerOnId = View.NO_ID;
    private View centerOn;

    public CircularRevealTransition() {
        super();
    }

    public CircularRevealTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularRevealTransition);
        startRadius = a.getDimension(R.styleable.CircularRevealTransition_startRadius, 0f);
        endRadius = a.getDimension(R.styleable.CircularRevealTransition_endRadius, 0f);
        centerOnId = a.getResourceId(R.styleable.CircularRevealTransition_centerOn, View.NO_ID);
        a.recycle();
    }

    /**
     * The center point of the reveal or conceal, relative to the target {@code view}.
     */
    public void setCenter(@NonNull Point center) {
        this.center = center;
    }

    /**
     * Center the reveal or conceal on this view.
     */
    public void centerOn(@NonNull View source) {
        centerOn = source;
    }

    /**
     * Sets the radius that <strong>reveals</strong> start from.
     */
    public void setStartRadius(float startRadius) {
        this.startRadius = startRadius;
    }

    /**
     * Sets the radius that <strong>conceals</strong> end at.
     */
    public void setEndRadius(float endRadius) {
        this.endRadius = endRadius;
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view,
                             TransitionValues startValues,
                             TransitionValues endValues) {
        if (view == null || view.getHeight() == 0 || view.getWidth() == 0) return null;
        ensureCenterPoint(sceneRoot, view);
        return new AnimUtils.NoPauseAnimator(ViewAnimationUtils.createCircularReveal(
                view,
                center.x,
                center.y,
                startRadius,
                getFullyRevealedRadius(view)));
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view,
                                TransitionValues startValues,
                                TransitionValues endValues) {
        if (view == null || view.getHeight() == 0 || view.getWidth() == 0) return null;
        ensureCenterPoint(sceneRoot, view);
        return new AnimUtils.NoPauseAnimator(ViewAnimationUtils.createCircularReveal(
                view,
                center.x,
                center.y,
                getFullyRevealedRadius(view),
                endRadius));
    }

    private void ensureCenterPoint(ViewGroup sceneRoot, View view) {
        if (center != null) return;
        if (centerOn != null || centerOnId != View.NO_ID) {
            View source;
            if (centerOn != null) {
                source = centerOn;
            } else {
                source = sceneRoot.findViewById(centerOnId);
            }
            if (source != null) {
                // use window location to allow views in diff hierarchies
                int[] loc = new int[2];
                source.getLocationInWindow(loc);
                int srcX = loc[0] + (source.getWidth() / 2);
                int srcY = loc[1] + (source.getHeight() / 2);
                view.getLocationInWindow(loc);
                center = new Point(srcX - loc[0], srcY - loc[1]);
            }
        }
        // else use the pivot point
        if (center == null) {
            center = new Point(Math.round(view.getPivotX()), Math.round(view.getPivotY()));
        }
    }

    private float getFullyRevealedRadius(@NonNull View view) {
        return (float) Math.hypot(
                Math.max(center.x, view.getWidth() - center.x),
                Math.max(center.y, view.getHeight() - center.y));
    }
}