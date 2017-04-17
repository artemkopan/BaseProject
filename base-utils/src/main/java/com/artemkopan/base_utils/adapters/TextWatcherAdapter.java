package com.artemkopan.base_utils.adapters;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created for MediMee.
 * Author Artem Kopan.
 * Date 09.07.2016 13:12
 */

public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
