package com.artemkopan.utils.router;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.util.Pair;
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

        FragmentAnim setEnterOverlap(boolean enterOverlap);

        FragmentAnim setReturnOverlap(boolean returnOverlap);

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
