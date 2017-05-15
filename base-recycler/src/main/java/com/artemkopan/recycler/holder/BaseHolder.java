package com.artemkopan.recycler.holder;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Artem Kopan for jabrool
 * 27.12.16
 */

public abstract class BaseHolder<M> extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public void bind(M item, int position) {
        bind(itemView.getContext(), item, position);
    }

    public abstract void bind(Context context, M item, int position);

    public abstract void clear();


}
