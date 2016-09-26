package com.artemkopan.baseproject.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.artemkopan.baseproject.recycler.adapter.RecyclerBaseAdapter;
import com.artemkopan.baseproject.recycler.view.ExRecyclerView;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ExRecyclerView list = (ExRecyclerView) findViewById(R.id.list);

        list.showProgress();
    }
}
