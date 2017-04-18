package com.artemkopan.utils.router;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.View;

import com.artemkopan.utils.router.Router.AnimDefault;

@SuppressWarnings("ALL")
public interface IFragmentBuilder {

    interface FragmentAnim {
        FragmentAnim setEnterAnim(@AnimRes int idRes);

        FragmentAnim setExitAnim(@AnimRes int idRes);

        FragmentAnim setPopEnterAnim(@AnimRes int idRes);

        FragmentAnim setPopExitAnim(@AnimRes int idRes);

        FragmentAnim setDefaultAnim(@AnimDefault int defaultAnim);

        FragmentAnim setSharedEnterTransition(Object object);

        FragmentAnim setSharedReturnTransition(Object object);

        FragmentAnim setEnterTransition(Object object);

        FragmentAnim setExitTransition(Object object);

        FragmentAnim setReenterTransition(Object object);

        FragmentAnim setReturnTransition(Object object);

        FragmentAnim setSharedElements(Pair<View, String>... sharedElements);

        Builder useCustomAnim(boolean isUse);

        Builder main();
    }

    interface Builder {

        FragmentAnim anim();

        Builder setIdRes(@IdRes int idRes);

        Builder setFragment(Fragment fragment);

        Builder setMethod(Router.Method method);

        Builder addToBackStack(boolean addToBackStack);

        void start(Fragment fragment);

        void start(FragmentActivity activity);

        void startChildFragment(Fragment fragment, boolean useParentFragment);

        void start(@NonNull FragmentManager fragmentManager);

        FragmentTransaction getTransaction(@NonNull FragmentManager fragmentManager);
    }

}
