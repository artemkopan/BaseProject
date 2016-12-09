package com.artemkopan.baseproject.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import com.artemkopan.baseproject.helper.Log;

public class VibrationHelper {

    private static final int VIBRATION_DURATION = 300;

    public static void vibrate(Context context) {
        vibrate(context, VIBRATION_DURATION);
    }

    public static void vibrate(Context context, int duration) {
        if (hasVibrationPermission(context)) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(duration);
        }
    }

    private static boolean hasVibrationPermission(Context context) {
        // temporary workaround until https://github.com/robolectric/robolectric/pull/2047 is released
        try {
            return (context.getPackageManager().checkPermission(Manifest.permission.VIBRATE, context.getPackageName())
                    == PackageManager.PERMISSION_GRANTED);
        } catch (NullPointerException e) {
            Log.e("You don't have vibration permission");
            return false;
        }
    }
}
