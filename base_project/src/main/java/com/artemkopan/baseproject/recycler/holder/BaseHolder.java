package com.artemkopan.baseproject.recycler.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Artem Kopan for jabrool
 * 27.12.16
 */

public abstract class BaseHolder<M> extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(Context context, M item);

    public abstract void clear();

}
