package com.artemkopan.baseproject.rx;

import android.view.View;
import android.view.View.OnClickListener;

import com.artemkopan.baseproject.internal.UiManager.RxLifeCycle;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;

/**
 * Created for MediMee.
 * Author Artem Kopan.
 * Date 09.07.2016 10:05
 */

@SuppressWarnings("WeakerAccess")
public class RxViewsClick implements ObservableOnSubscribe<View> {

    private WeakReference<View[]> mViewWeak;

    public RxViewsClick(View... view) {
        mViewWeak = new WeakReference<>(view);
    }

    public static Observable<View> create(Observable<RxLifeCycle> mDestroySubject, View... views) {
        return create(mDestroySubject, RxViewClick.TIME_DELAY, views);
    }

    public static Observable<View> create(Observable<RxLifeCycle> mDestroySubject, int milliseconds, View... views) {
        if (views == null || views.length == 0) return Observable.empty();

        return Observable.create(new RxViewsClick(views))
                         .takeUntil(mDestroySubject)
                         .throttleFirst(milliseconds, TimeUnit.MILLISECONDS);
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
            for (View view : mViewWeak.get()) {
                view.setOnClickListener(listener);
            }
        }

        subscriber.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                if (mViewWeak.get() != null) {
                    for (View view : mViewWeak.get()) {
                        view.setOnClickListener(null);
                    }
                }
                mViewWeak.clear();
            }
        });
    }
}
