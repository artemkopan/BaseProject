package com.artemkopan.baseproject.rx.rx;

import android.view.View;
import android.view.View.OnClickListener;

import com.artemkopan.baseproject.internal.UiManager.RxLifeCycle;
import com.jakewharton.rxrelay2.PublishRelay;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created for MediMee.
 * Author Artem Kopan.
 * Date 09.07.2016 10:05
 */

@SuppressWarnings("WeakerAccess")
public class RxViewClick implements ObservableOnSubscribe<View> {

    public static final int TIME_DELAY = 400;

    private WeakReference<View> mViewWeak;

    public RxViewClick(View view) {
        mViewWeak = new WeakReference<>(view);
    }

    public static Observable<View> create(View view, Observable<RxLifeCycle> destroySubject) {
        return create(view, TIME_DELAY).takeUntil(destroySubject);
    }

    public static Observable<View> create(View view, Observable<RxLifeCycle> destroySubject, int milliseconds) {
        return create(view, milliseconds).takeUntil(destroySubject);

    }

    public static Observable<View> create(View view) {
        return create(view, TIME_DELAY);
    }

    public static Observable<View> create(View view, int milliseconds) {
        if (view == null) return Observable.empty();

        return Observable.create(new RxViewClick(view))

                         .throttleFirst(milliseconds, TimeUnit.MILLISECONDS);
    }

    public static PublishRelay<View> create(Consumer<View> onNext, Observable<RxLifeCycle> destroySubject) {
        return create(onNext, destroySubject, TIME_DELAY);
    }

    public static PublishRelay<View> create(Consumer<View> onNext, Observable<RxLifeCycle> destroySubject, int millis) {
        PublishRelay<View> publishSubject = PublishRelay.create();
        publishSubject
                .throttleFirst(millis, TimeUnit.MILLISECONDS)
                .takeUntil(destroySubject)
                .subscribe(onNext);

        return publishSubject;
    }

    public static PublishRelay<View> create(Consumer<View> onNext) {
        return create(onNext, TIME_DELAY);
    }

    public static PublishRelay<View> create(Consumer<View> onNext, int millis) {
        PublishRelay<View> publishSubject = PublishRelay.create();
        publishSubject
                .throttleFirst(millis, TimeUnit.MILLISECONDS)
                .subscribe(onNext);

        return publishSubject;
    }

    @Override
    public void subscribe(final ObservableEmitter<View> subscriber) throws Exception {

        final OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subscriber.isDisposed()) {
                    subscriber.onNext(v);
                }
            }
        };

        if (mViewWeak.get() != null) {
            mViewWeak.get().setOnClickListener(listener);
        }

        subscriber.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                if (mViewWeak.get() != null) {
                    mViewWeak.get().setOnClickListener(null);
                }
                mViewWeak.clear();
            }
        });
    }
}
