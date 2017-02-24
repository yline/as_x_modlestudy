package com.layout.label.view.labellayout;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class LabelAdapter<T>
{
	private List<T> mTagDataList;

	private OnDataChangedListener mOnDataChangedListener;

	private HashSet<Integer> mCheckedPosList = new HashSet<Integer>();

	public LabelAdapter(List<T> data)
	{
		mTagDataList = data;
	}

	public LabelAdapter(T[] data)
	{
		mTagDataList = new ArrayList<>(Arrays.asList(data));
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
		return mTagDataList == null ? 0 : mTagDataList.size();
	}

	public void notifyDataChanged()
	{
		mOnDataChangedListener.onChanged();
	}

	public T getItem(int position)
	{
		return mTagDataList.get(position);
	}

	public abstract View getView(FlowLayout parent, int position, T t);

	public boolean setSelected(int position, T t)
	{
		return false;
	}


}