package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nohttp.R;
import com.nohttp.common.CommonActivity;
import com.nohttp.helper.RecyclerListSingleAdapter;

public class UploadFileActivity extends CommonActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_file);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_upload_activity);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration()
		{
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
			{
				outRect.set(10, 10, 10, 10);
			}
		});

		RecyclerListSingleAdapter singleAdapter = new RecyclerListSingleAdapter();
		recyclerView.setAdapter(singleAdapter);

		singleAdapter.setDataList(this, R.array.activity_upload_item);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, UploadFileActivity.class));
	}
}
