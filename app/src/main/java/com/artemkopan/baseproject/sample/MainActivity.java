package com.artemkopan.baseproject.sample;

import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

import com.artemkopan.baseproject.widget.PlacesAutoCompleteView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private PlacesAutoCompleteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new PlacesAutoCompleteAdapter(this);

        Address address = new Address(Locale.CANADA);
        address.setAddressLine(0, "Test test test");
        adapter.add(address);
        adapter.add(address);
        adapter.add(address);
        adapter.add(address);
        adapter.add(address);
        adapter.add(address);
        adapter.add(address);

        final PlacesAutoCompleteView placesAutoCompleteView = (PlacesAutoCompleteView) findViewById(R.id.autoComplete);

        placesAutoCompleteView.setResetResult(false);
        placesAutoCompleteView.setIgnoreThreshold(true);

        placesAutoCompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    placesAutoCompleteView.setAdapter(adapter);
                    placesAutoCompleteView.showDropDown();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
