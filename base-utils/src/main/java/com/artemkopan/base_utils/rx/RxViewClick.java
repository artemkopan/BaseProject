package com.artemkopan.base_utils.rx;

import android.view.View;
import android.view.View.OnClickListener;

import com.artemkopan.base_utils.ExceptionUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created for MediMee.
 * Author Artem Kopan.
 * Date 09.07.2016 10:05
 */

@SuppressWarnings("WeakerAccess")
public class RxViewClick implements ObservableOnSubscribe<View> {

    public static final int TIME_DELAY = 400;

    private WeakReference<View> weakView;

    private RxViewClick(View view) {
        weakView = new WeakReference<>(view);
    }

    public static Observable<View> create(View view) {
        return create(view, TIME_DELAY);
    }

    public static Observable<View> create(View view, int milliseconds) {
        ExceptionUtils.checkNull(view, View.class);
        return Observable.create(new RxViewClick(view)).throttleFirst(milliseconds, TimeUnit.MILLISECONDS);
    }

    public static PublishSubject<View> create(Consumer<View> onNext) {
        return create(onNext, TIME_DELAY);
    }

    public static PublishSubject<View> create(Consumer<View> onNext, int millis) {
        PublishSubject<View> publishSubject = PublishSubject.create();
        publishSubject.throttleFirst(millis, TimeUnit.MILLISECONDS)
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

        if (weakView.get() != null) {
            weakView.get().setOnClickListener(listener);
        }

        subscriber.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                if (weakView.get() != null) {
                    weakView.get().setOnClickListener(null);
                }
                weakView.clear();
            }
        });
    }
}
