package com.artemkopan.baseproject.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;


public class BaseRx {

    protected final PublishSubject<Lifecycle> mRxLifecycle;

    public BaseRx(PublishSubject<Lifecycle> rxLifecycle) {
        mRxLifecycle = rxLifecycle;
    }

    public <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public <T> Observable.Transformer<T, T> applyLifecycle() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.takeUntil(mRxLifecycle);
            }
        };
    }
}
