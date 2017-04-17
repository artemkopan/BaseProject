package com.artemkopan.base_utils.transitions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created for jabrool
 * by Kopan Artem on 04.11.2016.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class TransitionHelper {

    private static final String TAG = "TransitionHelper";

    public static void onEnterTransitionEndAction(Activity activity, final Runnable actionEnd) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            onTransitionEndAction(activity.getWindow().getEnterTransition(), actionEnd);
        } else if (actionEnd != null) {
            actionEnd.run();
        }
    }

    public static void onEnterTransitionEndAction(Fragment fragment, final Runnable actionEnd) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            onTransitionEndAction((Transition) fragment.getEnterTransition(), actionEnd);
        } else if (actionEnd != null) {
            actionEnd.run();
        }
    }

    public static void onEnterSharedTransitionEndAction(Activity activity, final Runnable actionEnd) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            onTransitionEndAction(activity.getWindow().getSharedElementEnterTransition(), actionEnd);
        } else if (actionEnd != null) {
            actionEnd.run();
        }
    }

    public static void onTransitionEndAction(Transition transition, final Runnable actionEnd) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && transition != null) {
            final Disposable autoRun = Observable
                    .timer(700, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if (actionEnd != null) {
                                actionEnd.run();
                            }
                        }
                    });

            transition.addListener(new TransitionListenerAdapter() {
                @Override
                @TargetApi(VERSION_CODES.KITKAT)
                public void onTransitionEnd(Transition transition) {
                    Log.d(TAG, "onTransitionEnd");
                    transition.removeListener(this);
                    if (actionEnd != null) actionEnd.run();
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    Log.d(TAG, "onTransitionCancel");
                    if (actionEnd != null) actionEnd.run();
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    autoRun.dispose();
                }
            });
            return;
        }
        if (actionEnd != null) {
            actionEnd.run();
        }
    }

    @SafeVarargs
    @Nullable
    public static Bundle generateBundle(Activity activity, Pair<? extends View, String>... elements) {
        return generateBundle(activity, true, elements);
    }

    @SafeVarargs
    @Nullable
    public static Bundle generateBundle(Activity activity, boolean includeStatusBar, Pair<? extends View, String>... elements) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            Pair<View, String>[] pairs = createSafeTransitionParticipants(activity, includeStatusBar, elements);
            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs).toBundle();
        } else {
            return null;
        }
    }

    public static void waitStartEnterTransition(final Activity activity) {
        if (activity == null) return;
        waitStartEnterTransition(activity, activity.getWindow().getDecorView());
    }

    public static void waitStartEnterTransition(final Activity activity, final View view) {
        if (activity == null) return;
        ActivityCompat.postponeEnterTransition(activity);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (view.getViewTreeObserver().isAlive()) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                ActivityCompat.startPostponedEnterTransition(activity);
                return true;
            }
        });
    }

    public static void waitStartEnterTransition(final Fragment fragment) {
        if (fragment == null) return;
        waitStartEnterTransition(fragment, fragment.getView());
    }

    public static void waitStartEnterTransition(final Fragment fragment, final View view) {
        fragment.postponeEnterTransition();
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (view.getViewTreeObserver().isAlive()) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                fragment.startPostponedEnterTransition();
                return true;
            }
        });
    }

    /**
     * Create the transition participants required during a activity transition while
     * avoiding glitches with the system UI.
     *
     * @param activity         The activity used as start for the transition.
     * @param includeStatusBar If false, the status bar will not be added as the transition
     *                         participant.
     * @return All transition participants.
     */
    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity,
                                                                        boolean includeStatusBar,
                                                                        @Nullable Pair... otherParticipants) {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        // Create pair of transition participants.
        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        // only add transition participants if there's at least one none-null element
        if (otherParticipants != null && !(otherParticipants.length == 1 && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        //noinspection unchecked
        return participants.toArray(new Pair[participants.size()]);
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    private static void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
    }
}