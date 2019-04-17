package com.system.app;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.system.app.caller.CallerActivity;
import com.system.app.contacter.ContactActivity;
import com.system.app.msg.MsgActivity;
import com.system.app.music.MusicActivity;
import com.system.app.wifi.WifiActivity;
import com.yline.test.BaseTestActivity;

/**
 * 高版本各种对权限，要求，现在暂时不处理，遗留
 *
 * @author yline 2019/2/25 -- 11:25
 */
public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("caller", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallerActivity.launch(MainActivity.this);
            }
        });

        addButton("contact", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactActivity.launch(MainActivity.this);
            }
        });

        addButton("msg", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgActivity.launch(MainActivity.this);
            }
        });

        addButton("music", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicActivity.launch(MainActivity.this);
            }
        });

        addButton("wifi", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiActivity.launch(MainActivity.this);
            }
        });
    }
}
