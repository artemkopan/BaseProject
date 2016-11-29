package com.artemkopan.baseproject.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class BaseRx {

    public static final Object TRIGGER = new Object();

    protected final PublishSubject<Object> mDestroySubject;

    public BaseRx(PublishSubject<Object> destroySubject) {
        mDestroySubject = destroySubject;
    }


    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> delayObserver(final int millis) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> tObservable) {
                return tObservable.zipWith(Observable.timer(millis, TimeUnit.MILLISECONDS),
                                           new BiFunction<T, Long, T>() {
                                               @Override
                                               public T apply(T t, Long aLong) throws Exception {
                                                   return t;
                                               }
                                           });
            }
        };
    }


    public <T> ObservableTransformer<T, T> applyLifecycle() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> tObservable) {
                return tObservable.takeUntil(mDestroySubject);
            }
        };
    }
}
