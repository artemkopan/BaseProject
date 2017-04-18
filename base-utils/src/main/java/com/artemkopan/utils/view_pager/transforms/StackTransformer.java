package com.artemkopan.utils.view_pager.transforms;

import android.view.View;

/**
 * Created by Artem Kopan for jabrool
 * 29.11.16
 */

public class StackTransformer extends ABaseTransformer {

    @SuppressWarnings("ResourceType")
    @Override
    protected void onTransform(View view, float position) {
        final float factor = 1f + Math.abs(position);
        view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
        view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (factor - 1f));
    }

}