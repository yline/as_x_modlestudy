package com.lrucache.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.GridView;

import com.lrucache.R;
import com.lrucache.view.SquareImageView;
import com.yline.base.common.CommonListAdapter;
import com.yline.log.LogFileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import libcore.io.DiskLruCache;

public class MainActivity extends AppCompatActivity
{
	private GridAdapter gridAdapter;

	private List<String> urlList;

	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.grid_view);
		urlList = getImageUrl();

		gridAdapter = new GridAdapter(MainActivity.this);
		gridAdapter.addAll(urlList);

		gridView.setAdapter(gridAdapter);
	}

	private List<String> getImageUrl()
	{
		List<String> list = new ArrayList<>();
		list.add("http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg");
		list.add("http://pic28.nipic.com/20130424/11588775_115415688157_2.jpg");
		list.add("http://pic44.nipic.com/20140717/2531170_194615292000_2.jpg");
		list.add("http://g.hiphotos.baidu.com/image/pic/item/810a19d8bc3eb135f59e64bfa41ea8d3fd1f445d.jpg");
		list.add("http://e.hiphotos.baidu.com/image/pic/item/e4dde71190ef76c62bee33059f16fdfaaf5167e4.jpg");
		list.add("http://b.hiphotos.baidu.com/image/pic/item/d52a2834349b033b926643d317ce36d3d439bdc9.jpg");
		list.add("http://c.hiphotos.baidu.com/image/pic/item/7a899e510fb30f24489e0207ca95d143ad4b03a9.jpg");
		list.add("http://a.hiphotos.baidu.com/image/pic/item/0824ab18972bd4078571c9f379899e510fb3090d.jpg");
		list.add("http://h.hiphotos.baidu.com/image/h%3D200/sign=39c1500eb4003af352badb60052ac619/8d5494eef01f3a2922e765c99b25bc315c607c8d.jpg");
		return list;
	}

	private class GridAdapter extends CommonListAdapter<String>
	{
		public GridAdapter(Context context)
		{
			super(context);
		}

		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_grid;
		}

		@Override
		protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			SquareImageView squareImageView = viewHolder.get(R.id.iv_square);
			String url = getItem(i);
			LogFileUtil.v("url = " + url + ",squareImageView = " + squareImageView);
			MainApplication.getImageLoader().bindBitmap(url, squareImageView);
		}
	}
	
	// LruCache的使用
	private void testLruCache()
	{
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // kb
		int cacheSize = maxMemory / 8; // maxMemory的 1/8

		LruCache mMemoryCache = new LruCache<String, Bitmap>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes() * value.getHeight() / 1024;
				// return super.sizeOf(key, value); // 默认 返回1
			}
		};

		// 获取对象
		mMemoryCache.get("key");

		// 添加对象
		mMemoryCache.put("key", "value");

		// 删除对象
		mMemoryCache.remove("key");
	}

	private void testDiskLruCache()
	{
		final long DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

		File diskCacheDir = null;
		// diskCacheDir = getDiskCacheDis(this, "bitmap");
		if (!diskCacheDir.exists())
		{
			diskCacheDir.mkdirs();
		}

		try
		{
			DiskLruCache diskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
