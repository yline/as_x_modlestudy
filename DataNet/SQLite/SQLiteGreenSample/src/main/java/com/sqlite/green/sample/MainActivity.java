package com.sqlite.green.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sqlite.green.SQLiteManager;
import com.sqlite.green.async.AsyncOperation;
import com.sqlite.green.async.AsyncOperationListener;
import com.sqlite.green.async.AsyncOperationModel;
import com.sqlite.green.test.NetCacheModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 第一步
        DaoManager.init(this);

        // UI线程操作
        DaoManager.getNetCacheModelDao().insert(new NetCacheModel("url", new Object()));

        // 异步操作
        AsyncOperation asyncOperation = DaoManager.getNetCacheModelDaoAsync();
        asyncOperation.insert(new NetCacheModel("url", new Object()));
        asyncOperation.setOnOperationListener(new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                // 非UI线程，依旧在子线程之中
            }
        });

        SQLiteManager.load("").getResultData();
    }
}
