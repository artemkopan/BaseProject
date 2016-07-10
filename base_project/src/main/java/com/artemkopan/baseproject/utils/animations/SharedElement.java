package com.artemkopan.baseproject.utils.animations;

import android.view.View;

public class SharedElement {
    private View mView;
    private String mName;

    public SharedElement(View view, String name) {
        mView = view;
        mName = name;
    }

    public View getView() {
        return mView;
    }

    public String getName() {
        return mName;
    }
}
