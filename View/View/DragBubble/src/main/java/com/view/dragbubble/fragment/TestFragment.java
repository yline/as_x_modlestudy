package com.view.dragbubble.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.view.dragbubble.R;
import com.view.dragbubble.activity.MainApplication;
import com.view.dragbubble.view.DragBubble;
import com.yline.base.BaseFragment;
import com.yline.base.common.CommonListAdapter;
import com.yline.log.LogFileUtil;

public class TestFragment extends BaseFragment
{
	private static final String TAG = "TestAdapters";

	private String[] strs = {"yline", "yui", "f21", "fatenliyer", "joe"};

	private TestAdapter testAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_contact, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		testAdapter = new TestAdapter(getActivity());
		if (view instanceof ListView)
		{
			((ListView) view).setAdapter(testAdapter);
		}
		else
		{
			MainApplication.toast("view is null");
		}

		for (int i = 0; i < 30; i++)
		{
			testAdapter.add(new DataBean(strs[i % strs.length], i + "", true));
		}
	}

	private class TestAdapter extends CommonListAdapter<DataBean>
	{
		public TestAdapter(Context context)
		{
			super(context);
		}

		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_contact;
		}

		@Override
		protected void setViewContent(final int i, final ViewGroup viewGroup, ViewHolder viewHolder)
		{
			LogFileUtil.v(TAG, "setViewContent position = " + i);
			viewHolder.setText(R.id.tv_content, sList.get(i).getContent());

			final DragBubble dragBubble = viewHolder.get(R.id.dragbubble);
			dragBubble.setText(sList.get(i).getMsgNumber());
			dragBubble.setVisibility(sList.get(i).getVisibility() ? View.VISIBLE : View.GONE);

			dragBubble.setOnDragListener(new DragBubble.OnDragListener()
			{
				@Override
				public void onDragOut()
				{
					sList.get(i).setVisibility(false);
					sList.get(i).setMsgNumber("0");
				}
			});
		}
	}

	private class DataBean
	{
		public DataBean(String content, String msgNumber, boolean visibility)
		{
			this.content = content;
			this.msgNumber = msgNumber;
			this.visibility = visibility;
		}

		private String content;

		private String msgNumber;

		private boolean visibility;

		public String getContent()
		{
			return content;
		}

		public void setContent(String content)
		{
			this.content = content;
		}

		public String getMsgNumber()
		{
			return msgNumber;
		}

		public void setMsgNumber(String msgNumber)
		{
			this.msgNumber = msgNumber;
		}

		public boolean getVisibility()
		{
			return visibility;
		}

		public void setVisibility(boolean visibility)
		{
			this.visibility = visibility;
		}
	}
}
