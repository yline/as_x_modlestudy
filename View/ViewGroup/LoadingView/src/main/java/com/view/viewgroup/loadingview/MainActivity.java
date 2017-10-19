package com.view.viewgroup.loadingview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yline.application.SDKManager;

public class MainActivity extends AppCompatActivity {
    private LoadingView mLoadingView;
    private int retryTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingView = (LoadingView) findViewById(R.id.main_view_loading);
        retryTime = 0;

        initView();
        mLoadingView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initView();
            }
        });
    }

    private void initView() {
        mLoadingView.loading();
        SDKManager.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (retryTime > 3) {
                    mLoadingView.loadSuccess();
                } else {
                    retryTime++;
                    mLoadingView.loadFailed();
                }
            }
        }, 2500);
    }
}
