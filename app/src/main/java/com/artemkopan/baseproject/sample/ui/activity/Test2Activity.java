package com.artemkopan.baseproject.sample.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.sample.R;

//TODO Check your activity class extends
public class Test2Activity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, Test2Activity.class);

        //noinspection unchecked
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();
        ActivityCompat.startActivity(activity, intent, bundle);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        bindButterKnife();
    }

    @Override
    public void showProgress(@Nullable Object tag) {

    }

    @Override
    public void hideProgress(@Nullable Object tag) {

    }

    @Override
    public void showError(@Nullable Object tag, String error) {

    }

}
