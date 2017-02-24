package com.viewgroup.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.viewgroup.R;
import com.viewgroup.view.TagLayout;
import com.yline.base.common.CommonListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
	private TagLayout mContainer;

	// private TagBaseAdapter mAdapter;

	private List<String> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setContentView(R.layout.activity_main);
		mContainer = (TagLayout) findViewById(R.id.container);

		mList = new ArrayList<>();
		mList.add("中华人名共和国");
		mList.add("大韩民国");
		mList.add("日本");
		mList.add("朝鲜");
		mList.add("台湾");
		mList.add("香港特别行政区");
		mList.add("澳门特别行政区");
		mList.add("越南");
		mList.add("老挝");
		mList.add("柬埔寨");
		mList.add("泰国");
		mList.add("缅甸");
		mList.add("马来西亚");
		mList.add("新加坡");
		mList.add("印度尼西亚");
		mList.add("文莱");
		mList.add("菲律宾");
		//mAdapter = new TagBaseAdapter(this, mList);
		
		final CommonListAdapter adapter = new CommonListAdapter<String>(this)
		{
			@Override
			protected int getItemRes(int i)
			{
				return R.layout.tagview;
			}

			@Override
			protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
			{
				Button btnTag = viewHolder.get(R.id.tag_btn);
				btnTag.setText(sList.get(i));
			}
		};
		adapter.add(mList);

		mContainer.setAdapter(adapter);

		findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				adapter.add("东帝汶");
			}
		});

		mContainer.setItemClickListener(new TagLayout.OnTagItemClickListener()
		{
			@Override
			public void onClick(int position)
			{
				Toast.makeText(MainActivity.this, mList.get(position), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
