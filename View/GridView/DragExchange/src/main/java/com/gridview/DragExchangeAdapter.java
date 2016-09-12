package com.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gridview.bean.DataBean;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class DragExchangeAdapter extends BaseAdapter implements DragExchangeGridView.IDragListener, DragExchangeGridView.IItemClickListener
{
	private static final String TAG = "DragExchangeAdapter";

	private Context context;
	
	private List<DataBean> dataBeanList;

	private int endPosition = AdapterView.INVALID_POSITION;

	private DragExchangeAdapter.IItemClickListener iItemClickListener;

	public DragExchangeAdapter(Context context)
	{
		this.context = context;
		this.dataBeanList = new ArrayList<DataBean>();
	}

	/** 增加多条数据,并更新 */
	public void addAppData(List<DataBean> dataBeanList)
	{
		this.dataBeanList.addAll(dataBeanList);
		notifyDataSetChanged();
	}

	/** 增加单条数据,并更新 */
	public void addAppData(DataBean dataBean)
	{
		this.dataBeanList.add(dataBean);
		notifyDataSetChanged();
	}

	/** 指定位置,增加数据,并更新 */
	public void addAppData(int location, DataBean dataBean)
	{
		this.dataBeanList.add(location, dataBean);
		notifyDataSetChanged();
	}

	/** 移除显示的某项数据,并更新 */
	public void removeAppData(int location)
	{
		this.dataBeanList.remove(location);
		notifyDataSetChanged();
	}

	/** 移除显示的某项数据,并更新 */
	public void removeAppData(DataBean dataBean)
	{
		this.dataBeanList.remove(dataBean);
		notifyDataSetChanged();
	}

	/** 更新当前页面数据 */
	public void updateAppData(List<DataBean> dataBeanList)
	{
		this.dataBeanList = dataBeanList;
		notifyDataSetChanged();
	}

	/** 返回当前页面显示的数据 */
	public List<DataBean> getAppData()
	{
		return this.dataBeanList;
	}

	public void setOnAppClickListener(DragExchangeAdapter.IItemClickListener listener)
	{
		this.iItemClickListener = listener;
	}

	@Override
	public int getCount()
	{
		return dataBeanList.size();
	}

	@Override
	public Object getItem(int position)
	{
		if (dataBeanList.size() != 0)
		{
			return dataBeanList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.item_drag, parent, false);

		ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
		imageView.setImageDrawable(dataBeanList.get(position).getIcon());

		TextView textView = (TextView) view.findViewById(R.id.tv_name);
		textView.setText(dataBeanList.get(position).getName());

		if (endPosition == position)
		{
			view.setVisibility(View.INVISIBLE);
		}
		else
		{
			view.setVisibility(View.VISIBLE);
		}

		return view;
	}

	@Override
	public void onDragStart()
	{
		LogFileUtil.v(TAG, "IDragListener onDragStart");
	}

	@Override
	public void onExchange(int startPosition, int endPosition)
	{
		LogFileUtil.v(TAG, "IDragListener onExchange startPosition = " + startPosition + ",endPosition = " + endPosition);

		// 交换Item
		DataBean startBean = (DataBean) getItem(startPosition);
		DataBean endBean = (DataBean) getItem(endPosition);

		dataBeanList.set(startPosition, endBean);
		dataBeanList.set(endPosition, startBean);

		this.endPosition = endPosition;

		notifyDataSetChanged();
	}

	@Override
	public void onDragEnd(int endPosition)
	{
		LogFileUtil.v(TAG, "IDragListener onDragEnd, endPosition = " + endPosition);
		this.endPosition = AdapterView.INVALID_POSITION;
	}

	@Override
	public void onItemClick(View view, int position)
	{
		if (null != iItemClickListener)
		{
			iItemClickListener.onItemClick(view, dataBeanList.get(position));
		}

	}

	public interface IItemClickListener
	{
		/**
		 * 单个点击事件
		 *
		 * @param view     被点击的View
		 * @param dataBean 被点击的数据
		 */
		void onItemClick(View view, DataBean dataBean);
	}
}
