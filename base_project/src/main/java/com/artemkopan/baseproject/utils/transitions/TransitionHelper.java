package com.artemkopan.baseproject.utils.transitions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.transition.Transition;
import android.view.View;
import android.view.ViewTreeObserver;

import com.artemkopan.baseproject.utils.Log;
import com.artemkopan.baseproject.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created for jabrool
 * by Kopan Artem on 04.11.2016.
 */

@SuppressWarnings("WeakerAccess")
public class TransitionHelper {

    public static void onEnterTransitionEndAction(Activity activity, final Runnable actionEnd) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            onTransitionEndAction(activity.getWindow().getEnterTransition(), actionEnd);
        } else if (actionEnd != null) {
            actionEnd.run();
        }
    }

    public static void onEnterTransitionEndAction(Fragment fragment, final Runnable actionEnd) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP &&
            ObjectUtils.instanceOf(fragment.getEnterTransition(), Transition.class)) {
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
            transition.addListener(new TransitionListenerAdapter() {
                @Override
                @TargetApi(VERSION_CODES.KITKAT)
                public void onTransitionEnd(Transition transition) {
                    Log.d("onTransitionEnd");
                    transition.removeListener(this);
                    if (actionEnd != null) actionEnd.run();
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    Log.d("onTransitionCancel");
                    if (actionEnd != null) actionEnd.run();
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
    public static Bundle generateBundle(Activity activity, Pair<View, String>... elements) {
        return generateBundle(activity, true, elements);
    }

    @SafeVarargs
    @Nullable
    public static Bundle generateBundle(Activity activity, boolean includeStatusBar, Pair<View, String>... elements) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            Pair<View, String>[] pairs = createSafeTransitionParticipants(activity, includeStatusBar, elements);
            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs).toBundle();
        } else {
            return null;
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public static void waitStartTransition(final Activity activity) {
        if (!(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) || activity == null)
            return;
        final View decor = activity.getWindow().getDecorView();
        waitStartTransition(activity, decor);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public static void waitStartTransition(final Activity activity, final View view) {
        if (!(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) || activity == null)
            return;
        activity.postponeEnterTransition();
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (view.getViewTreeObserver().isAlive()) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                activity.startPostponedEnterTransition();
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