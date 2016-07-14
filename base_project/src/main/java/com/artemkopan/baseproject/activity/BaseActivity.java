package com.artemkopan.baseproject.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.artemkopan.baseproject.fragment.BaseFragment;
import com.artemkopan.baseproject.rx.Lifecycle;

import butterknife.ButterKnife;
import butterknife.Unbinder;
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
}
