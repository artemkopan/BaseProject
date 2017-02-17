package com.artemkopan.baseproject.internal.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.transition.Fade;
import android.view.View;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.Log;
import com.artemkopan.baseproject.utils.animations.DetailsTransition;
import com.artemkopan.baseproject.utils.transitions.TransitionHelper;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.artemkopan.baseproject.internal.router.IRouterBuilder.FragmentAnim;

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
public class Router {

    public static FragmentBuilder fragment() {
        return new FragmentBuilder();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Activity> ActivityBuilder activity(Class<T> navigateClass) {
        return new ActivityBuilder(navigateClass);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Activity> ActivityBuilder activity(Intent intent, Class<T> navigateClass) {
        return new ActivityBuilder(intent, navigateClass);
    }

    public enum Method {
        ADD, REPLACE, SWITCH
    }

    @IntDef({AnimDefault.ANIM_ALPHA, AnimDefault.ANIM_SLIDE, AnimDefault.ANIM_BOTTOM_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimDefault {

        int ANIM_ALPHA = 1;
        int ANIM_SLIDE = 2;
        int ANIM_BOTTOM_TOP = 3;
    }

    public static class RouterBuilderException extends RuntimeException {

        public RouterBuilderException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class ActivityBuilder<T extends Activity> {

        private Intent intent;
        private Class<T> navigateClass;
        private int resultCode;

        public ActivityBuilder(Class<T> navigateClass) {
            intent = new Intent();
            this.navigateClass = navigateClass;
        }

        public ActivityBuilder(Intent intent, Class<T> navigateClass) {
            this.intent = intent;
            this.navigateClass = navigateClass;
        }

        public ActivityBuilder returnResult(int resultCode) {
            if (resultCode <= 0) throw new IllegalArgumentException("please, use result code > 0");
            this.resultCode = resultCode;
            return this;
        }

        /**
         * @see Intent#FLAG_GRANT_READ_URI_PERMISSION
         * @see Intent#FLAG_GRANT_WRITE_URI_PERMISSION
         * @see Intent#FLAG_GRANT_PERSISTABLE_URI_PERMISSION
         * @see Intent#FLAG_GRANT_PREFIX_URI_PERMISSION
         * @see Intent#FLAG_DEBUG_LOG_RESOLUTION
         * @see Intent#FLAG_FROM_BACKGROUND
         * @see Intent#FLAG_ACTIVITY_BROUGHT_TO_FRONT
         * @see Intent#FLAG_ACTIVITY_CLEAR_TASK
         * @see Intent#FLAG_ACTIVITY_CLEAR_TOP
         * @see Intent#FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
         * @see Intent#FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
         * @see Intent#FLAG_ACTIVITY_FORWARD_RESULT
         * @see Intent#FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
         * @see Intent#FLAG_ACTIVITY_MULTIPLE_TASK
         * @see Intent#FLAG_ACTIVITY_NEW_DOCUMENT
         * @see Intent#FLAG_ACTIVITY_NEW_TASK
         * @see Intent#FLAG_ACTIVITY_NO_ANIMATION
         * @see Intent#FLAG_ACTIVITY_NO_HISTORY
         * @see Intent#FLAG_ACTIVITY_NO_USER_ACTION
         * @see Intent#FLAG_ACTIVITY_PREVIOUS_IS_TOP
         * @see Intent#FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
         * @see Intent#FLAG_ACTIVITY_REORDER_TO_FRONT
         * @see Intent#FLAG_ACTIVITY_SINGLE_TOP
         * @see Intent#FLAG_ACTIVITY_TASK_ON_HOME
         * @see Intent#FLAG_RECEIVER_REGISTERED_ONLY
         */
        public ActivityBuilder setFlags(int flags) {
            intent.setFlags(flags);
            return this;
        }

        /**
         * @see Intent#FLAG_GRANT_READ_URI_PERMISSION
         * @see Intent#FLAG_GRANT_WRITE_URI_PERMISSION
         * @see Intent#FLAG_GRANT_PERSISTABLE_URI_PERMISSION
         * @see Intent#FLAG_GRANT_PREFIX_URI_PERMISSION
         * @see Intent#FLAG_DEBUG_LOG_RESOLUTION
         * @see Intent#FLAG_FROM_BACKGROUND
         * @see Intent#FLAG_ACTIVITY_BROUGHT_TO_FRONT
         * @see Intent#FLAG_ACTIVITY_CLEAR_TASK
         * @see Intent#FLAG_ACTIVITY_CLEAR_TOP
         * @see Intent#FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
         * @see Intent#FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
         * @see Intent#FLAG_ACTIVITY_FORWARD_RESULT
         * @see Intent#FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
         * @see Intent#FLAG_ACTIVITY_MULTIPLE_TASK
         * @see Intent#FLAG_ACTIVITY_NEW_DOCUMENT
         * @see Intent#FLAG_ACTIVITY_NEW_TASK
         * @see Intent#FLAG_ACTIVITY_NO_ANIMATION
         * @see Intent#FLAG_ACTIVITY_NO_HISTORY
         * @see Intent#FLAG_ACTIVITY_NO_USER_ACTION
         * @see Intent#FLAG_ACTIVITY_PREVIOUS_IS_TOP
         * @see Intent#FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
         * @see Intent#FLAG_ACTIVITY_REORDER_TO_FRONT
         * @see Intent#FLAG_ACTIVITY_SINGLE_TOP
         * @see Intent#FLAG_ACTIVITY_TASK_ON_HOME
         * @see Intent#FLAG_RECEIVER_REGISTERED_ONLY
         */
        public ActivityBuilder addFlags(int flags) {
            intent.addFlags(flags);
            return this;
        }

        public ActivityBuilder clearBackStack() {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            return this;
        }

        public ActivityBuilder putExtra(String key, Object extra) {
            if (extra instanceof Boolean) {
                intent.putExtra(key, (Boolean) extra);
            } else if (extra instanceof Boolean[]) {
                intent.putExtra(key, (Boolean[]) extra);
            } else if (extra instanceof Byte) {
                intent.putExtra(key, (Byte) extra);
            } else if (extra instanceof Byte[]) {
                intent.putExtra(key, (Byte[]) extra);
            } else if (extra instanceof Character) {
                intent.putExtra(key, (Character) extra);
            } else if (extra instanceof Character[]) {
                intent.putExtra(key, (Character[]) extra);
            } else if (extra instanceof Short) {
                intent.putExtra(key, (Short) extra);
            } else if (extra instanceof Short[]) {
                intent.putExtra(key, (Short[]) extra);
            } else if (extra instanceof Integer) {
                intent.putExtra(key, (Integer) extra);
            } else if (extra instanceof Integer[]) {
                intent.putExtra(key, (Integer[]) extra);
            } else if (extra instanceof Long) {
                intent.putExtra(key, (Long) extra);
            } else if (extra instanceof Long[]) {
                intent.putExtra(key, (Long[]) extra);
            } else if (extra instanceof Float) {
                intent.putExtra(key, (Float) extra);
            } else if (extra instanceof Float[]) {
                intent.putExtra(key, (Float[]) extra);
            } else if (extra instanceof Double) {
                intent.putExtra(key, (Double) extra);
            } else if (extra instanceof Double[]) {
                intent.putExtra(key, (Double[]) extra);
            } else if (extra instanceof String) {
                intent.putExtra(key, (String) extra);
            } else if (extra instanceof String[]) {
                intent.putExtra(key, (String[]) extra);
            } else if (extra instanceof Bundle) {
                intent.putExtra(key, (Bundle) extra);
            } else if (extra instanceof Parcelable[]) {
                intent.putExtra(key, (Parcelable[]) extra);
            } else if (extra instanceof Parcelable) {
                intent.putExtra(key, (Parcelable) extra);
            } else if (extra instanceof Serializable) {
                intent.putExtra(key, (Serializable) extra);
            } else {
                throw new IllegalArgumentException("Wrong extra type [" + extra + "]");
            }
            return this;
        }

        public ActivityBuilder putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
            intent.putParcelableArrayListExtra(name, value);
            return this;
        }

        public ActivityBuilder putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
            intent.putIntegerArrayListExtra(name, value);
            return this;
        }

        public ActivityBuilder putStringArrayListExtra(String name, ArrayList<String> value) {
            intent.putStringArrayListExtra(name, value);
            return this;
        }

        public ActivityBuilder putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
            intent.putCharSequenceArrayListExtra(name, value);
            return this;
        }

        public Intent intent() {
            return intent;
        }

        public Intent intentWithClass(Context context) {
            intent.setClass(context, navigateClass);
            return intent;
        }

        public void start(Context context) {
            intent.setClass(context, navigateClass);
            context.startActivity(intent);
        }

        public void start(Activity activity) {
            intent.setClass(activity, navigateClass);
            if (resultCode > 0) activity.startActivityForResult(intent, resultCode);
            else activity.startActivity(intent);
        }

        public void start(Fragment fragment) {
            intent.setClass(fragment.getContext(), navigateClass);
            if (resultCode > 0) fragment.startActivityForResult(intent, resultCode);
            else fragment.startActivity(intent);
        }

        public void startWithTransition(Activity activity) {
            startWithTransition(activity, (Pair<View, String>[]) null);
        }

        @SafeVarargs
        public final void startWithTransition(Activity activity, Pair<View, String>... shared) {
            intent.setClass(activity, navigateClass);
            if (resultCode > 0) {
                startActivityForResult(activity, intent, resultCode, TransitionHelper.generateBundle(activity, shared));
            } else ActivityCompat.startActivity(activity, intent, TransitionHelper.generateBundle(activity, shared));

        }
    }

    public static class FragmentBuilder implements FragmentAnim, IRouterBuilder.FragmentBuilder {

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

        /**
         * If needed you can set default id res for fragments. Usually call in {@link android.app.Application}
         *
         * @param idResDefault fragment container id;
         */
        public static void setIdResDefault(@IdRes int idResDefault) {
            sIdResDefault = idResDefault;
        }

        @Override
        public FragmentAnim setEnterAnim(@AnimRes int idRes) {
            mEnter = idRes;
            return this;
        }

        @Override
        public FragmentAnim setExitAnim(@AnimRes int idRes) {
            mExit = idRes;
            return this;
        }

        @Override
        public FragmentAnim setPopEnterAnim(@AnimRes int idRes) {
            mPopEnter = idRes;
            return this;
        }

        @Override
        @SafeVarargs
        public final FragmentAnim setSharedElements(Pair<View, String>... sharedElements) {
            mSharedElements = sharedElements;
            return this;
        }

        @Override
        public FragmentAnim setPopExitAnim(@AnimRes int idRes) {
            mPopExit = idRes;
            return this;
        }

        @Override
        public FragmentAnim setDefaultAnim(@AnimDefault int defaultAnim) {
            switch (defaultAnim) {
                case AnimDefault.ANIM_ALPHA:
                    mPopEnter = mEnter = R.anim.fragment_alpha_enter;
                    mPopExit = mExit = R.anim.fragment_alpha_exit;
                    break;
                case AnimDefault.ANIM_SLIDE:
                    mPopEnter = R.anim.fragment_pop_enter;
                    mEnter = R.anim.fragment_enter;
                    mPopExit = R.anim.fragment_pop_exit;
                    mExit = R.anim.fragment_exit;
                    break;
                case AnimDefault.ANIM_BOTTOM_TOP:
                    mPopEnter = R.anim.fragment_pop_bottom_top;
                    mEnter = R.anim.fragment_bottom_top;
                    mPopExit = R.anim.fragment_pop_top_bottom;
                    mExit = R.anim.fragment_top_bottom;
                    break;
            }
            return this;
        }

        @Override
        public FragmentAnim setSharedEnterTransition(Object object) {
            mSharedEnterTransition = object;
            return this;
        }

        @Override
        public FragmentAnim setSharedReturnTransition(Object object) {
            mSharedReturnTransition = object;
            return this;
        }

        @Override
        public FragmentAnim setEnterTransition(Object object) {
            mEnterTransition = object;
            return this;
        }

        @Override
        public FragmentAnim setExitTransition(Object object) {
            mExitTransition = object;
            return this;
        }

        @Override
        public FragmentAnim setReenterTransition(Object object) {
            mReenterTransition = object;
            return this;
        }

        @Override
        public FragmentAnim setReturnTransition(Object object) {
            mReturnTransition = object;
            return this;
        }

        @Override
        public FragmentAnim useCustomAnim(boolean isUse) {
            mUseCustomAnim = isUse;
            return this;
        }

        @Override
        public IRouterBuilder.FragmentBuilder setIdRes(@IdRes int idRes) {
            mIdRes = idRes;
            return this;
        }

        @Override
        public IRouterBuilder.FragmentBuilder setFragment(Fragment fragment) {
            mFragment = fragment;
            return this;
        }

        /**
         * Default <b>{@link Method#REPLACE}</b>
         */
        @Override
        public IRouterBuilder.FragmentBuilder setMethod(Method method) {
            mMethod = method;
            return this;
        }

        /**
         * Default <b>true</b>;
         */
        @Override
        public IRouterBuilder.FragmentBuilder addToBackStack(boolean addToBackStack) {
            mAddToBackStack = addToBackStack;
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
            if (mFragment == null) {
                throw new RouterBuilderException("You must set fragment");
            }
            if (mMethod != Method.ADD && mIdRes <= 0 && sIdResDefault <= 0) {
                throw new RouterBuilderException("Your fragment container id myst be >= 0.\n\n" +
                                                 "You can call in Application onCreate() Router.setIdResDefault()\n");
            }
            int idRes;

            if (mMethod == Method.ADD) {
                idRes = mIdRes > 0 ? mIdRes : android.R.id.content;
            } else {
                idRes = mIdRes > 0 ? mIdRes : sIdResDefault;
            }

            Log.i("start fragment | method: " + mMethod.name() + " id: " + idRes);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            String tag = mFragment.getClass().getName();

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
            }

            if (mEnterTransition != null) mFragment.setEnterTransition(mEnterTransition);
            if (mExitTransition != null) mFragment.setExitTransition(mExitTransition);
            if (mReenterTransition != null) mFragment.setReenterTransition(mReenterTransition);
            if (mReturnTransition != null) mFragment.setReturnTransition(mReturnTransition);

            if (mUseCustomAnim) {
                fragmentTransaction.setCustomAnimations(
                        mEnter == 0 ? R.anim.fragment_enter : mEnter,
                        mExit == 0 ? R.anim.fragment_exit : mExit,
                        mPopEnter == 0 ? R.anim.fragment_pop_enter : mPopEnter,
                        mPopExit == 0 ? R.anim.fragment_pop_exit : mPopExit);
            }

            switch (mMethod) {
                case ADD:
                    if (idRes > 0) {
                        fragmentTransaction.add(idRes, mFragment, tag);
                    } else {
                        fragmentTransaction.add(mFragment, tag);
                    }
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

            fragmentTransaction.setAllowOptimization(true);

            return fragmentTransaction;
        }
    }
}