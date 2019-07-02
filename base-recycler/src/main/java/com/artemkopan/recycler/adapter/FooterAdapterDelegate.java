package com.artemkopan.recycler.adapter;

import androidx.recyclerview.widget.RecyclerView.Adapter;


public class FooterAdapterDelegate {

    private boolean showFooter;

    public FooterAdapterDelegate() {
    }

    public int getItemCount(int realCount) {
        return realCount + (showFooter ? 1 : 0);
    }

    public boolean isShowFooter() {
        return showFooter;
    }

    public void showFooter(Adapter adapter, boolean show) {
        if (showFooter == show) return;
        showFooter = show;
        if (showFooter) {
            adapter.notifyItemInserted(adapter.getItemCount()+1);
        } else {
            adapter.notifyItemRemoved(adapter.getItemCount() + 1);
        }
    }

    /**
     * @param position  -
     * @param realCount Call super.{@link Adapter#getItemCount()} in your adapter
     * @return -
     */
    public boolean isFooterPosition(int position, int realCount) {
        return showFooter && (realCount == 0 || position == realCount);
    }

}
