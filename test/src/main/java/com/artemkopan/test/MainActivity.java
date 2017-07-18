package com.artemkopan.test;

import android.os.Bundle;
import android.view.View;

import com.artemkopan.mvp.activity.BaseActivity;
import com.artemkopan.test.mvp.RotateActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {
        RotateActivity.route().start(this);
    }

    @Override
    public int onInflateLayout() {
        return View.NO_ID;
    }
}
