package com.artemkopan.widget.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Artem Kopan for MyMoodAndMe
 * 22.03.17
 */

public class FontCache {

    private static final String TAG = "FontCache";

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = fontCache.get(fontName);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }

            fontCache.put(fontName, typeface);
        }

        return typeface;
    }
}