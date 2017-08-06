package com.artemkopan.recycler.adapter;

import android.support.v7.widget.RecyclerView.Adapter;

public class HeaderAdapterDelegate {

    private boolean showHeader;

    public HeaderAdapterDelegate() {
    }

    public int getItemCount(int realCount) {
        return realCount + (showHeader ? 1 : 0);
    }

    public int getItemPosition(int position){
        return position - (showHeader ? 1 : 0);
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void showHeader(Adapter adapter, boolean show) {
        if (showHeader == show) return;
        showHeader = show;
        if (showHeader) {
            adapter.notifyItemInserted(0);
        } else {
            adapter.notifyItemRemoved(0);
        }
    }

    /**
     * @param position  -
     * @return -
     */
    public boolean isHeaderPosition(int position) {
        return showHeader && position == 0;
    }

}
