package com.artemkopan.baseproject.fragment;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.animations.DetailsTransition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.artemkopan.baseproject.fragment.IRouterBuilder.Anim;
import static com.artemkopan.baseproject.fragment.IRouterBuilder.Build;

/**
 * <p> - {@link Method} set default {@link Method#REPLACE}</p>
 * <p> - {@link FragmentTransaction#addToBackStack(String)} set default <b>true</b> </p>
 * <p> - Default custom animation set default:
 * {@link R.anim#fragment_enter},
 * {@link R.anim#fragment_exit},
 * {@link R.anim#fragment_pop_enter},
 * {@link R.anim#fragment_pop_exit}</p>
 * <p> - Default shared anim:  <b>Enter</b>, <b>Exit</b> - {@link DetailsTransition}</p>
 * <p> - Default fragment transaction: <b>Enter</b>, <b>Exit</b> - {@link Fade}</p>
 */
@SuppressWarnings("WeakerAccess")
public class Router implements Anim, Build {

    public static final int ANIM_ALPHA = 1, ANIM_SLIDE = 2;
    @IdRes
    private static int sIdResDefault;
    @IdRes
    private int mIdRes;
    @AnimRes
    private int mEnter, mExit, mPopEnter, mPopExit;
    private Method mMethod = Method.REPLACE;
    private Fragment mFragment;
    private Pair<View, String>[] mSharedElements;
    private Object mSharedEnterTransition, mSharedReturnTransition;
    private Object mEnterTransition, mExitTransition, mReenterTransition, mReturnTransition;
    private boolean mAddToBackStack = true, mUseCustomAnim = true;


    public static Router builder() {
        return new Router();
    }

    /**
     * If needed you can set default id res for fragments. Usually call in {@link android.app.Application}
     *
     * @param idResDefault fragment container id;
     */
    public static void setIdResDefault(@IdRes int idResDefault) {
        sIdResDefault = idResDefault;
    }

    @Override
    public Anim setEnterAnim(@AnimRes int idRes) {
        mEnter = idRes;
        return this;
    }

    @Override
    public Anim setExitAnim(@AnimRes int idRes) {
        mExit = idRes;
        return this;
    }

    @Override
    public Anim setPopEnterAnim(@AnimRes int idRes) {
        mPopEnter = idRes;
        return this;
    }


    @Override
    @SafeVarargs
    public final Anim setSharedElements(Pair<View, String>... sharedElements) {
        mSharedElements = sharedElements;
        return this;
    }

    @Override
    public Anim setPopExitAnim(@AnimRes int idRes) {
        mPopExit = idRes;
        return this;
    }

    @Override
    public Anim setDefaultAnim(@ANIM_DEFAULT int defaultAnim) {
        switch (defaultAnim) {
            case ANIM_ALPHA:
                mPopEnter = mEnter = R.anim.fragment_alpha_enter;
                mPopExit = mExit = R.anim.fragment_alpha_exit;
                break;
            case ANIM_SLIDE:
                mPopEnter = R.anim.fragment_pop_enter;
                mEnter = R.anim.fragment_enter;
                mPopExit = R.anim.fragment_pop_exit;
                mExit = R.anim.fragment_exit;
                break;
        }
        return this;
    }


    @Override
    public Anim setSharedEnterTransition(Object object) {
        mSharedEnterTransition = object;
        return this;
    }

    @Override
    public Anim setSharedReturnTransition(Object object) {
        mSharedReturnTransition = object;
        return this;
    }

    @Override
    public Anim setEnterTransition(Object object) {
        mEnterTransition = object;
        return this;
    }

    @Override
    public Anim setExitTransition(Object object) {
        mExitTransition = object;
        return this;
    }

    @Override
    public Anim setReenterTransition(Object object) {
        mReenterTransition = object;
        return this;
    }

    @Override
    public Anim setReturnTransition(Object object) {
        mReturnTransition = object;
        return this;
    }

