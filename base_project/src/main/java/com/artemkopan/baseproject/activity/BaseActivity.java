package com.artemkopan.baseproject.activity;

import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Window;
import android.view.WindowManager;

import com.artemkopan.baseproject.fragment.BaseFragment;
import com.artemkopan.baseproject.rx.Lifecycle;
import com.artemkopan.baseproject.utils.ExtraUtils;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;


public abstract class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public PublishSubject<Lifecycle> mPublishSubject = PublishSubject.create();

    @SuppressWarnings("SpellCheckingInspection")
    protected Unbinder mUnbinder;

    public void bindButterKnife() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        mPublishSubject.onNext(Lifecycle.ON_STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mPublishSubject.onNext(Lifecycle.ON_DESTROY);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount() - 1;
        if (backStackCount >= 0) {
            FragmentManager.BackStackEntry backEntry =
                    fragmentManager.getBackStackEntryAt(backStackCount);
            String str = backEntry.getName();

            Fragment fragment = fragmentManager.findFragmentByTag(str);

            if (fragment != null && fragment instanceof BaseFragment) {
                if (!((BaseFragment) fragment).onBackPressed()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    public void setStatusBarColor(@ColorInt int color) {
        if (ExtraUtils.postLollipop()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public void setStatusBarColor(@ColorInt final int color, long delay, TimeUnit timeUnit) {
        if (ExtraUtils.postLollipop()) {
            Observable.timer(delay, timeUnit, AndroidSchedulers.mainThread())
                    .takeUntil(mPublishSubject)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            setStatusBarColor(color);
                        }
                    });
        }

    }
}