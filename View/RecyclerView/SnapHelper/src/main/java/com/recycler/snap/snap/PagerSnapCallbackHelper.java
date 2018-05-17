package com.recycler.snap.snap;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PagerSnapCallbackHelper {
    private OnItemSelectListener onItemSelectListener;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        private boolean mScrolled;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // 当 recyclerView已经滚动过了(mScrolled)，并且 已经停止滚动
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mScrolled) {
                // 停止滚动，获取滚动后的位置
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (null != layoutManager && layoutManager instanceof LinearLayoutManager) {
                    int firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

                    if (null != onItemSelectListener) {
                        int position = (firstVisiblePosition + lastVisiblePosition) / 2;
                        onItemSelectListener.onItemSelect(position);
                    }
                }

                mScrolled = false;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dx != 0 || dy != 0) {
                mScrolled = true;
            }
        }
    };

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(onScrollListener);
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public interface OnItemSelectListener {
        /**
         * 滚动到相应的item
         *
         * @param position 当前位置
         */
        void onItemSelect(int position);
    }
}
