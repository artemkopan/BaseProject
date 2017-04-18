package com.artemkopan.utils.rx;

import android.view.View;
import android.view.View.OnClickListener;

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

@SuppressWarnings({"WeakerAccess", "unused"})
public class RxViewsClick implements ObservableOnSubscribe<View> {

    private WeakReference<View[]> weakViews;

    private RxViewsClick(View... view) {
        weakViews = new WeakReference<>(view);
    }

    public static Observable<View> create(View... views) {
        return create(RxViewClick.TIME_DELAY, views);
    }

    public static Observable<View> create(int milliseconds, View... views) {
        if (views == null || views.length == 0) return Observable.empty();

        return Observable.create(new RxViewsClick(views))
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

        if (weakViews.get() != null) {
            for (View view : weakViews.get()) {
                view.setOnClickListener(listener);
            }
        }

        subscriber.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                if (weakViews.get() != null) {
                    for (View view : weakViews.get()) {
                        view.setOnClickListener(null);
                    }
                }
                weakViews.clear();
            }
        });
    }
}
