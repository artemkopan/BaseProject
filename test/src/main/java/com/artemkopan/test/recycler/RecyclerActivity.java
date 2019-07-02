package com.artemkopan.test.recycler;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.artemkopan.mvp.activity.BaseActivity;
import com.artemkopan.recycler.listeners.OnItemClickListener;
import com.artemkopan.recycler.view.ExRecyclerView;
import com.artemkopan.test.R;

public class RecyclerActivity extends BaseActivity {

    private TestAdapter adapter = new TestAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter.addItem(getResources().getStringArray(R.array.test_items));
        adapter.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClickListener(View view, int pos, String item, View... transactionViews) {
                Toast.makeText(view.getContext(), "Click " + pos, Toast.LENGTH_SHORT).show();
            }
        });
        ExRecyclerView view = (ExRecyclerView) findViewById(R.id.list);

        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);

    }

    @Override
    public int onInflateLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    public void showProgress(@Nullable Object tag) {
        super.showProgress(tag);
    }

    @Override
    public void hideProgress(@Nullable Object tag) {
        super.hideProgress(tag);
    }

    @Override
    public void showError(@Nullable Object tag, String error) {
        super.showError(tag, error);
    }

    public void toggleFooter(View view) {
        adapter.showFooter(!adapter.isShowFooter());
    }

    public void toggleHeader(View view) {
        adapter.showHeader(!adapter.isShowHeader());
    }

    public void showTex(View view) {
        ((ExRecyclerView) findViewById(R.id.list)).showText("TEST");
    }
}
