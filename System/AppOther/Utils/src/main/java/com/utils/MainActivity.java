package com.utils;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.utils.utils.NetworkUtil;
import com.utils.utils.RootUtil;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        // 检查网络状态
        addButton("NetworkUtil", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtil.NetType netType = NetworkUtil.getNetType(MainActivity.this);
                LogFileUtil.v(MainApplication.TAG, "netType = " + netType);
            }
        });

        // 检查Root权限
        final TextView rootTextView = addTextView("");
        addButton("Root权限", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = "";
                long startTime = System.currentTimeMillis();

                boolean rootByExecute = RootUtil.checkRootByExecute();
                LogUtil.v("rootByExecute = " + rootByExecute + ", diffTime = " + (System.currentTimeMillis() - startTime));
                value += ("rootByExecute = " + rootByExecute + ", diffTime = " + (System.currentTimeMillis() - startTime));
                rootTextView.setText(value);
            }
        });
    }
}
