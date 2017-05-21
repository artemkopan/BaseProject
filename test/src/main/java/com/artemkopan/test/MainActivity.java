package com.artemkopan.test;

import android.arch.lifecycle.LifecycleActivity;
import android.os.Bundle;
import android.view.View;

import com.artemkopan.test.mvp.RotateActivity;

public class MainActivity extends LifecycleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {
        RotateActivity.route().start(this);
    }
}
