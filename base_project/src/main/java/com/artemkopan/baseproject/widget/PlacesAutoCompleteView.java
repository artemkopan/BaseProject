package com.artemkopan.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.ExtraUtils;
import com.artemkopan.baseproject.utils.Log;
import com.artemkopan.baseproject.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.MaybeSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Artem Kopan for jabrool
 * 26.01.17
 */

public class PlacesAutoCompleteView extends AppCompatAutoCompleteTextView {

    private static final int DEBOUNCE = 1000;
    private static final int MIN_START_SEARCH = 2;
    private static final int MAX_RESULT = 10;
    private Geocoder geocoder;
    private Predicate<String> filter;
    private Consumer<List<Address>> consumer;
    private Function<CharSequence, String> convert;
    private PublishSubject<CharSequence> textChangeSubject;
    private Function<String, MaybeSource<List<Address>>> findItem;
    private AddressAdapter addressAdapter;
    private OnLoadingListener loadingListener;
    private String oldValue;
    private int debounce = DEBOUNCE;
    private int minStartSearch = MIN_START_SEARCH;
    private int maxResult = MAX_RESULT;
    private boolean blocked;

    public PlacesAutoCompleteView(Context context) {
        super(context);
        init(null);
    }

    public PlacesAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PlacesAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        textChangeSubject = PublishSubject.create();
        addressAdapter = new AddressAdapter(getContext(), 0, new ArrayList<Address>());

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.GeocodeAutoCompleteView);

            try {
                debounce = ta.getInteger(R.styleable.GeocodeAutoCompleteView_gac_debounce, DEBOUNCE);
                minStartSearch = ta.getInteger(R.styleable.GeocodeAutoCompleteView_gac_min_start_search, MIN_START_SEARCH);
                maxResult = ta.getInteger(R.styleable.GeocodeAutoCompleteView_gac_max_result, MAX_RESULT);
            } finally {
                ta.recycle();
            }
        }

        VectorCompatViewHelper.loadFromAttributes(this, attrs);

        setAdapter(addressAdapter);
    }

    @Override
    protected void replaceText(CharSequence text) {
        blocked = true;
        super.replaceText(text);
        oldValue = text.toString();
        blocked = false;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() >= minStartSearch && textChangeSubject != null && !blocked) {
            Log.i("onNext " + text + " " + oldValue);
            textChangeSubject.onNext(StringUtils.trim(text));
        } else if (!blocked && addressAdapter != null) {
            Log.i("clear");
            addressAdapter.clear();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        textChangeSubject
                .map(getRxConvert())
                .filter(getRxFilter())
                .debounce(debounce, TimeUnit.MILLISECONDS, Schedulers.io())
                .flatMapMaybe(getRxFinder())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getRxConsumer());
    }

    @Override
    protected void onDetachedFromWindow() {
        textChangeSubject.onComplete();
        super.onDetachedFromWindow();
    }

    public void setLoadingListener(OnLoadingListener loadingListener) {
        this.loadingListener = loadingListener;
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

    private Function<String, MaybeSource<List<Address>>> getRxFinder() {
        if (findItem == null) {
            findItem = new Function<String, MaybeSource<List<Address>>>() {
                @Override
                public MaybeSource<List<Address>> apply(final String sequence) throws Exception {
                    return Maybe.create(new MaybeOnSubscribe<List<Address>>() {
                        @Override
                        public void subscribe(MaybeEmitter<List<Address>> e) throws Exception {
                            if (loadingListener != null) loadingListener.startLoad();
                            e.onSuccess(geocoder.getFromLocationName(sequence, maxResult));
                            if (loadingListener != null) loadingListener.stopLoad();
                            e.onComplete();
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
                    if (string.equals(oldValue)) {
                        showDropDown();
                        return false;
                    }
                    oldValue = string;
                    return !StringUtils.isEmpty(string);
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
                    addressAdapter.clear();
                    addressAdapter.addAll(addresses);
                }
            };
        }
        return consumer;
    }

    //endregion

    public interface OnLoadingListener {

        void startLoad();

        void stopLoad();
    }

    private static class AddressAdapter extends ArrayAdapter<Address> {

        private static final Filter NO_FILTER = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return super.convertResultToString(ExtraUtils.getCompleteAddressLine((Address) resultValue));
            }
        };

        AddressAdapter(Context context, int resource, List<Address> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            return createView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return createView(position, convertView, parent);
        }

        private View createView(int position, View convertView, ViewGroup parent) {
            TextView view;
            if (convertView == null) {
                view = (TextView) LayoutInflater.from(parent.getContext())
                                                .inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            } else {
                view = (TextView) convertView;
            }

            view.setText(ExtraUtils.getCompleteAddressLine(getItem(position)));

            return view;
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return NO_FILTER;
        }
    }
}
