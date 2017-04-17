package com.artemkopan.baseproject.rx.rx;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

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

public class RxTextWatcher implements ObservableOnSubscribe<CharSequence> {

    final TextView view;

    private RxTextWatcher(TextView view) {
        this.view = view;
    }

    public static Observable<CharSequence> create(TextView view, Observable<RxLifeCycle> destroySubject) {
        return create(view).takeUntil(destroySubject);
    }

    public static Observable<CharSequence> create(TextView view) {
        return Observable.create(new RxTextWatcher(view));
    }

    @Override
    public void subscribe(final ObservableEmitter<CharSequence> subscriber) throws Exception {
        verifyMainThread();

        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!subscriber.isDisposed()) {
                    subscriber.onNext(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        subscriber.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                view.removeTextChangedListener(watcher);
            }
        });

        view.addTextChangedListener(watcher);
    }
}
