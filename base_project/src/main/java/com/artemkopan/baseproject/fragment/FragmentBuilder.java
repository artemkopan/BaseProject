package com.artemkopan.baseproject.fragment;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.animations.DetailsTransition;
import com.artemkopan.baseproject.utils.animations.SharedElement;

import static com.artemkopan.baseproject.fragment.IFragment.Anim;
import static com.artemkopan.baseproject.fragment.IFragment.Build;

/**
 * <li>{@link Method} set default {@link Method#REPLACE}</li>
 * <li>{@link FragmentTransaction#addToBackStack(String)} set default <b>true</b></li>
 * <li>Default custom animation set default:
 * {@link R.anim#fragment_enter},
 * {@link R.anim#fragment_exit},
 * {@link R.anim#fragment_pop_enter},
 * {@link R.anim#fragment_pop_exit}</li>
 * <li>Default shared anim:  <b>Enter</b>, <b>Exit</b> - {@link DetailsTransition}</li>
 * <li>Default fragment transaction: <b>Enter</b>, <b>Exit</b> - {@link Fade}</li>
 */
public class FragmentBuilder implements Anim, Build {

    @IdRes
    private static int sIdResDefault;
    @IdRes
    private int mIdRes;
    @AnimRes
    private int mEnter, mExit, mPopEnter, mPopExit;
    private Method mMethod = Method.REPLACE;
    private Fragment mFragment;
    private SharedElement[] mSharedElements;
    private Object mSharedEnterTransaction, mSharedReturnTransaction, mEnterTransaction, mExitTransaction;
    private boolean mAddToBackStack = true, mUseCustomAnim = true;


    public static FragmentBuilder builder() {
        return new FragmentBuilder();
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
    public Anim setSharedEnterTransaction(Object object) {
        mSharedEnterTransaction = object;
        return this;
    }

    @Override
    public Anim setSharedReturnTransaction(Object object) {
        mSharedReturnTransaction = object;
        return this;
    }

    @Override
    public Anim setEnterTransaction(Object object) {
        mEnterTransaction = object;
        return this;
    }

    @Override
    public Anim setExitTransaction(Object object) {
        mExitTransaction = object;
        return this;
    }

    @Override
    public Anim setSharedElements(SharedElement... sharedElements) {
        mSharedElements = sharedElements;
        return this;
    }

    @Override
    public Anim setPopExitAnim(@AnimRes int idRes) {
        mPopExit = idRes;
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
            throw new FragmentBuilderException("You must set fragment");
        }
        if (mIdRes <= 0 && sIdResDefault <= 0) {
            throw new FragmentBuilderException("Your fragment container id myst be >= 0.\n\n" +
                    "You can call in Application onCreate() FragmentBuilder.setIdResDefault()\n");
        }

        int idRes = mIdRes > 0 ? mIdRes : sIdResDefault;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        String tag = mFragment.getClass().getSimpleName();

        if (mSharedElements != null && mSharedElements.length > 0 &&
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            mFragment.setSharedElementEnterTransition(
                    mSharedEnterTransaction == null ? new DetailsTransition() : mSharedEnterTransaction);
            mFragment.setSharedElementReturnTransition(
                    mSharedReturnTransaction == null ? new DetailsTransition() : mSharedReturnTransaction);

            mFragment.setEnterTransition(mEnterTransaction == null ? new Fade() : mEnterTransaction);
            mFragment.setExitTransition(mExitTransaction == null ? new Fade() : mExitTransaction);

            for (SharedElement sharedElement : mSharedElements) {
                fragmentTransaction.addSharedElement(sharedElement.getView(), sharedElement.getName());
            }
        } else if (mUseCustomAnim) {
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

        fragmentTransaction.commit();
    }

    public enum Method {
        ADD, REPLACE, SWITCH
    }

    public class FragmentBuilderException extends RuntimeException {
        public FragmentBuilderException(String detailMessage) {
            super(detailMessage);
        }
    }
}