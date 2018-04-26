package com.thread.asynctaskloader.activity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.thread.asynctaskloader.AsyncTaskLoaderSample;
import com.thread.asynctaskloader.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

import java.util.List;

/**
 * AsyncTaskLoader 和 AsyncTask 很类似
 * 区分：http://blog.csdn.net/a910626/article/details/45599133
 * AsyncTaskLoader:加载数据更方便,也能灵活依据Activity的配置改变;它是基于AsyncTask基础上扩展实现的
 * AsyncTask:更轻便
 */
public class MainActivity extends BaseAppCompatActivity {
    private ArrayAdapter<String> adapter;

    private ListView lvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTest = (ListView) findViewById(R.id.lv_test);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lvTest.setAdapter(adapter);

        this.getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<List<String>>() {
            /**
             * 在创建activity时跟着onCreate会调用一次
             */
            @Override
            public Loader<List<String>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoaderSample(MainActivity.this, 150);
            }

            /**
             * 在关闭Activity时调用，释放资源
             */
            @Override
            public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
                LogFileUtil.v("onLoadFinished ");
                if (adapter != null) {
                    adapter.clear();
                    for (String s : data) {
                        adapter.add(s);
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<List<String>> loader) {
                LogFileUtil.v("onLoaderReset ");
            }
        });
    }
}
