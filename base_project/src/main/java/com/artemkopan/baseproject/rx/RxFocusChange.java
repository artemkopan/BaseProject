package com.artemkopan.baseproject.rx;

import android.view.View;
import android.view.View.OnFocusChangeListener;

import com.artemkopan.baseproject.internal.UiManager.RxLifeCycle;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

/**
 * Created for jabrool
 * Author: Kopan Artem
 * 21.10.2016
 */

public class RxFocusChange implements ObservableOnSubscribe<View> {

    final View view;

    private RxFocusChange(View view) {
        this.view = view;
    }

    public static Observable<View> create(View view, Observable<RxLifeCycle> destroySubject) {
        return create(view).takeUntil(destroySubject);
    }

    public static Observable<View> create(View view) {
        return Observable.create(new RxFocusChange(view));
    }

    @Override
    public void subscribe(final ObservableEmitter<View> subscriber) throws Exception {
        verifyMainThread();

        OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!subscriber.isDisposed()) {
                    subscriber.onNext(v);
                }
            }
        };

        subscriber.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                view.setOnFocusChangeListener(null);
            }
        });

        view.setOnFocusChangeListener(onFocusChangeListener);
    }
}
