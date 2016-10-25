package com.artemkopan.baseproject.rx;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.disposables.Disposable;

/**
 * Created for jabrool
 * Author: Kopan Artem
 * 25.10.2016
 */

public abstract class SimpleDisposable implements Disposable {

    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    @Override
    public final boolean isDisposed() {
        return unsubscribed.get();
    }

    @Override
    public final void dispose() {
        if (unsubscribed.compareAndSet(false, true)) {
            onDispose();
        }
    }

    protected abstract void onDispose();
}

