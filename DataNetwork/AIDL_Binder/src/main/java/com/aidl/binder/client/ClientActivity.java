package com.aidl.binder.client;

import android.os.Bundle;
import android.view.View;

import com.aidl.binder.R;
import com.yline.base.BaseActivity;
import com.yline.utils.LogUtil;

/**
 * 参考：https://www.jianshu.com/p/429a1ff3560c
 *
 * @author yline 2019/7/17 -- 11:32
 */
public class ClientActivity extends BaseActivity {
    private AIDLManager.ServiceConn mServiceConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        mServiceConn = AIDLManager.bindServiceConn(ClientActivity.this);
        findViewById(R.id.client_aidl_test).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LogUtil.v("btn_aidl_test");
                AIDLManager.callServiceConn(mServiceConn);
            }
        });
    }
}
