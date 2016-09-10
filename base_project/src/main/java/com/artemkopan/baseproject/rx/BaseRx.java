package com.artemkopan.baseproject.rx;

import android.content.Context;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.ExtraUtils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class BaseRx {

    protected final PublishSubject<Lifecycle> mRxLifecycle;

    public BaseRx(PublishSubject<Lifecycle> rxLifecycle) {
        mRxLifecycle = rxLifecycle;
    }

    public static <T> ObservableTransformer<T, T> checkInternetConnection(final Context context) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> tObservable) throws Exception {
                if (!ExtraUtils.checkInternetConnection(context)) {
                    return Observable.error(
                            new IOException(context.getString(R.string.base_error_internet)));
                } else {
                    return tObservable;
                }
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> tObservable) throws Exception {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public <T> ObservableTransformer<T, T> applyLifecycle() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> tObservable) throws Exception {
                return tObservable.takeUntil(mRxLifecycle);
            }
        };
    }
}
