package com.view.attach.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.yline.common.CommonRecyclerAdapter;
import com.yline.common.CommonRecyclerViewHolder;

/**
 * 添加头部和底部的Recycle
 *
 * @author yline 2017/3/2 --> 17:33
 * @version 1.0.0
 */
public abstract class HeadFootRecycleAdapter<T> extends CommonRecyclerAdapter<T>
{
	// sList的最大数目
	private static final int BASE_ITEM_TYPE_HEADER = 100000;

	// 200000 - 100000 是头部布局的最大个数
	private static final int BASE_ITEM_TYPE_FOOTER = 200000;

	// 头布局
	private SparseArrayCompat<View> headerViewArray = new SparseArrayCompat<>();

	// 底部布局
	private SparseArrayCompat<View> footViewArray = new SparseArrayCompat<>();

	/**
	 * 创建时会调用多次,依据viewType类型,创建ViewHolder
	 *
	 * @param parent
	 * @param viewType
	 * @return
	 */
	@Override
	public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		if (headerViewArray.get(viewType) != null)
		{
			return new CommonRecyclerViewHolder(headerViewArray.get(viewType));
		}
		else if (footViewArray.get(viewType) != null)
		{
			return new CommonRecyclerViewHolder(footViewArray.get(viewType));
		}

		return super.onCreateViewHolder(parent, viewType);
	}


	@Override
	public int getItemViewType(int position)
	{
		if (isHeaderViewPos(position))
		{
			return headerViewArray.keyAt(position);
		}
		else if (isFooterViewPos(position))
		{
			return footViewArray.keyAt(position - getHeadersCount() - sList.size());
		}
		return super.getItemViewType(position - getHeadersCount());
	}

	@Override
	public void onBindViewHolder(CommonRecyclerViewHolder holder, int position)
	{
		if (isHeaderViewPos(position))
		{
			return;
		}
		if (isFooterViewPos(position))
		{
			return;
		}
		super.onBindViewHolder(holder, position - getHeadersCount());
	}

	@Override
	public int getItemCount()
	{
		return getHeadersCount() + getFootersCount() + sList.size();
	}

	/**
	 * 适配 GridLayoutManager
	 */
	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView)
	{
		super.onAttachedToRecyclerView(recyclerView);

		RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

		if (layoutManager instanceof GridLayoutManager)
		{
			final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
			final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

			gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
			{
				@Override
				public int getSpanSize(int position)
				{
					int viewType = getItemViewType(position);

					if (headerViewArray.get(viewType) != null)
					{
						return gridLayoutManager.getSpanCount();
					}
					else if (footViewArray.get(viewType) != null)
					{
						return gridLayoutManager.getSpanCount();
					}

					if (spanSizeLookup != null)
					{
						return spanSizeLookup.getSpanSize(position);
					}

					return 0;
				}
			});

			gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
		}
	}

	// 适配 StaggeredGridLayoutManager
	@Override
	public void onViewAttachedToWindow(CommonRecyclerViewHolder holder)
	{
		super.onViewAttachedToWindow(holder);
		int position = holder.getLayoutPosition();
		if (isHeaderViewPos(position) || isFooterViewPos(position))
		{
			ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
			if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams)
			{
				StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) lp;

				params.setFullSpan(true);
			}
		}
	}

	private boolean isHeaderViewPos(int position)
	{
		return position < getHeadersCount();
	}

	private boolean isFooterViewPos(int position)
	{
		return position >= getHeadersCount() + sList.size();
	}

	public void addHeaderView(View view)
	{
		headerViewArray.put(headerViewArray.size() + BASE_ITEM_TYPE_HEADER, view);
	}

	public void addFootView(View view)
	{
		footViewArray.put(footViewArray.size() + BASE_ITEM_TYPE_FOOTER, view);
	}

	public int getHeadersCount()
	{
		return headerViewArray.size();
	}

	public int getFootersCount()
	{
		return footViewArray.size();
	}
}
