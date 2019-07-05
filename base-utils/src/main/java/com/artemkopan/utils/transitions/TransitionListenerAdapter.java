package com.artemkopan.utils.transitions;


import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.Transition.TransitionListener;

/**
 * Created for medimee
 * by Kopan Artem on 25.09.2016.
 */

@RequiresApi(api = VERSION_CODES.KITKAT)
public abstract class TransitionListenerAdapter implements TransitionListener {

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {

    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }
}
