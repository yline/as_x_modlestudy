package com.sample.okhttp.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sample.okhttp.R;
import com.sample.okhttp.bean.VNewsSingleBean;
import com.sample.okhttp.http.XHttp;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestFragment;

public class TestPostFragment extends BaseTestFragment
{
	@Override
	protected void testStart(View view, Bundle savedInstanceState)
	{
		addButton("请求文字", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new XHttp<VNewsSingleBean>()
				{
					@Override
					public void onSuccess(VNewsSingleBean vNewsSingleBean)
					{
					}

					@Override
					protected Object getRequestPostParam()
					{
						return new TestPostFragment.Request("1");
					}
				}.doRequest("new_1", VNewsSingleBean.class);
			}
		});

		addButton("请求图片流", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new XHttp<String>()
				{

					@Override
					public void onSuccess(String s)
					{

					}

					@Override
					protected boolean isResponseParse()
					{
						return false;
					}

					@Override
					protected boolean isResponseCodeHandler()
					{
						return false;
					}

					@Override
					protected String getRequestUrlBase()
					{
						return "http://120.92.35.211/wanghong/wh/Common/Uploads/2017-03-11/55cc154f216745.60264944.jpg";
					}
				}.doRequest("", String.class);
			}
		});

		final String url = "http://120.92.35.211/wanghong/wh/Common/Uploads/2017-03-11/55cc154f216745.60264944.jpg";
		final ImageView imageView = addImageView(250, 250);
		addButton("Glide请求图片", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("url = " + url);
				Glide.with(getContext()).load(url).error(R.mipmap.ic_launcher).into(imageView);
			}
		});
		addButton("清除图片内容", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				imageView.setImageBitmap(null);
			}
		});
	}

	private class Request
	{
		private String news_id;

		public Request(String news_id)
		{
			this.news_id = news_id;
		}

		public String getNews_id()
		{
			return news_id;
		}

		public void setNews_id(String news_id)
		{
			this.news_id = news_id;
		}
	}
}
