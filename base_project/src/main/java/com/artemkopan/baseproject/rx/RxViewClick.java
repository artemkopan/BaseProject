package com.artemkopan.baseproject.rx;

import android.view.View;
import android.view.View.OnClickListener;

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

    private static final int TIME_DELAY = 700;

    private WeakReference<View> mViewWeak;

    public RxViewClick(View view) {
        mViewWeak = new WeakReference<>(view);
    }

    public static Observable<View> create(View view, PublishSubject<Object> mDestroySubject) {
        return create(view, mDestroySubject, TIME_DELAY);
    }

    public static Observable<View> create(View view, PublishSubject<Object> mDestroySubject, int milliseconds) {
        if (view == null) return Observable.empty();

        return Observable.create(new RxViewClick(view))
                .takeUntil(mDestroySubject)
                .throttleFirst(milliseconds, TimeUnit.MILLISECONDS);
    }

    public static PublishSubject<View> create(Consumer<View> onNext, PublishSubject<Object> mDestroySubject) {
        PublishSubject<View> publishSubject = PublishSubject.create();
        publishSubject
                .throttleFirst(TIME_DELAY, TimeUnit.MILLISECONDS)
                .takeUntil(mDestroySubject)
                .subscribe(onNext);
        return publishSubject;
    }

    @Override
    public void subscribe(final ObservableEmitter<View> subscriber) throws Exception {

//        ExtraUtils.checkUiThread();

        final View.OnClickListener listener = new OnClickListener() {
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
            }
        });
    }
}
