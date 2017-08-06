package com.artemkopan.test.recycler;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artemkopan.recycler.adapter.RecyclerListAdapter;
import com.artemkopan.recycler.holder.SimpleHolder;
import com.artemkopan.test.R;
import com.artemkopan.utils.ViewUtils;

/**
 * Created by Artem Kopan for BaseProject
 * 06.08.2017
 */

public class TestAdapter extends RecyclerListAdapter<String, SimpleHolder<String>> {

    @Override
    public SimpleHolder<String> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new SimpleHolder<>(ViewUtils.inflateView(parent, R.layout.item_header));
        } else if (viewType == FOOTER) {
            return new SimpleHolder<>(ViewUtils.inflateView(parent, R.layout.item_footer));
        }
        final SimpleHolder<String> holder = new SimpleHolder<>(ViewUtils.inflateView(parent, android.R.layout.simple_list_item_1));
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callOnItemClick(holder, v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleHolder<String> holder, String model, int position) {
        ((TextView) holder.itemView).setText(model);
    }
}
