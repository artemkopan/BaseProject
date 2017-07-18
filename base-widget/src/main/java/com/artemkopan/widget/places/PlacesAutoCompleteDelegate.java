package com.artemkopan.widget.places;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.artemkopan.widget.places.PlacesAutoCompleteView.OnLocationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Artem Kopan for BaseProject
 * 18.07.2017
 */

class PlacesAutoCompleteDelegate {

    private static final String TAG = "PlacesAutoComplete";
    static final int DEBOUNCE = 800;
    static final int MIN_START_SEARCH = 2;
    static final int MAX_RESULT = 10;

    private @Nullable ArrayAdapter<Address> addressAdapter;

    private Geocoder geocoder;
    private Predicate<String> filter;
    private Consumer<List<Address>> consumer;
    private Function<CharSequence, String> convert;
    private PublishSubject<CharSequence> textChangeSubject;
    private Function<String, ObservableSource<List<Address>>> findItem;
    private OnLocationListener loadingListener;

    @LayoutRes
    private int layoutRes = android.R.layout.simple_dropdown_item_1line;
    private int debounce = DEBOUNCE;
    private int minStartSearch = MIN_START_SEARCH;
    private int maxResult = MAX_RESULT;
    private boolean blocked;
    private String oldText;
    private Disposable subscribe;

    PlacesAutoCompleteDelegate(Context context) {
        geocoder = new Geocoder(context);
        textChangeSubject = PublishSubject.create();
    }

    void setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
    }

    void setDebounce(int debounce) {
        this.debounce = debounce;
    }

    void setMinStartSearch(int minStartSearch) {
        this.minStartSearch = minStartSearch;
    }

    void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    void setOldText(String oldText) {
        this.oldText = oldText;
    }

    void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    ArrayAdapter<Address> initAdapter(Context context) {
        addressAdapter = new PlacesAdapter(context, layoutRes, new ArrayList<Address>(MAX_RESULT));
        return addressAdapter;
    }

    void setAdapter(ArrayAdapter<Address> adapter) {
        this.addressAdapter = adapter;
    }

    void setLoadingListener(OnLocationListener loadingListener) {
        this.loadingListener = loadingListener;
    }

    void onTextChanged(CharSequence text) {
        if (text.length() >= minStartSearch && textChangeSubject != null && !blocked) {
            Log.i(TAG, "onNext " + text + " " + oldText);
            textChangeSubject.onNext(PlacesAutoCompleteUtils.trim(text));
        } else if (!blocked && addressAdapter != null) {
            Log.i(TAG, "clear");
            addressAdapter.clear();
        }
    }

    void onAttachedToWindow() {
        subscribe = textChangeSubject
                .subscribeOn(Schedulers.io())
                .map(getRxConvert())
                .filter(getRxFilter())
                .debounce(debounce, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .switchMap(getRxFinder())
                .subscribe(getRxConsumer(), new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (loadingListener != null) loadingListener.error(throwable);
                    }
                });
    }

    void onDetachedFromWindow() {
        textChangeSubject.onComplete();
        if (subscribe != null) subscribe.dispose();
    }

    //==============================================================================================
    // Rx Methods
    //==============================================================================================
    //region methods

    private Function<CharSequence, String> getRxConvert() {
        if (convert == null) {
            convert = new Function<CharSequence, String>() {
                @Override
                public String apply(CharSequence charSequence) throws Exception {
                    return charSequence.toString();
                }
            };
        }
        return convert;
    }

    private Function<String, ObservableSource<List<Address>>> getRxFinder() {
        if (findItem == null) {
            findItem = new Function<String, ObservableSource<List<Address>>>() {
                @Override
                public ObservableSource<List<Address>> apply(final String s) throws Exception {
                    if (loadingListener != null) loadingListener.startLoad();
                    return Observable
                            .create(new ObservableOnSubscribe<List<Address>>() {
                                @Override
                                public void subscribe(ObservableEmitter<List<Address>> e) throws Exception {
                                    try {
                                        e.onNext(geocoder.getFromLocationName(s, maxResult));
                                        e.onComplete();
                                    } catch (Exception ex) {
                                        e.onError(ex);
                                    }
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally(new Action() {
                                @Override
                                public void run() throws Exception {
                                    if (loadingListener != null) loadingListener.stopLoad();
                                }
                            });
                }
            };
        }
        return findItem;
    }

    private Predicate<String> getRxFilter() {
        if (filter == null) {
            filter = new Predicate<String>() {
                @Override
                public boolean test(String string) throws Exception {
                    if (string.equals(oldText)) {
                        return false;
                    }
                    oldText = string;
                    return !(TextUtils.isEmpty(string) || TextUtils.getTrimmedLength(string) == 0);
                }
            };
        }
        return filter;
    }

    private Consumer<List<Address>> getRxConsumer() {
        if (consumer == null) {
            consumer = new Consumer<List<Address>>() {
                @Override
                public void accept(List<Address> addresses) throws Exception {
                    if (addressAdapter == null) return;
                    addressAdapter.clear();
                    addressAdapter.addAll(addresses);
                }
            };
        }
        return consumer;
    }

    //endregion

}
