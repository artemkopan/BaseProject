package com.artemkopan.utils.view_pager.transforms;

import android.view.View;

/**
 * Created by Artem Kopan for jabrool
 * 09.12.16
 */

public class ZoomOutTransformer extends ABaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float scale = 1f + Math.abs(position);
        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
        if (position == -1) {
            view.setTranslationX(view.getWidth() * -1);
        }
    }
}
