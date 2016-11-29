package com.artemkopan.baseproject.utils.animations;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Build;
import android.util.Property;
import android.view.View;

import com.artemkopan.baseproject.R;


public final class AnimUtils {

    public static final int VERY_FAST_DURATION = 150;
    public static final int FAST_DURATION = 200;
    public static final int MIDDLE_DURATION = 400;
    public static final int SLOW_DURATION = 700;

    public static final Property<View, Integer> BACKGROUND_TINT_LIST =
            new Property<View, Integer>(Integer.class, "backgroundTint") {

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void set(View object, Integer value) {
                    object.setBackgroundTintList(ColorStateList.valueOf(value));
                }

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
    public static void setupRipple(View view, boolean setClickable, boolean boundary) {
        if (view == null) return;

        if (setClickable) {
            view.setClickable(true);
        }

        int[] attrs = new int[]{
                boundary ?
                R.attr.selectableItemBackgroundBorderless :
                R.attr.selectableItemBackground};

        TypedArray typedArray = view.getContext().obtainStyledAttributes(attrs);
        try {
            int backgroundResource = typedArray.getResourceId(0, 0);
            view.setBackgroundResource(backgroundResource);
        } finally {
            typedArray.recycle();
        }
    }
}
