package com.artemkopan.base_utils.animations;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.util.Property;
import android.view.View;

import java.util.ArrayList;

@SuppressWarnings("unused")
public final class AnimUtils {

    public static final int VERY_FAST_DURATION = 150;
    public static final int FAST_DURATION = 200;
    public static final int MIDDLE_DURATION = 400;
    public static final int SLOW_DURATION = 700;

    public static final Property<View, Integer> BACKGROUND_TINT_LIST =
            new Property<View, Integer>(Integer.class, "backgroundTint") {

                @TargetApi(VERSION_CODES.LOLLIPOP)
                @Override
                public void set(View object, Integer value) {
                    object.setBackgroundTintList(ColorStateList.valueOf(value));
                }

                @TargetApi(VERSION_CODES.LOLLIPOP)
                @Override
                public Integer get(View object) {
                    return object.getBackgroundTintList() != null ?
                           object.getBackgroundTintList().getDefaultColor() : Color.TRANSPARENT;
                }
            };

    public static ObjectAnimator alpha(int duration, float... values) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setPropertyName("alpha");
        objectAnimator.setFloatValues(values);
        objectAnimator.setDuration(duration);
        return objectAnimator;
    }

    /**
     * You can use with {@link android.graphics.drawable.GradientDrawable}
     */
    public static ObjectAnimator color(int duration, int... colors) {
        ObjectAnimator animator = new ObjectAnimator();
        animator.setIntValues(colors);
        animator.setDuration(duration);
        animator.setPropertyName("color");
        animator.setEvaluator(new ArgbEvaluator());
        return animator;
    }

    public static ObjectAnimator textColor(int duration, int... colors) {
        ObjectAnimator textColorAnimator = new ObjectAnimator();
        textColorAnimator.setPropertyName("textColor");
        textColorAnimator.setIntValues(colors);
        textColorAnimator.setDuration(duration);
        textColorAnimator.setEvaluator(new ArgbEvaluator());
        return textColorAnimator;
    }

    public static ObjectAnimator backgroundColor(int duration, int... colors) {
        ObjectAnimator textColorAnimator = new ObjectAnimator();
        textColorAnimator.setPropertyName("backgroundColor");
        textColorAnimator.setIntValues(colors);
        textColorAnimator.setDuration(duration);
        textColorAnimator.setEvaluator(new ArgbEvaluator());
        return textColorAnimator;
    }

    public static ObjectAnimator textColorAndBackgroundColor(int duration,
                                                             int textStartColor,
                                                             int textEndColor,
                                                             int backgroundStartColor,
                                                             int backgroundEndColor) {

        PropertyValuesHolder textColor = PropertyValuesHolder.ofInt("textColor", textStartColor,
                                                                    textEndColor);
        PropertyValuesHolder backgroundColor = PropertyValuesHolder.ofInt("backgroundColor",
                                                                          backgroundStartColor, backgroundEndColor);

        ArgbEvaluator evaluator = new ArgbEvaluator();

        textColor.setEvaluator(evaluator);
        backgroundColor.setEvaluator(evaluator);

        ObjectAnimator animator = new ObjectAnimator();
        animator.setValues(textColor, backgroundColor);
        animator.setDuration(duration);
        return animator;
    }

    /**
     * You can use in {@link android.widget.ImageView#setColorFilter(ColorFilter)} (tint image);
     */
    public static ObjectAnimator colorFilter(int duration, int... colors) {
        ObjectAnimator textColorAnimator = new ObjectAnimator();
        textColorAnimator.setPropertyName("colorFilter");
        textColorAnimator.setIntValues(colors);
        textColorAnimator.setDuration(duration);
        textColorAnimator.setEvaluator(new ArgbEvaluator());
        return textColorAnimator;
    }

    public static ObjectAnimator rotation(int duration, float... angles) {
        ObjectAnimator textColorAnimator = new ObjectAnimator();
        textColorAnimator.setPropertyName("rotation");
        textColorAnimator.setFloatValues(angles);
        textColorAnimator.setDuration(duration);
        return textColorAnimator;
    }

    public static ObjectAnimator shake(int duration) {
        ObjectAnimator shakeAnim = new ObjectAnimator();
        shakeAnim.setPropertyName("translationX");
        shakeAnim.setFloatValues(0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        shakeAnim.setDuration(duration);
        return shakeAnim;
    }

    /**
     * Setup background ripple animation for view.
     *
     * @param view         Current View
     * @param setClickable Ripple effect work with clickable view
     * @param boundary     If you want boundary (recommend for circle view)
     */
    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    public static void setupRipple(View view, boolean setClickable, boolean boundary) {
        if (view == null) return;

        if (setClickable) {
            view.setClickable(true);
        }

        int[] attrs = new int[]{boundary ? android.R.attr.selectableItemBackgroundBorderless
                                         : android.R.attr.selectableItemBackground};

        TypedArray typedArray = view.getContext().obtainStyledAttributes(attrs);
        try {
            int backgroundResource = typedArray.getResourceId(0, 0);
            view.setBackgroundResource(backgroundResource);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * https://halfthought.wordpress.com/2014/11/07/reveal-transition/
     * <p>
     * Interrupting Activity transitions can yield an OperationNotSupportedException when the
     * transition tries to pause the animator. Yikes! We can fix this by wrapping the Animator:
     */
    @TargetApi(VERSION_CODES.KITKAT)
    public static class NoPauseAnimator extends Animator {

        private final Animator animator;
        private final ArrayMap<AnimatorListener, AnimatorListener> listeners = new ArrayMap<>();

        public NoPauseAnimator(Animator animator) {
            this.animator = animator;
        }

        @Override
        public void addListener(AnimatorListener listener) {
            AnimatorListener wrapper = new AnimatorListenerWrapper(this, listener);
            if (!listeners.containsKey(listener)) {
                listeners.put(listener, wrapper);
                animator.addListener(wrapper);
            }
        }

        @Override
        public void cancel() {
            animator.cancel();
        }

        @Override
        public void end() {
            animator.end();
        }

        @Override
        public long getDuration() {
            return animator.getDuration();
        }

        @Override
        public TimeInterpolator getInterpolator() {
            return animator.getInterpolator();
        }

        @Override
        public void setInterpolator(TimeInterpolator timeInterpolator) {
            animator.setInterpolator(timeInterpolator);
        }

        @Override
        public ArrayList<AnimatorListener> getListeners() {
            return new ArrayList<>(listeners.keySet());
        }

        @Override
        public long getStartDelay() {
            return animator.getStartDelay();
        }

        @Override
        public void setStartDelay(long delayMS) {
            animator.setStartDelay(delayMS);
        }

        @Override
        public boolean isPaused() {
            return animator.isPaused();
        }

        @Override
        public boolean isRunning() {
            return animator.isRunning();
        }

        @Override
        public boolean isStarted() {
            return animator.isStarted();
        }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect animator.
        public void pause();
        public void resume();
        public void addPauseListener(AnimatorPauseListener listener);
        public void removePauseListener(AnimatorPauseListener listener);
        */

        @Override
        public void removeAllListeners() {
            listeners.clear();
            animator.removeAllListeners();
        }

        @Override
        public void removeListener(AnimatorListener listener) {
            AnimatorListener wrapper = listeners.get(listener);
            if (wrapper != null) {
                listeners.remove(listener);
                animator.removeListener(wrapper);
            }
        }

        @Override
        public Animator setDuration(long durationMS) {
            animator.setDuration(durationMS);
            return this;
        }

        @Override
        public void setTarget(Object target) {
            animator.setTarget(target);
        }

        @Override
        public void setupEndValues() {
            animator.setupEndValues();
        }

        @Override
        public void setupStartValues() {
            animator.setupStartValues();
        }

        @Override
        public void start() {
            animator.start();
        }
    }

    private static class AnimatorListenerWrapper implements Animator.AnimatorListener {

        private final Animator mAnimator;
        private final Animator.AnimatorListener mListener;

        AnimatorListenerWrapper(Animator animator, Animator.AnimatorListener listener) {
            mAnimator = animator;
            mListener = listener;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            mListener.onAnimationStart(mAnimator);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mListener.onAnimationEnd(mAnimator);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mListener.onAnimationCancel(mAnimator);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            mListener.onAnimationRepeat(mAnimator);
        }
    }

}
