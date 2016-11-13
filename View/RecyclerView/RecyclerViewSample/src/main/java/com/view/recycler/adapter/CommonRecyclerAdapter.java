package com.view.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yline on 2016/11/14.
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> implements ICommonAdapterCallback<T>
{
	protected List<T> sList;

	public CommonRecyclerAdapter()
	{
		this.sList = new ArrayList<>();
	}

	@Override
	public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new CommonRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemRes(), parent, false));
	}

	/**
	 * @return item 资源文件
	 */
	public abstract int getItemRes();

	/**
	 * 对内容设置
	 * @param holder
	 * @param position 当前item位置
	 */
	protected abstract void setViewContent(final CommonRecyclerViewHolder holder, final int position);

	@Override
	public void onBindViewHolder(final CommonRecyclerViewHolder holder, final int position)
	{
		if (null != onClickListener)
		{
			holder.itemView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					onClickListener.onClick(v, position);
				}
			});
		}

		if (null != onLongClickListener)
		{
			holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					return onLongClickListener.onClick(v, position);
				}
			});
		}

		if (null != onTouchListener)
		{
			holder.itemView.setOnTouchListener(new View.OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					return onTouchListener.onClick(v, position);
				}
			});
		}

		setViewContent(holder, position);
	}

	private OnClickListener onClickListener;

	public void setOnClickListener(OnClickListener listener)
	{
		this.onClickListener = listener;
	}

	public interface OnClickListener
	{
		void onClick(View v, int position);
	}

	private OnLongClickListener onLongClickListener;

	public void setOnLongClickListener(OnLongClickListener listener)
	{
		this.onLongClickListener = listener;
	}

	public interface OnLongClickListener
	{
		boolean onClick(View v, int position);
	}

	private OnTouchListener onTouchListener;

	public void setOnTouchListener(OnTouchListener listener)
	{
		this.onTouchListener = listener;
	}

	public interface OnTouchListener
	{
		boolean onClick(View v, int position);
	}

	@Override
	public int getItemCount()
	{
		return sList.size();
	}

	public T getItemData(int position)
	{
		if (position >= sList.size())
		{
			throw new IllegalArgumentException("invalid position");
		}
		return sList.get(position);
	}

	@Override
	public boolean add(T object)
	{
		boolean result = sList.add(object);
		this.notifyDataSetChanged();
		return result;
	}

	@Override
	public void add(int index, T element)
	{
		sList.add(index, element);
		this.notifyItemInserted(index);
	}

	@Override
	public boolean addAll(Collection collection)
	{
		boolean result = sList.addAll(collection);
		this.notifyDataSetChanged();
		return result;
	}

	@Override
	public void clear()
	{
		sList.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public boolean contains(Object object)
	{
		boolean result = sList.contains(object);
		return result;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public boolean remove(Object object)
	{
		boolean result = sList.remove(object);
		this.notifyDataSetChanged();
		return result;
	}

	@Override
	public T remove(int index)
	{
		T t = sList.remove(index);
		this.notifyItemRemoved(index);
		return t;
	}

	@Override
	public int size()
	{
		int size = sList.size();
		return size;
	}

	@Override
	public boolean removeAll(Collection collection)
	{
		boolean result = sList.removeAll(collection);
		this.notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean containsAll(Collection collection)
	{
		boolean result = sList.containsAll(collection);
		return result;
	}
}
