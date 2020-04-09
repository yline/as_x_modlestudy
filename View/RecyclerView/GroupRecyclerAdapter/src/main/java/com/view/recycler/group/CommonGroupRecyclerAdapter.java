package com.view.recycler.group;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonGroupRecyclerAdapter<Head, Item, Foot> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private static final int TYPE_SECTION_HEADER = -1;
    private static final int TYPE_SECTION_FOOTER = -2;

    private static final int TYPE_ITEM = -3;

    private int[] positionOfGroups = null; // 依据 当前位置, 记录对应的group位置
    private int[] positionInGroups = null; // 依据 当前位置，记录对应的group中的位置

    private boolean[] isHeads = null; // 依据 当前位置，记录当前是否是 Head
    private boolean[] isFoots = null; // 依据 当前位置，记录当前是否是 Foot

    protected List<Head> headList = new ArrayList<>(); // 头部数据
    protected List<List<Item>> itemDoubleList = new ArrayList<>(); // 内容数据
    protected List<Foot> footList = new ArrayList<>(); // 底部数据

    public CommonGroupRecyclerAdapter() {
        super();
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setupIndices();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupIndices();

        // 适配 GridLayoutManager
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);

                    if (isHeadViewType(viewType) || isFootViewType(viewType)) {
                        return gridLayoutManager.getSpanCount();
                    }

                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }

                    return 0;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        // 适配 StaggeredGridLayoutManager
        int position = holder.getLayoutPosition();
        if (isHeadPosition(position) || isFootPosition(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) lp;

                params.setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeadPosition(position)) {
            return TYPE_SECTION_HEADER;
        } else if (isFootPosition(position)) {
            return TYPE_SECTION_FOOTER;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeadViewType(viewType)) {
            return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getHeadRes(), parent, false));
        } else if (isFootViewType(viewType)) {
            return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getFootRes(), parent, false));
        } else {
            return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemRes(), parent, false));
        }
    }

    /**
     * 判断 是否是 头部的View
     *
     * @param viewType
     * @return
     */
    private boolean isHeadViewType(int viewType) {
        return viewType == TYPE_SECTION_HEADER;
    }

    /**
     * 判断 是否是 底部的view
     *
     * @param viewType
     * @return
     */
    private boolean isFootViewType(int viewType) {
        return viewType == TYPE_SECTION_FOOTER;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        int positionOfGroup = positionOfGroups[position];

        if (isHeadPosition(position)) {
            onBindViewHolderHead(holder, headList, positionOfGroup);
        } else if (isFootPosition(position)) {
            onBindViewHolderFoot(holder, footList, positionOfGroup);
        } else {
            int positionInGroup = positionInGroups[position];
            onBindViewHolderItem(holder, itemDoubleList, positionOfGroup, positionInGroup);
        }
    }

    /**
     * 判断 是否是 头部的位置
     *
     * @param position
     * @return
     */
    public boolean isHeadPosition(int position) {
        if (null == isHeads) {
            setupIndices();
        }
        return isHeads[position];
    }

    public boolean isFootPosition(int position) {
        if (null == isFoots) {
            setupIndices();
        }
        return isFoots[position];
    }

    @Override
    public int getItemCount() {
        int count = 0;
        int groups = itemDoubleList.size();
        int foot;

        for (int i = 0; i < groups; i++) {
            foot = hasFootInGroup(i) ? 1 : 0;
            count += (1 + foot + getGroupItemCount(i));
        }

        return count;
    }

    /**
     * 初始化数据
     */
    private void setupIndices() {
        int count = getItemCount();

        isHeads = new boolean[count];
        isFoots = new boolean[count];
        positionOfGroups = new int[count];
        positionInGroups = new int[count];

        setupComputed();
    }

    private void setupComputed() {
        int groups = itemDoubleList.size();
        int index = 0;

        // groups 一次设置一组
        for (int i = 0; i < groups; i++) {
            setPrecomputedItem(index, true, false, i, 0);
            index++;

            for (int j = 0; j < getGroupItemCount(i); j++) {
                setPrecomputedItem(index, false, false, i, j);
                index++;
            }

            if (hasFootInGroup(i)) {
                setPrecomputedItem(index, false, true, i, 0);
                index++;
            }
        }
    }

    /**
     * 设置 数据初始化 情况
     *
     * @param position        整个的位置
     * @param isHeader        是否是头部
     * @param isFooter        是否是底部
     * @param positionOfGroup group 的位置
     * @param positionInGroup group 中 item 的位置
     */
    private void setPrecomputedItem(int position, boolean isHeader, boolean isFooter, int positionOfGroup, int positionInGroup) {
        this.isHeads[position] = isHeader;
        this.isFoots[position] = isFooter;
        this.positionOfGroups[position] = positionOfGroup;
        this.positionInGroups[position] = positionInGroup;
    }

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 提供重写的方法 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */

    /**
     * 返回当前 item 显示的个数
     *
     * @param positionOfGroup group的位置
     * @return
     */
    protected int getGroupItemCount(int positionOfGroup) {
        return itemDoubleList.get(positionOfGroup).size();
    }

    /**
     * 头部，布局文件
     *
     * @return
     */
    protected abstract int getHeadRes();

    /**
     * 头部，绑定内容
     *
     * @param viewHolder
     * @param headList
     * @param positionOfGroup
     */
    protected abstract void onBindViewHolderHead(RecyclerViewHolder viewHolder, List<Head> headList, int positionOfGroup);

    /**
     * 内容，布局文件
     *
     * @return
     */
    protected abstract int getItemRes();

    /**
     * 内容，绑定内容
     *
     * @param viewHolder
     * @param itemDoubleList
     * @param positionOfGroup
     * @param positionInGroup
     */
    protected abstract void onBindViewHolderItem(RecyclerViewHolder viewHolder, List<List<Item>> itemDoubleList, int positionOfGroup, int positionInGroup);

    /**
     * Group中是否有底部
     *
     * @param positionOfGroup
     * @return
     */
    protected boolean hasFootInGroup(int positionOfGroup) {
        return false;
    }

    protected int getFootRes() {
        return 0;
    }

    protected void onBindViewHolderFoot(RecyclerViewHolder viewHolder, List<Foot> footList, int positionOfGroup) {

    }
}
