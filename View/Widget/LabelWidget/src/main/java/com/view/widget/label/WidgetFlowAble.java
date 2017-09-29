package com.view.widget.label;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.view.widget.label.widget.FlowLayout;
import com.view.widget.label.widget.LabelAdapter;
import com.view.widget.label.widget.LabelFlowLayout;

import java.util.Deque;
import java.util.List;

/**
 * 流动添加控件，
 *
 * @author yline 2017/4/28 -- 11:06
 * @version 1.0.0
 */
public class WidgetFlowAble
{
	private Context sContext;

	private LabelFlowLayout labelFlowLayout;

	private FlowAbleAdapter flowAbleAdapter;

	public WidgetFlowAble(Activity activity, @IdRes int widgetLayoutId)
	{
		this.sContext = activity;
		this.labelFlowLayout = (LabelFlowLayout) activity.findViewById(widgetLayoutId);
		this.flowAbleAdapter = new FlowAbleAdapter();

		this.labelFlowLayout.setAdapter(flowAbleAdapter);
	}

	public WidgetFlowAble(Context context, @NonNull LabelFlowLayout flowLayout)
	{
		this.sContext = context;
		this.labelFlowLayout = flowLayout;
		this.flowAbleAdapter = new FlowAbleAdapter();

		this.labelFlowLayout.setAdapter(flowAbleAdapter);
	}

	public int getDataSize()
	{
		return flowAbleAdapter.getDataSize();
	}

	public int getSelectedSize()
	{
		return flowAbleAdapter.getSelectedSize();
	}

	public Deque<Integer> getSelectedList()
	{
		return flowAbleAdapter.getSelectedList();
	}

	public int getSelectedFirst()
	{
		return flowAbleAdapter.getSelectedFirst();
	}

	public int getSelectedMaxCount()
	{
		return flowAbleAdapter.getMaxSelectNumber();
	}

	public int getSelectedMinCount()
	{
		return flowAbleAdapter.getMinSelectNumber();
	}

	/**
	 * EQUIDISTANT：要求每一个内容相同，不然很难看
	 *
	 * @param gravity
	 */
	public void setLabelGravity(FlowLayout.LabelGravity gravity)
	{
		labelFlowLayout.setLabelGravity(gravity);
	}

	public void setClickable(boolean clickable)
	{
		labelFlowLayout.setClickable(clickable);
	}

	public void setMaxCountEachLine(int maxCountEachLine)
	{
		labelFlowLayout.setMaxCountEachLine(maxCountEachLine);
	}

	public void setMaxSelectCount(int maxCount)
	{
		flowAbleAdapter.setMaxSelectNumber(maxCount);
	}

	public void setMinSelectCount(int minCount)
	{
		flowAbleAdapter.setMinSelectNumber(minCount);
	}

	public void toggleSpecialState(int currentPosition, int specialPosition)
	{
		toggleSpecialState(currentPosition == specialPosition, specialPosition);
	}

	public void toggleSpecialState(boolean isSpecialClicked, int specialPosition)
	{
		boolean specialSelected = flowAbleAdapter.isSelectedContain(specialPosition);
		if (isSpecialClicked)
		{
			if (specialSelected)
			{
				int length = flowAbleAdapter.getDataSize();
				for (int i = 0; i < length; i++)
				{
					if (i != specialPosition)
					{
						flowAbleAdapter.removeInnerSelectedPosition(i);
					}
				}

				flowAbleAdapter.notifySelectedChange();
			}
		}
		else
		{
			if (specialSelected)
			{
				flowAbleAdapter.removeSelectedPosition(specialPosition);
			}
		}
	}

	public void setDataList(List<String> data)
	{
		flowAbleAdapter.setDataList(data);
	}

	public void addAllDataList(List<String> data)
	{
		flowAbleAdapter.addAllDataList(data);
	}

	public void addData(String str)
	{
		flowAbleAdapter.addData(str);
	}

	public void removeData(int index)
	{
		flowAbleAdapter.removeData(index);
	}

	public void removeData(String str)
	{
		flowAbleAdapter.removeData(str);
	}

	public void setOnLabelSelectListener(LabelAdapter.OnLabelSelectListener labelSelectListener)
	{
		flowAbleAdapter.setOnLabelSelectListener(labelSelectListener);
	}

	public void setOnLabelClickListener(LabelAdapter.OnLabelClickListener labelClickListener)
	{
		flowAbleAdapter.setOnLabelClickListener(labelClickListener);
	}

	public void addSelectedPosition(int position)
	{
		flowAbleAdapter.addSelectedPosition(position);
	}

	public void addAllSelectedPosition(List<Integer> positionList)
	{
		flowAbleAdapter.addAllSelectedPosition(positionList);
	}

	public void removeSelectedPosition(int position)
	{
		flowAbleAdapter.removeSelectedPosition(position);
	}

	public void clearSelectedPosition()
	{
		flowAbleAdapter.clearSelectedPosition();
	}

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 提供重写的方法 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */
	protected int getItemResourceId()
	{
		return R.layout.label_item_flow_able;
	}

	private class FlowAbleAdapter extends LabelAdapter<String>
	{
		@Override
		public View getView(FlowLayout container, String s, int position)
		{
			LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(sContext).inflate(getItemResourceId(), labelFlowLayout, false);

			TextView tvItem = (TextView) linearLayout.findViewById(R.id.tv_label);
			tvItem.setText(s);

			return linearLayout;
		}
	}
}
