package com.artemkopan.baseproject.rx;

import android.view.View;
import android.view.View.OnClickListener;

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
    private final View mView;

    public RxViewClick(View view) {
        mView = view;
    }

    public static Observable<View> create(View view) {
        return create(view, TIME_DELAY);
    }

    public static Observable<View> create(View view, int milliseconds) {
        if (view == null) return Observable.empty();

        return Observable.create(new RxViewClick(view))
                .throttleFirst(milliseconds, TimeUnit.MILLISECONDS);
    }

    public static PublishSubject<View> create(Consumer<View> onNext) {
        PublishSubject<View> publishSubject = PublishSubject.create();
        publishSubject.throttleFirst(TIME_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(onNext);
        return publishSubject;
    }

    @Override
    public void subscribe(final ObservableEmitter<View> subscriber) throws Exception {

//        ExtraUtils.checkUiThread();

        View.OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subscriber.isDisposed()) {
                    subscriber.onNext(v);
                }
            }
        };

        mView.setOnClickListener(listener);

        subscriber.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                mView.setOnClickListener(null);
            }
        });
    }
}
