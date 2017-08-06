package com.artemkopan.recycler.listeners;

import android.view.View;

public interface OnItemClickListener<M> {
    void onItemClickListener(View view, int pos, M item, View... transactionViews);
}
