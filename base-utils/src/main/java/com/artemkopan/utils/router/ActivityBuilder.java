package com.artemkopan.utils.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

import com.artemkopan.utils.transitions.TransitionHelper;

import java.io.Serializable;
import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Artem Kopan for BaseProject
 * 17.04.17
 */
public class ActivityBuilder<T extends Activity> {

    private Intent intent;
    private Class<T> navigateClass;
    private int resultCode;

    public ActivityBuilder(Class<T> navigateClass) {
        intent = new Intent();
        this.navigateClass = navigateClass;
    }

    public ActivityBuilder(Intent intent, Class<T> navigateClass) {
        this.intent = intent;
        this.navigateClass = navigateClass;
    }

    public ActivityBuilder returnResult(int resultCode) {
        if (resultCode <= 0) throw new IllegalArgumentException("please, use result code > 0");
        this.resultCode = resultCode;
        return this;
    }

    /**
     * @see Intent#FLAG_GRANT_READ_URI_PERMISSION
     * @see Intent#FLAG_GRANT_WRITE_URI_PERMISSION
     * @see Intent#FLAG_GRANT_PERSISTABLE_URI_PERMISSION
     * @see Intent#FLAG_GRANT_PREFIX_URI_PERMISSION
     * @see Intent#FLAG_DEBUG_LOG_RESOLUTION
     * @see Intent#FLAG_FROM_BACKGROUND
     * @see Intent#FLAG_ACTIVITY_BROUGHT_TO_FRONT
     * @see Intent#FLAG_ACTIVITY_CLEAR_TASK
     * @see Intent#FLAG_ACTIVITY_CLEAR_TOP
     * @see Intent#FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
     * @see Intent#FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
     * @see Intent#FLAG_ACTIVITY_FORWARD_RESULT
     * @see Intent#FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
     * @see Intent#FLAG_ACTIVITY_MULTIPLE_TASK
     * @see Intent#FLAG_ACTIVITY_NEW_DOCUMENT
     * @see Intent#FLAG_ACTIVITY_NEW_TASK
     * @see Intent#FLAG_ACTIVITY_NO_ANIMATION
     * @see Intent#FLAG_ACTIVITY_NO_HISTORY
     * @see Intent#FLAG_ACTIVITY_NO_USER_ACTION
     * @see Intent#FLAG_ACTIVITY_PREVIOUS_IS_TOP
     * @see Intent#FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
     * @see Intent#FLAG_ACTIVITY_REORDER_TO_FRONT
     * @see Intent#FLAG_ACTIVITY_SINGLE_TOP
     * @see Intent#FLAG_ACTIVITY_TASK_ON_HOME
     * @see Intent#FLAG_RECEIVER_REGISTERED_ONLY
     */
    public ActivityBuilder setFlags(int flags) {
        intent.setFlags(flags);
        return this;
    }

    /**
     * @see Intent#FLAG_GRANT_READ_URI_PERMISSION
     * @see Intent#FLAG_GRANT_WRITE_URI_PERMISSION
     * @see Intent#FLAG_GRANT_PERSISTABLE_URI_PERMISSION
     * @see Intent#FLAG_GRANT_PREFIX_URI_PERMISSION
     * @see Intent#FLAG_DEBUG_LOG_RESOLUTION
     * @see Intent#FLAG_FROM_BACKGROUND
     * @see Intent#FLAG_ACTIVITY_BROUGHT_TO_FRONT
     * @see Intent#FLAG_ACTIVITY_CLEAR_TASK
     * @see Intent#FLAG_ACTIVITY_CLEAR_TOP
     * @see Intent#FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
     * @see Intent#FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
     * @see Intent#FLAG_ACTIVITY_FORWARD_RESULT
     * @see Intent#FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
     * @see Intent#FLAG_ACTIVITY_MULTIPLE_TASK
     * @see Intent#FLAG_ACTIVITY_NEW_DOCUMENT
     * @see Intent#FLAG_ACTIVITY_NEW_TASK
     * @see Intent#FLAG_ACTIVITY_NO_ANIMATION
     * @see Intent#FLAG_ACTIVITY_NO_HISTORY
     * @see Intent#FLAG_ACTIVITY_NO_USER_ACTION
     * @see Intent#FLAG_ACTIVITY_PREVIOUS_IS_TOP
     * @see Intent#FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
     * @see Intent#FLAG_ACTIVITY_REORDER_TO_FRONT
     * @see Intent#FLAG_ACTIVITY_SINGLE_TOP
     * @see Intent#FLAG_ACTIVITY_TASK_ON_HOME
     * @see Intent#FLAG_RECEIVER_REGISTERED_ONLY
     */
    public ActivityBuilder addFlags(int flags) {
        intent.addFlags(flags);
        return this;
    }

