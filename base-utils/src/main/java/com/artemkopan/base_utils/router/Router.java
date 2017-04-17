package com.artemkopan.base_utils.router;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;

import com.artemkopan.base_utils.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p> - {@link Method} set default {@link Method#REPLACE}</p>
 * <p> - {@link FragmentTransaction#addToBackStack(String)} set default <b>true</b> </p>
 * <p> - Default custom animation set default:
 * {@link R.anim#fragment_enter},
 * {@link R.anim#fragment_exit},
 * {@link R.anim#fragment_pop_enter},
 * {@link R.anim#fragment_pop_exit}</p>
 * <p> - Default fragment transaction: <b>Enter</b>, <b>Exit</b> - {@link Fade}</p>
 */
@SuppressWarnings("WeakerAccess")
public class Router {

    public static FragmentBuilder fragment() {
        return new FragmentBuilder();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Activity> ActivityBuilder activity(Class<T> navigateClass) {
        return new ActivityBuilder(navigateClass);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Activity> ActivityBuilder activity(Intent intent, Class<T> navigateClass) {
        return new ActivityBuilder(intent, navigateClass);
    }

    public enum Method {
        ADD, REPLACE, SWITCH
    }

    @IntDef({AnimDefault.ANIM_ALPHA, AnimDefault.ANIM_SLIDE, AnimDefault.ANIM_BOTTOM_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimDefault {

        int ANIM_ALPHA = 1;
        int ANIM_SLIDE = 2;
        int ANIM_BOTTOM_TOP = 3;
    }

    public static class RouterBuilderException extends RuntimeException {

        public RouterBuilderException(String detailMessage) {
            super(detailMessage);
        }
    }

}