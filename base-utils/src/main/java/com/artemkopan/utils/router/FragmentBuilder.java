package com.artemkopan.utils.router;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.util.Pair;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;

import com.artemkopan.utils.R;
import com.artemkopan.utils.router.IFragmentBuilder.Builder;
import com.artemkopan.utils.router.IFragmentBuilder.FragmentAnim;
import com.artemkopan.utils.router.Router.AnimDefault;
import com.artemkopan.utils.router.Router.Method;
import com.artemkopan.utils.router.Router.RouterBuilderException;

/**
 * Created by Artem Kopan for BaseProject
 * 17.04.17
 */
public class FragmentBuilder implements FragmentAnim, Builder {

    private static final String TAG = "FragmentBuilder";

    @IdRes
    private static int idResDefault;

    @IdRes
    private int idRes;
    @AnimRes
    private int enter, exit, popEnter, popExit;
    private Method method = Method.REPLACE;
    private Fragment fragment;
    private Pair<View, String>[] sharedElements;
    private Object sharedEnterTransition, sharedReturnTransition;
    private Object enterTransition, exitTransition, reenterTransition, returnTransition;
    private boolean enterOverlap, returnOverlap;
    private boolean addToBackStack = true, useCustomAnim = true;


    /**
     * If needed you can set default id res for fragments. Usually call in {@link android.app.Application}
     *
     * @param idResDefault fragment container id;
     */
    public static void setIdResDefault(@IdRes int idResDefault) {
        FragmentBuilder.idResDefault = idResDefault;
    }

    @Override
    public FragmentAnim setEnterAnim(@AnimRes int idRes) {
        enter = idRes;
        return this;
    }

    @Override
    public FragmentAnim setExitAnim(@AnimRes int idRes) {
        exit = idRes;
        return this;
    }

    @Override
    public FragmentAnim setPopEnterAnim(@AnimRes int idRes) {
        popEnter = idRes;
        return this;
    }

    @Override
    @SafeVarargs
    public final FragmentAnim setSharedElements(Pair<View, String>... sharedElements) {
        this.sharedElements = sharedElements;
        return this;
    }

    @Override
    public FragmentAnim setPopExitAnim(@AnimRes int idRes) {
        popExit = idRes;
        return this;
    }

    @Override
    public FragmentAnim setDefaultAnim(@AnimDefault int defaultAnim) {
        switch (defaultAnim) {
            case AnimDefault.ANIM_ALPHA:
                popEnter = enter = R.anim.alpha_enter;
                popExit = exit = R.anim.alpha_exit;
                break;
            case AnimDefault.ANIM_SLIDE:
                popEnter = R.anim.fragment_pop_enter;
                enter = R.anim.fragment_enter;
                popExit = R.anim.fragment_pop_exit;
                exit = R.anim.fragment_exit;
                break;
            case AnimDefault.ANIM_BOTTOM_TOP:
                popEnter = R.anim.fragment_pop_bottom_top;
                enter = R.anim.fragment_bottom_top;
                popExit = R.anim.fragment_pop_top_bottom;
                exit = R.anim.fragment_top_bottom;
                break;
        }
        return this;
    }

    @Override
    public FragmentAnim setSharedEnterTransition(Object object) {
        sharedEnterTransition = object;
        return this;
    }

    @Override
    public FragmentAnim setSharedReturnTransition(Object object) {
        sharedReturnTransition = object;
        return this;
    }

    @Override
    public FragmentAnim setEnterTransition(Object object) {
        enterTransition = object;
        return this;
    }

    @Override
    public FragmentAnim setExitTransition(Object object) {
        exitTransition = object;
        return this;
    }

    @Override
    public FragmentAnim setReenterTransition(Object object) {
        reenterTransition = object;
        return this;
    }

    @Override
    public FragmentAnim setReturnTransition(Object object) {
        returnTransition = object;
        return this;
    }

    @Override
    public FragmentAnim setEnterOverlap(boolean enterOverlap) {
        this.enterOverlap = enterOverlap;
        return this;
    }

    @Override
    public FragmentAnim setReturnOverlap(boolean returnOverlap) {
        this.returnOverlap = returnOverlap;
        return this;
    }

