/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.artemkopan.mvp.presenter.lifecycle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.artemkopan.mvp.presenter.BasePresenter;
import com.artemkopan.mvp.view.BaseView;

/**
 * An utility class that provides {@code Presenters} for a scope.
 * <p>
 * Default {@code PresenterProvider} for an {@code Activity} or a {@code Fragment} can be obtained
 * from {@link PresentersProvider} class.
 */
@SuppressWarnings("WeakerAccess")
public class PresenterProvider {

    private static final String DEFAULT_KEY = "com.artemkopan.mvp.presenter.PresenterProvider.DefaultKey";

    /**
     * Implementations of {@code Factory} interface are responsible to instantiate Presenters.
     */
    public interface Factory {

        /**
         * Creates a new instance of the given {@code Class}.
         * <p>
         *
         * @param modelClass a {@code Class} whose instance is requested
         * @param <T>        The type parameter for the Presenter.
         * @return a newly created Presenter
         */
        <T extends BasePresenter<? extends BaseView>> T create(Class<T> modelClass);
    }

    private final Factory factory;
    private final PresenterStore presenterStore;

    /**
     * Creates {@code PresenterProvider}, which will create {@code Presenters} via the given
     * {@code Factory} and retain them in the given {@code store}.
     *
     * @param store   {@code PresenterStore} where Presenters will be stored.
     * @param factory factory a {@code Factory} which will be used to instantiate
     *                new {@code Presenters}
     */
    public PresenterProvider(PresenterStore store, Factory factory) {
        this.factory = factory;
        this.presenterStore = store;
    }

    /**
     * Returns an existing Presenter or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this {@code PresenterProvider}.
     * <p>
     * The created Presenter is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param modelClass The class of the Presenter to create an instance of it if it is not
     *                   present.
     * @param <T>        The type parameter for the Presenter.
     * @return A Presenter that is an instance of the given type {@code T}.
     */
    public <T extends BasePresenter<? extends BaseView>> T get(Class<T> modelClass) {
        String canonicalName = modelClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return get(DEFAULT_KEY + ":" + canonicalName, modelClass);
    }

    /**
     * Returns an existing Presenter or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this {@code PresenterProvider}.
     * <p>
     * The created Presenter is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param key        The key to use to identify the Presenter.
     * @param modelClass The class of the Presenter to create an instance of it if it is not
     *                   present.
     * @param <T>        The type parameter for the Presenter.
     * @return A Presenter that is an instance of the given type {@code T}.
     */
    @NonNull
    @MainThread
    public <T extends BasePresenter<? extends BaseView>> T get(@NonNull String key, @NonNull Class<T> modelClass) {
        BasePresenter<? extends BaseView> presenter = presenterStore.get(key);

        if (modelClass.isInstance(presenter)) {
            //noinspection unchecked
            return (T) presenter;
        } else {
            //noinspection StatementWithEmptyBody
            if (presenter != null) {
                // TODO: log a warning.
            }
        }

        presenter = factory.create(modelClass);
        presenterStore.put(key, presenter);
        //noinspection unchecked
        return (T) presenter;
    }

    /**
     * Simple factory, which calls empty constructor on the give class.
     */
    public static class NewInstanceFactory implements Factory {

        @Override
        public <T extends BasePresenter<? extends BaseView>> T create(Class<T> modelClass) {
            //noinspection TryWithIdenticalCatches
            try {
                return modelClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
    }
}
