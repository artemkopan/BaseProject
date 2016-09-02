package com.artemkopan.baseproject.rx;

import android.content.Context;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.ExtraUtils;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;


public class BaseRx {

    protected final PublishSubject<Lifecycle> mRxLifecycle;

    public BaseRx(PublishSubject<Lifecycle> rxLifecycle) {
        mRxLifecycle = rxLifecycle;
    }

    public static <T> Observable.Transformer<T, T> checkInternetConnection(final Context context) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                if (!ExtraUtils.checkInternetConnection(context)) {
                    return Observable.error(
                            new IOException(context.getString(R.string.base_error_internet)));
                } else {
                    return tObservable;
                }
            }
        };
    }

    public static <T> Observable.Transformer<T, T> applySchedulers() {
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
