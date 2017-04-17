package com.artemkopan.base_recycler.diff;

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
        return (oldItem == null && newItem == null) || (oldItem != null && oldItem.equals(newItem));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }

    public boolean areContentsTheSame(T oldItem, T newItem) {
        return false;
    }

    public static class SimpleDiffCallback<T> extends BaseDiffCallback<T> {

        private SimpleDiffCallback(List<T> oldList, List<T> newList) {
            super(oldList, newList);
        }

        public static <T> SimpleDiffCallback<T> init(List<T> oldList, List<T> newList) {
            return new SimpleDiffCallback<>(oldList, newList);
        }
    }
}
