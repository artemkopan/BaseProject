package com.artemkopan.baseproject.utils;


import android.text.TextUtils;
import android.util.Patterns;

import java.io.UnsupportedEncodingException;

public class StringUtils {

    public static String digitsOnly(String str) {
        return str.replaceAll("\\D+", "");
    }

    public static boolean isHttpLink(String str) {
        return !TextUtils.isEmpty(str) && str.length() > 3 && str.startsWith("htt");
    }

    public static String convertToUtf8(String source) throws UnsupportedEncodingException {
        return new String(source.getBytes("ISO-8859-1"), "UTF-8");
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean isEmpty(CharSequence value) {
        return TextUtils.isEmpty(value) || TextUtils.getTrimmedLength(value) == 0;
    }

    /**
     * Get null string if parameter is null or empty or trimmed length == 0;
     * @return Return trimmed string if value not empty
     */
    public static String nullIfEmpty(CharSequence value) {
        return isEmpty(value) ? null : value.toString().trim();
    }

    public static CharSequence getTrimmed(CharSequence s) {
        if (s == null) return null;

        int len = s.length();

        int start = 0;
        while (start < len && s.charAt(start) <= ' ') {
            start++;
        }

        int end = len;
        while (end > start && s.charAt(end - 1) <= ' ') {
            end--;
        }

        return s.subSequence(start, end);
    }


}
