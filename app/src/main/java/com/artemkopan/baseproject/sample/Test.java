package com.artemkopan.baseproject.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.artemkopan.baseproject.dialog.DialogProvider;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.recycler.view.ExRecyclerView;

public class Test extends AppCompatActivity {

    DialogProvider mDialogProvider = new DialogProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ExRecyclerView list = (ExRecyclerView) findViewById(R.id.list);

        list.showProgress();

        mDialogProvider.showProgressDialog(this, new Runnable() {
            @Override
            public void run() {
                Log.d("run() called");
            }
        });

    }
}
