package com.artemkopan.recycler.adapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class RecyclerListAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerBaseAdapter<M, VH>
        implements RecyclerList<M> {

    protected List<M> list;

    public RecyclerListAdapter() {
        this(new ArrayList<M>());
    }

    public RecyclerListAdapter(List<M> list) {
        this.list = list;
    }

    @Override
    protected M getListItemByPos(int pos) {
        return get(pos);
    }

    @Override
    protected int getListSize() {
        return listSize();
    }

    public void setList(List<M> list) {
        setList(list, true);
    }

    public void setList(List<M> list, boolean notify) {
        if (this.list != null && this.list.isEmpty()) {
            this.list.clear();
        }
        this.list = list;
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void setList(Collection<M> list, boolean notify) {
        if (this.list == null) {
            this.list = new ArrayList<>();
            this.list.addAll(list);
        } else {
            this.list.clear();
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public List<M> getList() {
        return list;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final M model = list.remove(fromPosition);
        list.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItem(M item) {
        if (list.add(item)) {
            notifyItemChanged(listSize() - 1);
        }
    }

    @Override
    public void addItem(Collection<M> items) {
        final int prevSize = listSize();
        if (list.addAll(items)) {
            notifyItemRangeChanged(prevSize, listSize());
        }
    }

    @Override
    @SafeVarargs
    public final void addItem(M... items) {
        addItem(Arrays.asList(items));
    }

    @Override
    public boolean remove(M item) {
        int index = list.indexOf(item);
        if (index != -1) {
            list.remove(index);
            notifyItemRemoved(index);
            return true;
        }
        return false;
    }

    @Override
    public M removeAt(int index) {
        notifyItemRemoved(index);
        return list.remove(index);
    }

    @Override
    public void updateAt(int index, M item) {
        list.set(index, item);
        notifyItemChanged(index);
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
}
