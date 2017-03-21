package com.layout.label.view.labellayout;

import android.view.View;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class LabelAdapter<T>
{
	protected List<T> slist;

	private OnDataChangedListener mOnDataChangedListener;

	private HashSet<Integer> mCheckedPosList = new HashSet<Integer>();

	public LabelAdapter(List<T> data)
	{
		slist = data;
	}

	interface OnDataChangedListener
	{
		void onChanged();
	}

	void setOnDataChangedListener(OnDataChangedListener listener)
	{
		mOnDataChangedListener = listener;
	}

	public void setSelectedList(int... poses)
	{
		Set<Integer> set = new HashSet<>();
		for (int pos : poses)
		{
			set.add(pos);
		}
		setSelectedList(set);
	}

	public void setSelectedList(Set<Integer> set)
	{
		mCheckedPosList.clear();
		if (set != null)
		{
			mCheckedPosList.addAll(set);
		}
		notifyDataChanged();
	}

	HashSet<Integer> getPreCheckedList()
	{
		return mCheckedPosList;
	}

	public int getCount()
	{
		return slist == null ? 0 : slist.size();
	}

	public void notifyDataChanged()
	{
		mOnDataChangedListener.onChanged();
	}

	public T getItem(int position)
	{
		return slist.get(position);
	}

	public abstract View getView(FlowLayout parent, int position, T t);

	public boolean setSelected(int position, T t)
	{
		return false;
	}
}