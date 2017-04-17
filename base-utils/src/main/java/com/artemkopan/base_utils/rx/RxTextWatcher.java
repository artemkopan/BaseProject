package com.artemkopan.base_utils.rx;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.lang.ref.WeakReference;

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

    private final WeakReference<TextView> weakView;

    private RxTextWatcher(TextView view) {
        this.weakView = new WeakReference<>(view);
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
                if (weakView.get() != null) weakView.get().removeTextChangedListener(watcher);
            }
        });

        if (weakView.get() != null) weakView.get().addTextChangedListener(watcher);
    }
}
