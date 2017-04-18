package com.artemkopan.utils.rx;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.disposables.Disposable;

/**
 * Created by Artem Kopan for BaseProject
 * 18.04.17
 */

public abstract class SimpleDisposable implements Disposable {

    private final AtomicBoolean unSubscribed = new AtomicBoolean();

    @Override
    public final boolean isDisposed() {
        return unSubscribed.get();
    }

    @Override
    public final void dispose() {
        if (unSubscribed.compareAndSet(false, true)) {
            onDispose();
        }
    }

    protected abstract void onDispose();
}
