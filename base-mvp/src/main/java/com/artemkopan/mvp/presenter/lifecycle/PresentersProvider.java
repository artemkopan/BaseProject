package com.artemkopan.mvp.presenter.lifecycle;

import android.annotation.SuppressLint;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.artemkopan.mvp.presenter.BasePresenter;
import com.artemkopan.mvp.presenter.lifecycle.PresenterProvider.Factory;
import com.artemkopan.mvp.presenter.lifecycle.PresenterProvider.NewInstanceFactory;

/**
 * Utilities methods for {@link com.artemkopan.mvp.presenter.lifecycle.PresenterStore} class.
 */
public class PresentersProvider {

    @SuppressLint("StaticFieldLeak")
    private static NewInstanceFactory newInstanceFactory;

    private static void initializeFactoryIfNeeded() {
        if (newInstanceFactory == null) {
            newInstanceFactory = new NewInstanceFactory();
        }
    }

    /**
     * Creates a {@link PresenterProvider}, which retains Presenters while a scope of given
     * {@code fragment} is alive. More detailed explanation is in {@link BasePresenter}.
     * <p>
     * It uses {@link NewInstanceFactory} to instantiate new Presenters.
     *
     * @param fragment a fragment, in whose scope Presenters should be retained
     * @return a PresenterProvider instance
     */
    @MainThread
    public static PresenterProvider of(@NonNull Fragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalArgumentException(
                    "Can't create PresenterProvider for detached fragment");
        }
        initializeFactoryIfNeeded();
        return new PresenterProvider(PresentersStore.of(fragment), newInstanceFactory);
    }

    /**
     * Creates a {@link PresenterProvider}, which retains Presenters while a scope of given Activity
     * is alive. More detailed explanation is in {@link BasePresenter}.
     * <p>
     * It uses {@link NewInstanceFactory} to instantiate new Presenters.
     *
     * @param activity an activity, in whose scope Presenters should be retained
     * @return a PresenterProvider instance
     */
    @MainThread
    public static PresenterProvider of(@NonNull FragmentActivity activity) {
        initializeFactoryIfNeeded();
        return new PresenterProvider(PresentersStore.of(activity), newInstanceFactory);
    }

    /**
     * Creates a {@link PresenterProvider}, which retains Presenters while a scope of given
     * {@code fragment} is alive. More detailed explanation is in {@link BasePresenter}.
     * <p>
     * It uses the given {@link Factory} to instantiate new Presenters.
     *
     * @param fragment a fragment, in whose scope Presenters should be retained
     * @param factory  a {@code Factory} to instantiate new Presenters
     * @return a PresenterProvider instance
     */
    @MainThread
    public static PresenterProvider of(@NonNull Fragment fragment, @NonNull Factory factory) {
        return new PresenterProvider(PresentersStore.of(fragment), factory);
    }

    /**
     * Creates a {@link PresenterProvider}, which retains Presenters while a scope of given Activity
     * is alive. More detailed explanation is in {@link BasePresenter}.
     * <p>
     * It uses the given {@link Factory} to instantiate new Presenters.
     *
     * @param activity an activity, in whose scope Presenters should be retained
     * @param factory  a {@code Factory} to instantiate new Presenters
     * @return a PresenterProvider instance
     */
    @MainThread
    public static PresenterProvider of(@NonNull FragmentActivity activity,
                                       @NonNull Factory factory) {
        return new PresenterProvider(PresentersStore.of(activity), factory);
    }

}