    @Override
    public Anim useCustomAnim(boolean isUse) {
        mUseCustomAnim = isUse;
        return this;
    }

    @Override
    public Build setIdRes(@IdRes int idRes) {
        mIdRes = idRes;
        return this;
    }

    @Override
    public Build setFragment(Fragment fragment) {
        mFragment = fragment;
        return this;
    }

    /**
     * Default <b>{@link Method#REPLACE}</b>
     */
    @Override
    public Build setMethod(Method method) {
        mMethod = method;
        return this;
    }

    /**
     * Default <b>true</b>;
     */
    @Override
    public Build addToBackStack(boolean addToBackStack) {
        mAddToBackStack = addToBackStack;
        return this;
    }

    @Override
    public void start(Fragment fragment) {
        startFragment(fragment.getFragmentManager());
    }

    @Override
    public void start(AppCompatActivity activity) {
        startFragment(activity.getSupportFragmentManager());
    }

    @Override
    public void startChildFragment(Fragment fragment, boolean useParentFragment) {
        if (useParentFragment) {
            startFragment(fragment.getParentFragment().getChildFragmentManager());
        } else {
            startFragment(fragment.getChildFragmentManager());
        }
    }

    private void startFragment(FragmentManager fragmentManager) {
        if (mFragment == null) {
            throw new RouterBuilderException("You must set fragment");
        }
        if (mIdRes <= 0 && sIdResDefault <= 0) {
            throw new RouterBuilderException("Your fragment container id myst be >= 0.\n\n" +
                    "You can call in Application onCreate() Router.setIdResDefault()\n");
        }

        int idRes = mIdRes > 0 ? mIdRes : sIdResDefault;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        String tag = mFragment.getClass().getSimpleName();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (mSharedElements != null && mSharedElements.length > 0) {
                mFragment.setSharedElementEnterTransition(
                        mSharedEnterTransition == null ? new DetailsTransition() : mSharedEnterTransition);
                mFragment.setSharedElementReturnTransition(
                        mSharedReturnTransition == null ? new DetailsTransition() : mSharedReturnTransition);

                if (mEnterTransition == null) mEnterTransition = new Fade();
                if (mExitTransition == null) mExitTransition = new Fade();

                for (Pair<View, String> sharedElement : mSharedElements) {
                    fragmentTransaction.addSharedElement(sharedElement.first, sharedElement.second);
                }
            }
            if (mEnterTransition != null) mFragment.setEnterTransition(mEnterTransition);
            if (mExitTransition != null) mFragment.setExitTransition(mExitTransition);
            if (mReenterTransition != null) mFragment.setReenterTransition(mReenterTransition);
            if (mReturnTransition != null) mFragment.setReturnTransition(mReturnTransition);
        }

        if (mUseCustomAnim) {
            fragmentTransaction.setCustomAnimations(
                    mEnter == 0 ? R.anim.fragment_enter : mEnter,
                    mExit == 0 ? R.anim.fragment_exit : mExit,
                    mPopEnter == 0 ? R.anim.fragment_pop_enter : mPopEnter,
                    mPopExit == 0 ? R.anim.fragment_pop_exit : mPopExit);
        }

        switch (mMethod) {
            case ADD:
                fragmentTransaction.add(idRes, mFragment, tag);
                break;
            case REPLACE:
                fragmentTransaction.replace(idRes, mFragment, tag);
                break;
            case SWITCH:
                fragmentManager.popBackStack();
                fragmentTransaction.replace(idRes, mFragment, tag);
                break;
        }

        if (mAddToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public enum Method {
        ADD, REPLACE, SWITCH
    }

    @IntDef({ANIM_ALPHA, ANIM_SLIDE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ANIM_DEFAULT {
    }

    public class RouterBuilderException extends RuntimeException {
        public RouterBuilderException(String detailMessage) {
            super(detailMessage);
        }
    }
}