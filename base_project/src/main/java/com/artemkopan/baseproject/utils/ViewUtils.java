package com.artemkopan.baseproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.view.View;


public class ViewUtils {

    /**
     * Get activity from view
     *
     * @param view current view
     * @return Current activity
     */
    @Nullable
    public static Activity getActivity(View view) {
        if (view == null) {
            return null;
        }
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
