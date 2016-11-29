package com.artemkopan.baseproject.view_pager.transforms;

import android.view.View;

/**
 * Created by Artem Kopan for jabrool
 * 29.11.16
 */

public class StackTransformer extends ABaseTransformer {

    @SuppressWarnings("ResourceType")
    @Override
    protected void onTransform(View view, float position) {
        view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
    }

}