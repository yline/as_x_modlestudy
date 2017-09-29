package com.view.widget.label.widget;

import android.view.View;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 1，管理原始数据，也就是总数据
 * 2，管理选中数据，也就是选中的数据
 * 3，设置点击事件（单个Label被点击，多个Label总响应事件）
 * 4，全部做成数据后驱动型(即：数据在模型之后加载)
 *
 * @param <T> 数据类型
 */
public abstract class LabelAdapter<T>
{
	/* ------------------------------------ selected Data ------------------------------------- */
	private Deque<Integer> selectDeque;

	private int maxSelectNumber = Integer.MAX_VALUE;

	private int minSelectNumber = -1;

	/* ------------------------------------ total Data ------------------------------------- */
	private List<T> dataList;

	/* ------------------------------------ listener ------------------------------------- */
	private OnDataSetChangedListener dataChangedListener;

	private OnSelectedChangeListener selectedChangeListener;

	private OnLabelSelectListener labelSelectListener;

	private OnLabelClickListener labelClickListener;

	public LabelAdapter()
	{
		dataList = new ArrayList<>();
		selectDeque = new ArrayDeque<>();
	}

	public T getItem(int position)
	{
		return dataList.get(position);
	}

	public abstract View getView(FlowLayout container, T t, int position);

	public void addSelectedPosition(int position)
	{
		if (!selectDeque.contains(position))
		{
			selectDeque.add(position);
			notifySelectedChange();
		}
	}

	public void removeSelectedPosition(int position)
	{
		selectDeque.remove(position);
		notifySelectedChange();
	}

	public void addAllSelectedPosition(List<Integer> positionList)
	{
		selectDeque.addAll(positionList);
		notifySelectedChange();
	}

	public void clearSelectedPosition()
	{
		selectDeque.clear();
		notifySelectedChange();
	}

	/**
	 * 提供给 LabelFlowLayout使用的方法,不更新UI
	 *
	 * @param position
	 */
	void addInnerSelectedPosition(int position)
	{
		if (!selectDeque.contains(position))
		{
			selectDeque.add(position);
		}
	}

	/**
	 * 提供给 LabelFlowLayout使用的方法,不更新UI
	 *
	 * @param position
	 */
	public void removeInnerSelectedPosition(int position)
	{
		selectDeque.remove(position);
	}

	/**
	 * 选择项中，是否包含该项
	 *
	 * @param position
	 * @return
	 */
	public boolean isSelectedContain(int position)
	{
		return selectDeque.contains(position);
	}

	/**
	 * 不能给对象,太危险
	 *
	 * @return
	 */
	public Deque<Integer> getSelectedList()
	{
		return new ArrayDeque<>(this.selectDeque);
	}

	public int getSelectedFirst()
	{
		return selectDeque.getFirst();
	}

	public int getSelectedSize()
	{
		return null == selectDeque ? 0 : selectDeque.size();
	}

	public int getMaxSelectNumber()
	{
		return maxSelectNumber;
	}

	public void setMaxSelectNumber(int maxSelectNumber)
	{
		this.maxSelectNumber = maxSelectNumber;
	}

	public int getMinSelectNumber()
	{
		return minSelectNumber;
	}

	public void setMinSelectNumber(int minSelectNumber)
	{
		this.minSelectNumber = minSelectNumber;
	}

	public void setDataList(List<T> dataList)
	{
		this.dataList = new ArrayList<>(dataList);
		notifyDataSetChange();
	}

	public void addAllDataList(List<T> dataList)
	{
		this.dataList.addAll(dataList);
		notifyDataSetChange();
	}

	public void addData(T t)
	{
		this.dataList.add(t);
		notifyDataSetChange();
	}

	public boolean removeData(T t)
	{
		boolean result = this.dataList.remove(t);
		if (result)
		{
			notifyDataSetChange();
		}
		return result;
	}

	public void removeData(int index)
	{
		this.dataList.remove(index);
		notifyDataSetChange();
	}

	public int getDataSize()
	{
		return dataList.size();
	}

	void setOnDataChangedListener(OnDataSetChangedListener listener)
	{
		this.dataChangedListener = listener;
	}

	public void notifyDataSetChange()
	{
		if (null != dataChangedListener)
		{
			dataChangedListener.onDataChanged();
		}
	}

	void setOnSelectedChangeListener(OnSelectedChangeListener listener)
	{
		this.selectedChangeListener = listener;
	}

	public void notifySelectedChange()
	{
		if (null != selectedChangeListener)
		{
			selectedChangeListener.onSelectedChanged();
		}
	}

	public void setOnLabelSelectListener(OnLabelSelectListener labelSelectListener)
	{
		this.labelSelectListener = labelSelectListener;
	}

	public void callLabelSelected()
	{
		if (null != labelSelectListener)
		{
			labelSelectListener.onLabelSelected(this.selectDeque);
		}
	}

	public void setOnLabelClickListener(OnLabelClickListener labelClickListener)
	{
		this.labelClickListener = labelClickListener;
	}

	public void callLabelClick(FlowLayout container, View view, int position)
	{
		if (null != labelClickListener)
		{
			if (dataList.size() < position)
			{
				throw new IllegalArgumentException("callLabelClick happened int a error place");
			}
			labelClickListener.onLabelClick(container, view, dataList.get(position), position);
		}
	}

	public interface OnDataSetChangedListener
	{
		/**
		 * 监听数据改变
		 */
		void onDataChanged();
	}

	public interface OnSelectedChangeListener
	{
		/**
		 * 监听被选择的数据改变
		 */
		void onSelectedChanged();
	}

	public interface OnLabelSelectListener
	{
		void onLabelSelected(Deque<Integer> selectedDeque);
	}

	public interface OnLabelClickListener<T>
	{
		/**
		 * 单条 Label 被点击
		 *
		 * @param container 父类
		 * @param view      该类对象
		 * @param t         数据
		 * @param position  位置
		 * @return
		 */
		boolean onLabelClick(FlowLayout container, View view, T t, int position);
	}
}