package com.artemkopan.baseproject.recycler.listeners;

import android.view.View;

public interface OnItemClickListener<M> {
    void onItemClickListener(View view, int pos, M object, View... transactionViews);
}
