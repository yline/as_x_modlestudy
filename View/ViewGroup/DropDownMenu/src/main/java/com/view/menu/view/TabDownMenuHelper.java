package com.view.menu.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.view.menu.R;
import com.yline.base.common.CommonListAdapter;
import com.yline.base.common.ViewHolder;

import java.util.Arrays;

/**
 * 这个的可定制性，很强
 *
 * @author yline 2017/3/6 --> 14:57
 * @version 1.0.0
 */
public class TabDownMenuHelper
{
	private CityListAdapter cityListAdapter, ageListAdapter, sexListAdapter, constellationListAdapter;

	private OnDropMenuClickListener listener;

	public View initCityView(Context context)
	{
		final ListView cityView = new ListView(context)
		{
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
			{
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(500, MeasureSpec.AT_MOST);
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		};
		cityView.setDividerHeight(0);
		cityListAdapter = new CityListAdapter(context);
		cityView.setAdapter(cityListAdapter);
		cityView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				cityListAdapter.setCheckItem(position);
				if (null != listener)
				{
					listener.onCityClick(position);
				}
			}
		});

		return cityView;
	}

	public View initAgeView(Context context)
	{
		final ListView ageView = new ListView(context)
		{
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
			{
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(500, MeasureSpec.AT_MOST);
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		};
		ageView.setDividerHeight(0);
		ageListAdapter = new CityListAdapter(context);
		ageView.setAdapter(ageListAdapter);
		ageView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				ageListAdapter.setCheckItem(position);
				if (null != listener)
				{
					listener.onAgeClick(position);
				}
			}
		});

		return ageView;
	}

	public View initSexView(Context context)
	{
		final ListView sexView = new ListView(context)
		{
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
			{
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(500, MeasureSpec.AT_MOST);
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		};
		sexView.setDividerHeight(0);
		sexListAdapter = new CityListAdapter(context);
		sexView.setAdapter(sexListAdapter);
		sexView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				sexListAdapter.setCheckItem(position);
				if (null != listener)
				{
					listener.onSexClick(position);
				}
			}
		});

		return sexView;
	}

	public View initConstellationView(Context context)
	{
		View parentView = LayoutInflater.from(context).inflate(R.layout.activity_main_menu, null);

		GridView gridView = (GridView) parentView.findViewById(R.id.grid_view_main_constellation);
		constellationListAdapter = new CityListAdapter(context);
		gridView.setAdapter(constellationListAdapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				constellationListAdapter.setCheckItem(position);
				if (null != listener)
				{
					listener.onConstellationClick(position);
				}
			}
		});

		return parentView;
	}

	public void setOnDropMenuClickListener(OnDropMenuClickListener listener)
	{
		this.listener = listener;
	}

	public void setCityData(String... data)
	{
		cityListAdapter.addAll(Arrays.asList(data));
	}

	public void setAgeData(String... data)
	{
		ageListAdapter.addAll(Arrays.asList(data));
	}

	public void setSexData(String... data)
	{
		sexListAdapter.addAll(Arrays.asList(data));
	}

	public void setConstellationData(String... data)
	{
		constellationListAdapter.addAll(Arrays.asList(data));
	}

	private class CityListAdapter extends CommonListAdapter<String>
	{
		private int checkItemPosition = 0;

		public CityListAdapter(Context context)
		{
			super(context);
		}

		public void setCheckItem(int position)
		{
			checkItemPosition = position;
			notifyDataSetChanged();
		}

		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_main_city;
		}

		@Override
		protected void setViewContent(int position, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			TextView textView = viewHolder.get(R.id.tv_item_main);
			textView.setText(sList.get(position));
			if (checkItemPosition != -1)
			{
				if (checkItemPosition == position)
				{
					textView.setTextColor(sContext.getResources().getColor(android.R.color.holo_red_light));
					textView.setCompoundDrawablesWithIntrinsicBounds(null, null, sContext.getResources().getDrawable(R.drawable.drop_down_checked), null);
				}
				else
				{
					textView.setTextColor(sContext.getResources().getColor(android.R.color.black));
					textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			}
		}
	}

	public interface OnDropMenuClickListener
	{
		void onCityClick(int position);

		void onAgeClick(int position);

		void onSexClick(int position);

		void onConstellationClick(int position);
	}
}
