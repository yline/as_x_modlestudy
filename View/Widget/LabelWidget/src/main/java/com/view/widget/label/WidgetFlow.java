package com.view.widget.label;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.view.widget.label.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态添加 控件
 * 不支持点击
 *
 * @author yline 2017/4/28 -- 11:03
 * @version 1.0.0
 */
public class WidgetFlow
{
	private FlowLayout flowLayout;

	private List<String> dataList;

	private Context sContext;

	public WidgetFlow(Activity activity, @IdRes int widgetLayoutId)
	{
		this.sContext = activity;
		this.flowLayout = (FlowLayout) activity.findViewById(widgetLayoutId);
		this.dataList = new ArrayList<>();
	}

	public WidgetFlow(Context context, @NonNull FlowLayout flowLayout)
	{
		this.sContext = context;
		this.flowLayout = flowLayout;
		this.dataList = new ArrayList<>();
	}

	/**
	 * EQUIDISTANT：要求每一个内容相同，不然很难看
	 *
	 * @param gravity
	 */
	public void setLabelGravity(FlowLayout.LabelGravity gravity)
	{
		flowLayout.setLabelGravity(gravity);
	}

	public void setMaxCountEachLine(int maxCountEachLine)
	{
		flowLayout.setMaxCountEachLine(maxCountEachLine);
	}

	public int getDataSize()
	{
		return dataList.size();
	}

	public void setDataList(List<String> data)
	{
		this.dataList = new ArrayList<>(data);
		this.flowLayout.removeAllViews();
		for (int i = 0; i < dataList.size(); i++)
		{
			View itemView = LayoutInflater.from(sContext).inflate(getItemResourceId(), flowLayout, false);
			TextView textView = (TextView) itemView.findViewById(R.id.tv_widget_label_item);
			textView.setText(dataList.get(i));

			flowLayout.addView(itemView);
		}
	}

	public void addDataAll(List<String> data)
	{
		this.dataList.addAll(data);
		this.flowLayout.removeAllViews();
		for (int i = 0; i < dataList.size(); i++)
		{
			View itemView = LayoutInflater.from(sContext).inflate(getItemResourceId(), flowLayout, false);
			TextView textView = (TextView) itemView.findViewById(R.id.tv_widget_label_item);
			textView.setText(dataList.get(i));

			flowLayout.addView(itemView);
		}
	}

	public void addData(String string)
	{
		View itemView = LayoutInflater.from(sContext).inflate(getItemResourceId(), flowLayout, false);
		TextView textView = (TextView) itemView.findViewById(R.id.tv_widget_label_item);
		textView.setText(string);

		dataList.add(string);
		flowLayout.addView(itemView);
	}

	public void addData(int index, String string)
	{
		View itemView = LayoutInflater.from(sContext).inflate(getItemResourceId(), flowLayout, false);
		TextView textView = (TextView) itemView.findViewById(R.id.tv_widget_label_item);
		textView.setText(string);

		dataList.add(string);
		flowLayout.addView(itemView, index);
	}

	public void removeView(int index)
	{
		View itemView = flowLayout.getChildAt(index);

		dataList.remove(index);
		flowLayout.removeView(itemView);
	}

	public boolean removeView(String string)
	{
		int index = -1;
		for (int i = 0; i < dataList.size(); i++)
		{
			if (string.equalsIgnoreCase(dataList.get(i)))
			{
				index = i;
				break;
			}
		}

		if (-1 != index)
		{
			View itemView = flowLayout.getChildAt(index);

			dataList.remove(index);
			flowLayout.removeView(itemView);

			return true;
		}

		return false;
	}

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 提供重写的方法 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */
	protected int getItemResourceId()
	{
		return R.layout.label_item_flow;
	}
}
