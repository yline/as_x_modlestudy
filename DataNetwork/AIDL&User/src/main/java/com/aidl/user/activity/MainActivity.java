package com.aidl.user.activity;

import android.os.Bundle;
import android.view.View;

import com.data.network.aidl.user.R;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

/**
 * 提供者aidl文件包名要求和使用者一模一样
 *
 * @author YLine
 *         <p>
 *         2016年7月30日 下午4:16:30
 */
public class MainActivity extends BaseActivity {
    private AIDLManager mAIDLMananger = new AIDLManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAIDLMananger.testBind(MainActivity.this);

        findViewById(R.id.btn_aidl_test).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LogFileUtil.v(MainApplication.TAG, "btn_aidl_test");
                mAIDLMananger.testCall();
            }
        });
    }

}
