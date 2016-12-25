package com.artemkopan.baseproject.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by Artem Kopan for jabrool
 * 05.12.16
 */
public abstract class BaseDiffCallback<T> extends DiffUtil.Callback {

    private List<T> oldList;
    private List<T> newList;

    public BaseDiffCallback(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    protected T getOldItem(int pos) {
        return oldList.get(pos);
    }

    protected T getNewItem(int pos) {
        return newList.get(pos);
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }

    public boolean areItemsTheSame(T oldItem, T newItem) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }

    public boolean areContentsTheSame(T oldItem, T newItem) {
        return false;
    }
}
