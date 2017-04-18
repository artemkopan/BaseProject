package com.artemkopan.recycler.holder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created for jabrool
 * by Kopan Artem on 31.10.2016.
 */

public class SimpleHolder extends BaseHolder {

    public SimpleHolder(View itemView) {
        super(itemView);
    }

    public static SimpleHolder create(ViewGroup parent, @LayoutRes int res) {
        return new SimpleHolder(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    @Override
    public void bind(Context context, Object item, int position) {

    }

    @Override
    public void clear() {

    }
}
