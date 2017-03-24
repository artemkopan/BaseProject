package com.artemkopan.baseproject.internal.router;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.View;

import com.artemkopan.baseproject.internal.router.Router.AnimDefault;


@SuppressWarnings("ALL")
public interface IRouterBuilder {

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

        FragmentBuilder useCustomAnim(boolean isUse);

        FragmentBuilder setFragment(Fragment fragment);
    }

    interface FragmentBuilder {

        FragmentAnim animation();

        FragmentBuilder setIdRes(@IdRes int idRes);

        FragmentBuilder setFragment(Fragment fragment);

        FragmentBuilder setMethod(Router.Method method);

        FragmentBuilder addToBackStack(boolean addToBackStack);

        void start(Fragment fragment);

        void start(FragmentActivity activity);

        void startChildFragment(Fragment fragment, boolean useParentFragment);

        void start(@NonNull FragmentManager fragmentManager);

        FragmentTransaction getTransaction(@NonNull FragmentManager fragmentManager);
    }

}
