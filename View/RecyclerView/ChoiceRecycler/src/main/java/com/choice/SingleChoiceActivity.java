package com.choice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.test.SimpleRecyclerAdapter;
import com.yline.view.apply.SimpleLinearItemDecoration;
import com.yline.view.common.CommonRecyclerAdapter;
import com.yline.view.common.RecyclerViewHolder;

import java.util.List;

public class SingleChoiceActivity extends BaseAppCompatActivity
{
	private CommonRecyclerAdapter recyclerAdapter;

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SingleChoiceActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new SimpleLinearItemDecoration(this));

		recyclerAdapter = new SimpleRecyclerAdapter()
		{
			private boolean[] isSelected;

			private int oldPosition = -1;

			@Override
			public void setDataList(List<String> strings)
			{
				super.setDataList(strings);
				isSelected = new boolean[strings.size()];
			}

			@Override
			public int getItemRes()
			{
				return R.layout.item_recycler;
			}

			@Override
			public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position)
			{
				viewHolder.setText(R.id.tv_item, sList.get(position));

				viewHolder.getItemView().setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (oldPosition == position)
						{
							// do nothing
						}
						else
						{
							if (oldPosition == -1)
							{
								isSelected[position] = true;
								oldPosition = position;
								notifyItemChanged(position);
							}
							else
							{
								isSelected[oldPosition] = false;
								notifyItemChanged(oldPosition);

								isSelected[position] = true;
								oldPosition = position;
								notifyItemChanged(position);
							}
						}
					}
				});

				updateItemState(viewHolder, position);
			}

			private void updateItemState(RecyclerViewHolder viewHolder, int position)
			{
				viewHolder.getItemView().setSelected(isSelected[position]);
			}
		};
		recyclerView.setAdapter(recyclerAdapter);

		recyclerAdapter.setDataList(DeleteConstant.getList());
	}
}
