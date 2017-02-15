package com.artemkopan.baseproject.rx;

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

import static com.artemkopan.baseproject.rx.RxViewClick.TIME_DELAY;

/**
 * Created for MediMee.
 * Author Artem Kopan.
 * Date 09.07.2016 10:05
 */

@SuppressWarnings("WeakerAccess")
public class RxDrawableClick implements ObservableOnSubscribe<TextView> {

    private WeakReference<TextView> viewWeak;
    private @DrawableIndex int pos;

    public RxDrawableClick(TextView textView, @DrawableIndex int pos) {
        viewWeak = new WeakReference<>(textView);
        this.pos = pos;
    }

    public static Observable<TextView> create(TextView view, @DrawableIndex int pos, Observable<RxLifeCycle>
            mDestroySubject) {
        return create(view, pos, mDestroySubject, TIME_DELAY);
    }

    public static Observable<TextView> create(TextView view, @DrawableIndex int pos,
                                              Observable<RxLifeCycle> mDestroySubject,
                                              int milliseconds) {
        if (view == null) return Observable.empty();

        return Observable.create(new RxDrawableClick(view, pos))
                         .takeUntil(mDestroySubject)
                         .throttleFirst(milliseconds, TimeUnit.MILLISECONDS);
    }

    @Override
    public void subscribe(final ObservableEmitter<TextView> subscriber) throws Exception {

        final OnTouchListener listener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!subscriber.isDisposed() && viewWeak.get() != null) {
                    if (ViewUtils.onDrawableClick(event, viewWeak.get(), pos, 0)) {
                        subscriber.onNext(viewWeak.get());
                        return true;
                    }
                }
                return true;
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
}