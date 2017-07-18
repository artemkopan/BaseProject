package com.artemkopan.widget.places;

import android.location.Address;
import android.support.annotation.Nullable;

/**
 * Created by Artem Kopan for BaseProject
 * 18.07.2017
 */

public class PlacesAutoCompleteUtils {

    /**
     * Get full address from {@link Address}
     */
    public static String getCompleteAddressLine(@Nullable Address address) {
        StringBuilder strReturnedAddress = new StringBuilder("");
        if (address != null) {
            int count = address.getMaxAddressLineIndex();
            if (count == 0) {
                strReturnedAddress.append(address.getAddressLine(0));
                return strReturnedAddress.toString();
            }
            for (int i = 0; i < count; i++) {
                strReturnedAddress.append(address.getAddressLine(i));
                if (i < count - 1) strReturnedAddress.append(", ");
            }
        }
        return strReturnedAddress.toString();
    }

    /**
     * Trim char sequence
     */
    public static CharSequence trim(CharSequence s) {
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
