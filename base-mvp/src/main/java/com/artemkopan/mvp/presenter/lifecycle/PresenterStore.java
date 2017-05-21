package com.artemkopan.mvp.presenter.lifecycle;/*
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

import com.artemkopan.mvp.presenter.BasePresenter;

import java.util.HashMap;

public class PresenterStore {

    private final HashMap<String, BasePresenter<?>> map = new HashMap<>();

    final void put(String key, BasePresenter<?> viewModel) {
        BasePresenter<?> oldPresenter = map.get(key);
        if (oldPresenter != null) {
            oldPresenter.onCleared();
        }
        map.put(key, viewModel);
    }

    final BasePresenter<?> get(String key) {
        return map.get(key);
    }

    /**
     * Clears internal storage and notifies ViewModels that they are no longer used.
     */
    public final void clear() {
        for (BasePresenter<?> p : map.values()) {
            p.onCleared();
        }
        map.clear();
    }
}
