package com.artemkopan.baseproject.rx;

import com.artemkopan.baseproject.presenter.MvpView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created for jabrool
 * Author: Kopan Artem
 * 24.10.2016
 */

public class RxComplete<T> implements ObservableOnSubscribe<T> {

    private MvpView mMvpView;

    private RxComplete(MvpView mvpView) {
        mMvpView = mvpView;
    }

    public static <T> Observable<T> create() {
        return create(null);
    }

    public static <T> Observable<T> create(MvpView mvpView) {
        return Observable.create(new RxComplete<T>(mvpView));
    }

    @Override
    public void subscribe(ObservableEmitter<T> e) throws Exception {
        e.onComplete();
        if (mMvpView != null) {
            AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    mMvpView.hideProgress(null);
                }
            });
        }
    }
}
