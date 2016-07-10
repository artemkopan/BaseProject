package com.artemkopan.baseproject.utils.animations;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;

public abstract class SimpleAnimatorListener implements ViewPropertyAnimatorListener {
    /**
     * <p>Notifies the start of the animation.</p>
     *
     * @param view The view associated with the ViewPropertyAnimator
     */
    @Override
    public void onAnimationStart(View view) {

    }

    /**
     * <p>Notifies the end of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param view The view associated with the ViewPropertyAnimator
     */
    @Override
    public void onAnimationEnd(View view) {

    }

    /**
     * <p>Notifies the cancellation of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param view The view associated with the ViewPropertyAnimator
     */
    @Override
    public void onAnimationCancel(View view) {

    }
}
