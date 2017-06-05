package com.artemkopan.recycler.adapter;

import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.util.SortedList.Callback;
import android.support.v7.widget.RecyclerView;

import java.util.Collection;

/**
 * Created by Artem Kopan for jabrool
 * 21.02.17
 */

public abstract class RecyclerSortedAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerBaseAdapter<M, VH>
        implements RecyclerList<M> {

    private SortedList<M> list;

    public void initList(Class<M> clazz, Callback<M> callback) {
        list = new SortedList<>(clazz, callback);
    }

    public SortedList<M> getList() {
        if (list == null) throw new NullPointerException("List is null. Please call iniList() method");
        return list;
    }

    @Override
    @Nullable
    protected M getListItemByPos(int position) {
        return get(position);
    }

    @Override
    protected int getListSize() {
        return listSize();
    }

    @Override
    public void addItem(M item) {
        list.add(item);
    }

    @Override
    public void addItem(Collection<M> items) {
        list.addAll(items);
    }

    @Override
    @SafeVarargs
    public final void addItem(M... items) {
        list.addAll(items);
    }

    @Override
    public M removeAt(int index) {
        return list.removeItemAt(index);
    }

    @Override
    public boolean remove(M item) {
        return list.remove(item);
    }

    @Override
    public void updateAt(int index, M item) {
        list.updateItemAt(index, item);
    }

    @Override
    public M get(int index) {
        if (index >= list.size() || index < 0) return null;
        return list.get(index);
    }

    @Override
    public int indexOf(M item) {
        return list.indexOf(item);
    }

    @Override
    public int listSize() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }
}
