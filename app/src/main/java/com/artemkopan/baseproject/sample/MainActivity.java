package com.artemkopan.baseproject.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private View mRevealView;
    private View mRevealBackgroundView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.appbar);
//
        setSupportActionBar(mToolbar);


        mRevealView = findViewById(R.id.reveal);
        mRevealBackgroundView = findViewById(R.id.revealBackground);

        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    animateAppAndStatusBar(R.color.colorPrimary, R.color.colorAccent);
                } else {
                    animateAppAndStatusBar(R.color.colorAccent, R.color.colorPrimary);
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateAppAndStatusBar(int fromColor, final int toColor) {
        Animator animator = ViewAnimationUtils.createCircularReveal(
                mRevealView,
                mToolbar.getWidth() / 2,
                mToolbar.getHeight() / 2, 0,
                mToolbar.getWidth() / 2);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRevealView.setBackgroundColor(getResources().getColor(toColor));
            }
        });

        mRevealBackgroundView.setBackgroundColor(getResources().getColor(fromColor));
        animator.setStartDelay(200);
        animator.setDuration(125);
        animator.start();
        mRevealView.setVisibility(View.VISIBLE);
    }
}
