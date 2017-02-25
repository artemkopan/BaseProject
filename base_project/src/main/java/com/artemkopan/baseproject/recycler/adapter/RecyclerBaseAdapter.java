package com.artemkopan.baseproject.recycler.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.artemkopan.baseproject.recycler.listeners.OnItemClickListener;
import com.artemkopan.baseproject.utils.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class RecyclerBaseAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerAdapter<M, VH> {

    private static final String TAG = "RecyclerBaseAdapter";
    protected List<M> mList;

    public RecyclerBaseAdapter() {
    }

    public RecyclerBaseAdapter(List<M> list) {
        this.mList = list;
    }

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Nullable
    public M removeItem(Object model) {
        int index = mList.indexOf(model);
        if (index != -1) {
            return removeItem(index);
        } else {
            return null;
        }
    }

    public M removeItem(int position) {
        final M model = mList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(M model) {
        mList.add(model);
        notifyItemInserted(mList.size() - 1);
    }

    public void addItem(int position, M model) {
        mList.add(position, model);
        notifyItemInserted(position);
    }

    public void addItemRanged(Collection<M> collection) {
        if (collection == null) {
            Log.e(TAG, "Collection is empty");
            return;
        }
        int size = mList.size();
        mList.addAll(collection);
        notifyItemRangeInserted(size, mList.size() - size);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final M model = mList.remove(fromPosition);
        mList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    @Nullable
    public M getItemByPos(int position) {
        if (mList == null) {
            Log.e("getItemByPos: ", new NullPointerException("mList is null"));
            return null;
        } else if (position >= mList.size() || position < 0) {
            Log.e("getItemByPos: ", new ArrayIndexOutOfBoundsException(
                    "position " + position + " mList size " + mList.size()));
            return null;
        }

        return mList.get(position);
    }

    public List<M> getList() {
        return mList;
    }

    public void setList(List<M> list) {
        setList(list, true);
    }

    public void setList(List<M> list, boolean notify) {
        if (mList != null && mList.isEmpty()) {
            mList.clear();
        }
        this.mList = list;
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void setList(Collection<M> list, boolean notify) {
        if (mList == null) {
            mList = new ArrayList<>();
            mList.addAll(list);
        } else {
            mList.clear();
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void createList(boolean dropList) {
        if (mList != null && !dropList) return;
        setList(new ArrayList<M>());
    }

    /***
     * Find item in list;
     *
     * @param object object which has been found;
     * @return return object index in list; If item not found return -1;
     */
    public int getItemIndex(Object object) {
        return mList != null ? mList.indexOf(object) : -1;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

}
