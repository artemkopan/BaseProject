package com.artemkopan.baseproject.fragment;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


@SuppressWarnings("ALL")
public interface IFragment {

    interface Anim {
        Anim setEnterAnim(@AnimRes int idRes);

        Anim setExitAnim(@AnimRes int idRes);

        Anim setPopEnterAnim(@AnimRes int idRes);

        Anim setSharedEnterTransaction(Object object);

        Anim setSharedReturnTransaction(Object object);

        Anim setEnterTransaction(Object object);

        Anim setExitTransaction(Object object);

        Anim setSharedElements(Pair<View, String>... sharedElements);

        Anim setPopExitAnim(@AnimRes int idRes);

        Anim useCustomAnim(boolean isUse);

        Build setFragment(Fragment fragment);
    }

    interface Build {
        Build setIdRes(@IdRes int idRes);

        Build setFragment(Fragment fragment);

        Build setMethod(FragmentBuilder.Method method);

        Build addToBackStack(boolean addToBackStack);

        void start(Fragment fragment);

        void start(AppCompatActivity activity);

        void startChildFragment(Fragment fragment, boolean useParentFragment);
    }

}
