package com.system.app.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.system.app.caller.CallHelper;
import com.system.app.contacter.ContacterHelper;
import com.system.app.msg.MsgHelper;
import com.system.app.music.MusicHelper;
import com.system.app.wifi.WifiHelper;
import com.system.apps.R;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

/**
 * 高版本各种对权限，要求，现在暂时不处理，遗留
 * @author yline 2019/2/25 -- 11:25
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new CallHelper().call(MainActivity.this, "563850");
            }
        });

        findViewById(R.id.btn_contacter_query_type1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new ContacterHelper().queryContacter(MainApplication.getApplication());
            }
        });

        findViewById(R.id.btn_contacter_query_type2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new ContacterHelper().queryContacterInfo(MainApplication.getApplication());
            }
        });

        findViewById(R.id.btn_contacter_insert).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new ContacterHelper().insertContacter(MainApplication.getApplication());
            }
        });

        new MsgHelper().registerMsgobserver(MainApplication.getApplication());
        findViewById(R.id.btn_msg_send).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MsgHelper().sendMsg();
            }
        });

        findViewById(R.id.btn_msg_read).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MsgHelper().readMsgAll(MainActivity.this);
            }
        });

        findViewById(R.id.btn_wifi_test).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new WifiHelper().testWifi(MainActivity.this);
            }
        });

        findViewById(R.id.btn_music_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicHelper.getInstance().test(MainActivity.this);
            }
        });
    }

    @Override
    protected String[] initRequestPermission() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE};
    }
}
