package com.gridview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gridview.DragExchangeAdapter;
import com.gridview.DragExchangeGridView;
import com.gridview.R;
import com.gridview.bean.DataBean;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements DragExchangeAdapter.IItemClickListener
{
	public static final String TAG = "MainActivity";

	private DragExchangeGridView gridView;

	private DragExchangeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (DragExchangeGridView) findViewById(R.id.gridview_grag_exchange);
		adapter = new DragExchangeAdapter(this);
		gridView.setAdapter(adapter);

		final EditText etNumber = (EditText) findViewById(R.id.et_number);

		findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String temp = etNumber.getText().toString().trim();
				LogFileUtil.v(TAG, "btn_update temp number = " + temp);

				int number = Integer.parseInt(temp);

				List<DataBean> dataBeanList = new ArrayList<DataBean>();
				for (int i = 0; i < number; i++)
				{
					DataBean dataBean = new DataBean();
					dataBean.setName("update " + i).setIcon(MainActivity.this, R.mipmap.ic_launcher).end();

					dataBeanList.add(dataBean);
				}

				adapter.updateAppData(dataBeanList);
			}
		});
	}

	@Override
	public void onItemClick(View view, DataBean dataBean)
	{
		MainApplication.toast("Data = " + dataBean.toString());
	}
}