    public ActivityBuilder clearBackStack() {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return this;
    }

    public ActivityBuilder newTaskAndSingleTop() {
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return this;
    }

    public ActivityBuilder putExtra(String key, Object extra) {
        if (extra instanceof Boolean) {
            intent.putExtra(key, (Boolean) extra);
        } else if (extra instanceof Boolean[]) {
            intent.putExtra(key, (Boolean[]) extra);
        } else if (extra instanceof Byte) {
            intent.putExtra(key, (Byte) extra);
        } else if (extra instanceof Byte[]) {
            intent.putExtra(key, (Byte[]) extra);
        } else if (extra instanceof Character) {
            intent.putExtra(key, (Character) extra);
        } else if (extra instanceof Character[]) {
            intent.putExtra(key, (Character[]) extra);
        } else if (extra instanceof Short) {
            intent.putExtra(key, (Short) extra);
        } else if (extra instanceof Short[]) {
            intent.putExtra(key, (Short[]) extra);
        } else if (extra instanceof Integer) {
            intent.putExtra(key, (Integer) extra);
        } else if (extra instanceof Integer[]) {
            intent.putExtra(key, (Integer[]) extra);
        } else if (extra instanceof Long) {
            intent.putExtra(key, (Long) extra);
        } else if (extra instanceof Long[]) {
            intent.putExtra(key, (Long[]) extra);
        } else if (extra instanceof Float) {
            intent.putExtra(key, (Float) extra);
        } else if (extra instanceof Float[]) {
            intent.putExtra(key, (Float[]) extra);
        } else if (extra instanceof Double) {
            intent.putExtra(key, (Double) extra);
        } else if (extra instanceof Double[]) {
            intent.putExtra(key, (Double[]) extra);
        } else if (extra instanceof String) {
            intent.putExtra(key, (String) extra);
        } else if (extra instanceof String[]) {
            intent.putExtra(key, (String[]) extra);
        } else if (extra instanceof Bundle) {
            intent.putExtra(key, (Bundle) extra);
        } else if (extra instanceof Parcelable[]) {
            intent.putExtra(key, (Parcelable[]) extra);
        } else if (extra instanceof Parcelable) {
            intent.putExtra(key, (Parcelable) extra);
        } else if (extra instanceof Serializable) {
            intent.putExtra(key, (Serializable) extra);
        } else {
            throw new IllegalArgumentException("Wrong extra type [" + extra + "]");
        }
        return this;
    }

    public ActivityBuilder putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
        intent.putParcelableArrayListExtra(name, value);
        return this;
    }

    public ActivityBuilder putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
        intent.putIntegerArrayListExtra(name, value);
        return this;
    }

    public ActivityBuilder putStringArrayListExtra(String name, ArrayList<String> value) {
        intent.putStringArrayListExtra(name, value);
        return this;
    }

    public ActivityBuilder putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
        intent.putCharSequenceArrayListExtra(name, value);
        return this;
    }

    public Intent intent() {
        return intent;
    }

    public Intent intentWithClass(Context context) {
        intent.setClass(context, navigateClass);
        return intent;
    }

    public void start(Context context) {
        intent.setClass(context, navigateClass);
        context.startActivity(intent);
    }

    public void start(Activity activity) {
        intent.setClass(activity, navigateClass);
        if (resultCode > 0) activity.startActivityForResult(intent, resultCode);
        else activity.startActivity(intent);
    }

    public void start(Fragment fragment) {
        intent.setClass(fragment.getContext(), navigateClass);
        if (resultCode > 0) fragment.startActivityForResult(intent, resultCode);
        else fragment.startActivity(intent);
    }

    public void startWithTransition(Activity activity) {
        startWithTransition(activity, (Pair<? extends View, String>) null);
    }

    @SuppressWarnings("unchecked")
    public void startWithTransition(Activity activity, Pair<? extends View, String> shared) {
        startWithTransition(activity, new Pair[]{shared});
    }

    @SafeVarargs
    public final void startWithTransition(Activity activity, Pair<? extends View, String>... shared) {
        intent.setClass(activity, navigateClass);
        if (resultCode > 0) {
            startActivityForResult(activity, intent, resultCode, TransitionHelper.generateBundle(activity, shared));
        } else ActivityCompat.startActivity(activity, intent, TransitionHelper.generateBundle(activity, shared));

    }
}
