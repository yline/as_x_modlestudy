package com.recycler.group.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.recycler.group.R;
import com.recycler.group.uadapter.CommonGroupRecyclerAdapter;
import com.yline.base.BaseAppCompatActivity;
import com.yline.common.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{
	private RecyclerView mRecyclerView;

	private EAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
	}

	private void initView()
	{
		mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		mAdapter = new EAdapter();
		//设置header
		mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

		// mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setAdapter(mAdapter);
	}

	private List<String> headList;

	private List<List<String>> itemDoubleList;

	private void initData()
	{
		headList = Arrays.asList("热门品牌", "商业区", "热门景点", "机场车站", "行政区", "地铁线路", "热点酒店");

		itemDoubleList = new ArrayList<>();
		itemDoubleList.add(Arrays.asList("汉庭", "7天", "如家", "速8", "锦江之星", "飘HOME", "海友", "锦江", "世纪金源"));
		itemDoubleList.add(Arrays.asList("天安门商圈", "中关村商圈", "西直门商圈", "前门/崇文门", "首都机场地区", "西单/金融街", "奥体中心地区", "公主坟商圈", "西站/丽泽区", "国贸地区", "东直门/工体",
				"南站/永定门", "建国门区域", "劲松/潘家园", "三里屯商圈", "国展区域"));
		itemDoubleList.add(Arrays.asList("天安门", "故宫", "北京欢乐谷", "北京欢乐水魔方水上乐园", "北京植物园", "北京动物园", "颐和园", "圆明园", "天坛", "北京香山", "八达岭长城", "世界花卉大观园", "十三陵",
				"鸟巢", "王府井商业街", "南锣鼓巷"));
		itemDoubleList.add(Arrays.asList("首都机场1/2号航站楼", "首都机场3号航站楼", "北京南苑机场", "北京西站",
				"北京站", "北京南站", "北京北站", "北京东站", "东直门长途汽车站", "永定门长途汽车站", "木樨园长途汽车站",
				"四惠长途汽车站", "北京南站汽车站", "西直门汽车客运站", "六里桥汽车客运站", "赵公口汽车客运站"));
		itemDoubleList.add(Arrays.asList("朝阳区", "海淀区", "密云县", "东城区", "丰台区", "西城区", "怀柔区", "昌平区", "延庆县", "顺义区",
				"房山区", "大兴区", "通州区", "平谷区", "门头沟", "石景山区"));
		itemDoubleList.add(Arrays.asList("四惠东", "四惠", "大望路", "国贸", "永安里", "建国门", "东单", "王府井", "天安门东"));
		itemDoubleList.add(Arrays.asList("王府井周边", "新国展中心", "奥体中心"));

		mAdapter.setDataList(headList, itemDoubleList);
	}

	private class EAdapter extends CommonGroupRecyclerAdapter<String, String, Object>
	{
		private SparseBooleanArray mBooleanMap;

		private int oldPosition = -1;

		public EAdapter()
		{
			mBooleanMap = new SparseBooleanArray();
		}

		public void setDataList(List<String> headList, List<List<String>> itemDoubleList)
		{
			this.headList = headList;
			this.itemDoubleList = itemDoubleList;
			notifyDataSetChanged();
		}

		@Override
		protected int getGroupItemCount(int positionOfGroup)
		{
			int count = super.getGroupItemCount(positionOfGroup);

			// 实现 展开和收起的主要位置
			if (count >= 0 && !mBooleanMap.get(positionOfGroup))
			{
				count = 0;
			}

			return count;
		}

		@Override
		protected int getHeadRes()
		{
			return R.layout.hotel_title_item;
		}

		@Override
		protected void onBindViewHolderHead(CommonRecyclerViewHolder viewHolder, List<String> strings, final int positionOfGroup)
		{
			final TextView textView = viewHolder.get(R.id.tv_open);
			textView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					boolean isOpen = mBooleanMap.get(positionOfGroup);
					String text = isOpen ? "展开" : "关闭";
					mBooleanMap.put(positionOfGroup, !isOpen);

					if (oldPosition != positionOfGroup)
					{
						if (oldPosition != -1)
						{
							mBooleanMap.put(oldPosition, false);
						}
						oldPosition = positionOfGroup;
					}
					else
					{
						oldPosition = -1;
					}

					textView.setText(text);
					notifyDataSetChanged();
				}
			});
			textView.setText(mBooleanMap.get(positionOfGroup) ? "关闭" : "展开");

			viewHolder.setText(R.id.tv_title, strings.get(positionOfGroup));
		}

		@Override
		protected int getItemRes()
		{
			return R.layout.hotel_desc_item;
		}

		@Override
		protected void onBindViewHolderItem(CommonRecyclerViewHolder viewHolder, List<List<String>> itemDoubleList, int positionOfGroup, int positionInGroup)
		{
			viewHolder.setText(R.id.tv_desc, itemDoubleList.get(positionOfGroup).get(positionInGroup));
		}
	}
}
