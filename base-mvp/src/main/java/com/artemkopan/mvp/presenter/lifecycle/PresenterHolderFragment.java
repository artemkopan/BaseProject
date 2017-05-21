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

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PresenterHolderFragment extends Fragment {

    public static final String HOLDER_TAG = "com.artemkopan.mvp.presenter.lifecycle.PresenterHolderFragment";

    private static final String LOG_TAG = "PresenterHolderFragment";

    private static final HolderFragmentManager holderFragmentManager = new HolderFragmentManager();

    private PresenterStore presenterStore = new PresenterStore();

    public PresenterHolderFragment() {
        setRetainInstance(true);
    }

    public static PresenterHolderFragment holderFragmentFor(FragmentActivity activity) {
        return holderFragmentManager.holderFragmentFor(activity);
    }

    public static PresenterHolderFragment holderFragmentFor(Fragment fragment) {
        return holderFragmentManager.holderFragmentFor(fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        holderFragmentManager.holderFragmentCreated(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterStore.clear();
    }

    public PresenterStore getPresenterStore() {
        return presenterStore;
    }

    @SuppressWarnings("WeakerAccess")
    static class HolderFragmentManager {

        private Map<Activity, PresenterHolderFragment> notCommittedActivityHolders = new HashMap<>();
        private Map<Fragment, PresenterHolderFragment> notCommittedFragmentHolders = new HashMap<>();

        private ActivityLifecycleCallbacks activityCallbacks =
                new EmptyActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        PresenterHolderFragment fragment = notCommittedActivityHolders.remove(activity);
                        if (fragment != null) {
                            Log.e(LOG_TAG, "Failed to save a ViewModel for " + activity);
                        }
                    }
                };

        private boolean activityCallbacksIsAdded = false;

        private FragmentLifecycleCallbacks parentDestroyedCallback =
                new FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentDestroyed(FragmentManager fm, Fragment parentFragment) {
                        PresenterHolderFragment fragment = notCommittedFragmentHolders.remove(parentFragment);
                        if (fragment != null) {
                            Log.e(LOG_TAG, "Failed to save a ViewModel for " + parentFragment);
                        }
                    }
                };

        private static PresenterHolderFragment findHolderFragment(FragmentManager manager) {
            if (manager.isDestroyed()) {
                throw new IllegalStateException("Can't access ViewModels from onDetach");
            }

            Fragment fragmentByTag = manager.findFragmentByTag(HOLDER_TAG);
            if (fragmentByTag != null && !(fragmentByTag instanceof PresenterHolderFragment)) {
                throw new IllegalStateException("Unexpected "
                                                + "fragment instance was returned by HOLDER_TAG");
            }
            return (PresenterHolderFragment) fragmentByTag;
        }

        private static PresenterHolderFragment createHolderFragment(FragmentManager fragmentManager) {
            PresenterHolderFragment holder = new PresenterHolderFragment();
            fragmentManager.beginTransaction().add(holder, HOLDER_TAG).commitAllowingStateLoss();
            return holder;
        }

        void holderFragmentCreated(Fragment holderFragment) {
            Fragment parentFragment = holderFragment.getParentFragment();
            if (parentFragment != null) {
                notCommittedFragmentHolders.remove(parentFragment);
                parentFragment.getFragmentManager().unregisterFragmentLifecycleCallbacks(
                        parentDestroyedCallback);
            } else {
                notCommittedActivityHolders.remove(holderFragment.getActivity());
            }
        }

        PresenterHolderFragment holderFragmentFor(FragmentActivity activity) {
            FragmentManager fm = activity.getSupportFragmentManager();
            PresenterHolderFragment holder = findHolderFragment(fm);
            if (holder != null) {
                return holder;
            }
            holder = notCommittedActivityHolders.get(activity);
            if (holder != null) {
                return holder;
            }

            if (!activityCallbacksIsAdded) {
                activityCallbacksIsAdded = true;
                activity.getApplication().registerActivityLifecycleCallbacks(activityCallbacks);
            }
            holder = createHolderFragment(fm);
            notCommittedActivityHolders.put(activity, holder);
            return holder;
        }

        PresenterHolderFragment holderFragmentFor(Fragment parentFragment) {
            FragmentManager fm = parentFragment.getChildFragmentManager();
            PresenterHolderFragment holder = findHolderFragment(fm);
            if (holder != null) {
                return holder;
            }
            holder = notCommittedFragmentHolders.get(parentFragment);
            if (holder != null) {
                return holder;
            }

            parentFragment.getFragmentManager()
                          .registerFragmentLifecycleCallbacks(parentDestroyedCallback, false);
            holder = createHolderFragment(fm);
            notCommittedFragmentHolders.put(parentFragment, holder);
            return holder;
        }
    }
}