    @Override
    public Builder useCustomAnim(boolean isUse) {
        useCustomAnim = isUse;
        return this;
    }

    @Override
    public FragmentAnim anim() {
        return this;
    }

    @Override
    public Builder setIdRes(@IdRes int idRes) {
        this.idRes = idRes;
        return this;
    }

    @Override
    public Builder setFragment(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }

    @Override
    public Builder main() {
        return this;
    }

    /**
     * Default <b>{@link Method#REPLACE}</b>
     */
    @Override
    public Builder setMethod(Method method) {
        this.method = method;
        return this;
    }

    /**
     * Default <b>true</b>;
     */
    @Override
    public Builder addToBackStack(boolean addToBackStack) {
        this.addToBackStack = addToBackStack;
        return this;
    }

    @Override
    public void start(Fragment fragment) {
        start(fragment.getFragmentManager());
    }

    @Override
    public void start(FragmentActivity activity) {
        start(activity.getSupportFragmentManager());
    }

    @Override
    public void startChildFragment(Fragment fragment, boolean useParentFragment) {
        if (useParentFragment) {
            start(fragment.getParentFragment().getChildFragmentManager());
        } else {
            start(fragment.getChildFragmentManager());
        }
    }

    @Override
    public void start(@NonNull FragmentManager fragmentManager) {
        getTransaction(fragmentManager).commitAllowingStateLoss();
    }

    @Override
    public FragmentTransaction getTransaction(@NonNull FragmentManager fragmentManager) {
        if (fragment == null) {
            throw new RouterBuilderException("You must set fragment");
        }
        if (method != Method.ADD && idRes <= 0 && idResDefault <= 0) {
            throw new RouterBuilderException("Your fragment container id myst be >= 0.\n\n" +
                                                     "You can call in Application onCreate() Router.setIdResDefault()\n");
        }
        int idRes;

        if (method == Method.ADD) {
            idRes = this.idRes > 0 ? this.idRes : android.R.id.content;
        } else {
            idRes = this.idRes > 0 ? this.idRes : idResDefault;
        }

        Log.i(TAG, "start fragment | method: " + method.name() + " id: " + idRes);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        String tag = fragment.getClass().getName();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (sharedElements != null && sharedElements.length > 0) {
                Transition transition = TransitionInflater.from(fragment.getContext())
                        .inflateTransition(android.R.transition.move);

                fragment.setSharedElementEnterTransition(sharedEnterTransition == null ? transition
                                                                                       : sharedEnterTransition);

                fragment.setSharedElementReturnTransition(sharedReturnTransition == null ? transition
                                                                                         : sharedReturnTransition);

                if (enterTransition == null) enterTransition = new Fade();
                if (exitTransition == null) exitTransition = new Fade();

                for (Pair<View, String> sharedElement : sharedElements) {
                    fragmentTransaction.addSharedElement(sharedElement.first, sharedElement.second);
                }
            }
        }

        if (enterTransition != null) fragment.setEnterTransition(enterTransition);
        if (exitTransition != null) fragment.setExitTransition(exitTransition);
        if (reenterTransition != null) fragment.setReenterTransition(reenterTransition);
        if (returnTransition != null) fragment.setReturnTransition(returnTransition);

        fragment.setAllowEnterTransitionOverlap(enterOverlap);
        fragment.setAllowReturnTransitionOverlap(returnOverlap);

        if (useCustomAnim) {
            fragmentTransaction.setCustomAnimations(
                    enter == 0 ? R.anim.fragment_enter : enter,
                    exit == 0 ? R.anim.fragment_exit : exit,
                    popEnter == 0 ? R.anim.fragment_pop_enter : popEnter,
                    popExit == 0 ? R.anim.fragment_pop_exit : popExit);
        }

        switch (method) {
            case ADD:
                if (idRes > 0) {
                    fragmentTransaction.add(idRes, fragment, tag);
                } else {
                    fragmentTransaction.add(fragment, tag);
                }
                break;
            case REPLACE:
                fragmentTransaction.replace(idRes, fragment, tag);
                break;
            case SWITCH:
                fragmentManager.popBackStack();
                fragmentTransaction.replace(idRes, fragment, tag);
                break;
        }

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }

        fragmentTransaction.setAllowOptimization(true);

        return fragmentTransaction;
    }
}
