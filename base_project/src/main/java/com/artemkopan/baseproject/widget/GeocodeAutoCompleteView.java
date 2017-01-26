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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.artemkopan.baseproject.R;
import com.artemkopan.baseproject.utils.ExtraUtils;
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

public class GeocodeAutoCompleteView extends AppCompatAutoCompleteTextView {

    private static final int DEBOUNCE = 1000;
    private static final int MIN_START_SEARCH = 2;
    private static final int MAX_RESULT = 10;
    private Geocoder geocoder;
    private Predicate<CharSequence> filter;
    private Consumer<List<Address>> consumer;
    private PublishSubject<CharSequence> textChangeSubject;
    private Function<CharSequence, MaybeSource<List<Address>>> findItem;
    private AddressAdapter addressAdapter;
    private OnLoadingListener loadingListener;
    private int debounce = DEBOUNCE;
    private int minStartSearch = MIN_START_SEARCH;
    private int maxResult = MAX_RESULT;
    private boolean disabled;

    public GeocodeAutoCompleteView(Context context) {
        super(context);
        init(null);
    }

    public GeocodeAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GeocodeAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                disabled = true;
                setText(ExtraUtils.getCompleteAddressLine(addressAdapter.getItem(position)));
                disabled = false;
            }
        });
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() >= minStartSearch && textChangeSubject != null && !disabled) {
            textChangeSubject.onNext(text);
        } else if (addressAdapter != null) {
            addressAdapter.clear();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        textChangeSubject
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

    private Function<CharSequence, MaybeSource<List<Address>>> getRxFinder() {
        if (findItem == null) {
            findItem = new Function<CharSequence, MaybeSource<List<Address>>>() {
                @Override
                public MaybeSource<List<Address>> apply(final CharSequence sequence) throws Exception {
                    return Maybe.create(new MaybeOnSubscribe<List<Address>>() {
                        @Override
                        public void subscribe(MaybeEmitter<List<Address>> e) throws Exception {
                            if (loadingListener != null) loadingListener.startLoad();
                            e.onSuccess(geocoder.getFromLocationName(sequence.toString(), maxResult));
                            if (loadingListener != null) loadingListener.stopLoad();
                            e.onComplete();
                        }
                    });
                }
            };
        }
        return findItem;
    }

    private Predicate<CharSequence> getRxFilter() {
        if (filter == null) {
            filter = new Predicate<CharSequence>() {
                @Override
                public boolean test(CharSequence sequence) throws Exception {
                    return !StringUtils.isEmpty(sequence);
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
