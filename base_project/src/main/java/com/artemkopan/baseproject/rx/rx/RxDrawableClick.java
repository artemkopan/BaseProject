package com.artemkopan.baseproject.rx.rx;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.artemkopan.baseproject.internal.UiManager.RxLifeCycle;
import com.artemkopan.baseproject.utils.ViewUtils;
import com.artemkopan.baseproject.utils.ViewUtils.DrawableIndex;

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
public class RxDrawableClick implements ObservableOnSubscribe<TextView> {

    private WeakReference<TextView> viewWeak;
    private @DrawableIndex int pos;
    private int motionEvent;
    private boolean defaultReturn = true, clickReturn = false;

    public static Builder builder() {
        return new Builder();
    }

    private RxDrawableClick(Builder builder) {
        viewWeak = builder.viewWeak;
        pos = builder.pos;
        motionEvent = builder.motionEvent;
        defaultReturn = builder.defaultReturn;
        clickReturn = builder.clickReturn;
    }

    @Override
    public void subscribe(final ObservableEmitter<TextView> subscriber) throws Exception {

        final OnTouchListener listener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!subscriber.isDisposed() && viewWeak.get() != null) {
                    if (ViewUtils.onDrawableClick(event, motionEvent, viewWeak.get(), pos, 0)) {
                        subscriber.onNext(viewWeak.get());
                        return clickReturn;
                    }
                }
                return defaultReturn;
            }
        };

        if (viewWeak.get() != null) {
            viewWeak.get().setOnTouchListener(listener);
        }

        subscriber.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                if (viewWeak.get() != null) {
                    viewWeak.get().setOnTouchListener(null);
                }
                viewWeak.clear();
            }
        });
    }

    public static final class Builder {

        private WeakReference<TextView> viewWeak;
        private int pos;
        private int motionEvent = MotionEvent.ACTION_UP;
        private boolean defaultReturn = false;
        private boolean clickReturn = true;

        private Builder() {}

        public Builder view(TextView val) {
            viewWeak = new WeakReference<>(val);
            return this;
        }

        public Builder pos(@DrawableIndex int val) {
            pos = val;
            return this;
        }

        public Builder motionEvent(int val) {
            motionEvent = val;
            return this;
        }

        public Builder defaultReturn(boolean val) {
            defaultReturn = val;
            return this;
        }

        public Builder clickReturn(boolean val) {
            clickReturn = val;
            return this;
        }

        public Observable<TextView> build(Observable<RxLifeCycle> destroyObservable) {
            return build(destroyObservable, RxViewClick.TIME_DELAY);
        }

        public Observable<TextView> build(Observable<RxLifeCycle> destroyObservable, int milliseconds) {
            return build().takeUntil(destroyObservable)
                          .throttleFirst(milliseconds, TimeUnit.MILLISECONDS);
        }

        public Observable<TextView> build() {return Observable.create(new RxDrawableClick(this));}

    }
}