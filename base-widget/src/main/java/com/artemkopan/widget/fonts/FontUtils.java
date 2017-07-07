package com.artemkopan.widget.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.artemkopan.widget.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Artem Kopan for MyMoodAndMe
 * 22.03.17
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class FontUtils {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public static final int NORMAL = Typeface.NORMAL;

    public static final int MEDIUM = 13;

    public static final int ITALIC = Typeface.ITALIC;

    public static final int BOLD = Typeface.BOLD;
    public static final int BOLD_ITALIC = Typeface.BOLD_ITALIC;

    public static final int SEMI_BOLD = 14;
    public static final int SEMI_BOLD_ITALIC = 15;

    public static final int LIGHT = 10;
    public static final int LIGHT_BOLD = 11;
    public static final int LIGHT_ITALIC = 12;

    public static final int EXTRA_BOLD = 16;
    public static final int EXTRA_BOLD_ITALIC = 17;


    private static final String TAG = "FontUtils";
    private static final SparseArray<String> FONTS = new SparseArray<>();

    public static void addFont(@TextStyle int style, String fontPath) {
        FONTS.append(style, fontPath);
    }

    public static void applyCustomFont(TextView textView, Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.FontTextView);

        String fontPath = attributeArray.getString(R.styleable.FontTextView_font);

        // check if a special textStyle was used (e.g. extra bold)
        int textStyle = attributeArray.getInt(R.styleable.FontTextView_textStyle, 0);

        // if nothing extra was used, fall back to regular android:textStyle parameter
        if (textStyle == 0) {
            textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);
        }

        Typeface typeface;

        if (TextUtils.isEmpty(fontPath)) {
            typeface = selectTypeface(context, textStyle);
        } else {
            typeface = FontCache.getTypeface(fontPath, context);
        }

        if (typeface != null) {
            textView.setTypeface(typeface);
        }

        attributeArray.recycle();
    }

    @Nullable
    public static Typeface selectTypeface(Context context, int textStyle) {

        if (FONTS.size() == 0) {
            Log.w(TAG, "For use custom fonts, firstly you must call addFont()");
            return null;
        }

        String fontNewPath = FONTS.get(textStyle);

        if (TextUtils.isEmpty(fontNewPath)) {
            Log.w(TAG, "Current style not found. Pleas add it");
            fontNewPath = FONTS.get(Typeface.NORMAL);
            if (TextUtils.isEmpty(fontNewPath)) {
                fontNewPath = FONTS.get(FONTS.keyAt(0));
            }
        }

        return FontCache.getTypeface(fontNewPath, context);
    }

    @IntDef({
            NORMAL,
            MEDIUM,
            ITALIC,
            BOLD,
            BOLD_ITALIC,
            SEMI_BOLD,
            SEMI_BOLD_ITALIC,
            LIGHT,
            LIGHT_BOLD,
            LIGHT_ITALIC,
            EXTRA_BOLD,
            EXTRA_BOLD_ITALIC
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextStyle {

    }
}