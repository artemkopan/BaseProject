package com.artemkopan.baseproject.recycler.holder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.artemkopan.baseproject.utils.ViewUtils;

/**
 * Created for jabrool
 * by Kopan Artem on 31.10.2016.
 */

public class SimpleHolder extends BaseHolder {

    public SimpleHolder(View itemView) {
        super(itemView);
    }

    public static SimpleHolder create(ViewGroup parent, @LayoutRes int res) {
        return new SimpleHolder(ViewUtils.inflateView(parent, res));
    }

    @Override
    public void bind(Context context, Object item, int position) {

    }

    @Override
    public void clear() {

    }
}
