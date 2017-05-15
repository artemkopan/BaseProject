package com.artemkopan.mvp.presenter;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import com.artemkopan.mvp.view.BaseView;

public final class PresenterLoader<T extends BasePresenter<V>, V extends BaseView> extends Loader<T> {

    private final PresenterFactory<T, V> factory;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory<T, V> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {
        Log.i("loader", "onStartLoading-");

        // if we already own a presenter instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        Log.i("loader", "onForceLoad-");

        // Create the Presenter using the Factory
        presenter = factory.create();

        // Deliver the result
        deliverResult(presenter);
    }

    @Override
    public void deliverResult(T data) {
        super.deliverResult(data);
        Log.i("loader", "deliverResult-");
    }

    @Override
    protected void onStopLoading() {
        Log.i("loader", "onStopLoading-");
    }

    @Override
    protected void onReset() {
        Log.i("loader", "onReset-");
        if (presenter != null) {
            presenter.onDestroyed();
            presenter = null;
        }
    }

    public T getPresenter() {
        return presenter;
    }
}
