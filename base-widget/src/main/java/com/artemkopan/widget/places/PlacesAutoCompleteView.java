package com.artemkopan.widget.places;

import android.content.Context;
import android.content.res.TypedArray;
import android.location.Address;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.artemkopan.widget.R;
import com.artemkopan.widget.vector.VectorCompatViewHelper;

/**
 * Created by Artem Kopan for jabrool
 * 26.01.17
 */

public class PlacesAutoCompleteView extends AppCompatAutoCompleteTextView {

    private PlacesAutoCompleteDelegate delegate;

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
        delegate = new PlacesAutoCompleteDelegate(getContext());
        boolean initAdapter = true;

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PlacesAutoCompleteView);

            try {
                delegate.setDebounce(ta.getInteger(R.styleable.PlacesAutoCompleteView_pac_debounce, PlacesAutoCompleteDelegate.DEBOUNCE));
                delegate.setMinStartSearch(ta.getInteger(R.styleable.PlacesAutoCompleteView_pac_min_start_search, PlacesAutoCompleteDelegate.MIN_START_SEARCH));
                delegate.setMaxResult(ta.getInteger(R.styleable.PlacesAutoCompleteView_pac_max_result, PlacesAutoCompleteDelegate.MAX_RESULT));
                delegate.setLayoutRes(ta.getResourceId(R.styleable.PlacesAutoCompleteView_pac_item_layout, android.R.layout.simple_dropdown_item_1line));
                initAdapter = ta.getBoolean(R.styleable.PlacesAutoCompleteView_pac_init_adapter, true);
            } finally {
                ta.recycle();
            }
        }

        VectorCompatViewHelper.loadFromAttributes(this, attrs);

        if (initAdapter) {
            super.setAdapter(delegate.initAdapter(getContext()));
        }
    }

    public void setDebounce(int debounce) {
        delegate.setDebounce(debounce);
    }

    public void setMinStartSearch(int minStartSearch) {
        delegate.setMinStartSearch(minStartSearch);
    }

    public void setMaxResult(int maxResult) {
        delegate.setMaxResult(maxResult);
    }

    public void setText(CharSequence text, boolean blocked) {
        if (blocked) replaceText(text);
        else setText(text);
    }

    public void setLoadingListener(OnLocationListener loadingListener) {
        delegate.setLoadingListener(loadingListener);
    }

    /**
     * Sel adapter item layout. By default use {@link android.R.layout#simple_dropdown_item_1line}
     *
     * @param layoutRes - Layout Id
     */
    public void setLayoutRes(@LayoutRes int layoutRes) {
        delegate.setLayoutRes(layoutRes);
    }

    @Override
    protected void replaceText(CharSequence text) {
        delegate.setBlocked(true);
        super.replaceText(text);
        delegate.setOldText(text.toString());
        delegate.setBlocked(false);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (delegate != null) delegate.onTextChanged(text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        delegate.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        delegate.onDetachedFromWindow();
    }

    public void setAdapter(ArrayAdapter<Address> adapter) {
        super.setAdapter(adapter);
        delegate.setAdapter(adapter);
    }

    //==============================================================================================
    // Static methods
    //==============================================================================================
    //region methods

    //endregion

    public interface OnLocationListener {

        void startLoad();

        void stopLoad();

        void error(Throwable throwable);
    }

}
