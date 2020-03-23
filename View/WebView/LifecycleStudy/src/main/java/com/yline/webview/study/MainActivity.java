package com.yline.webview.study;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Webview - 百度翻译", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String httpUrl = "https://fanyi.baidu.com/?aldtype=16047#auto/zh";
                WebviewActivity.launch(MainActivity.this, httpUrl);
            }
        });

        addButton("Webview - 百度一下", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String httpUrl = "https://www.baidu.com/";
                WebviewActivity.launch(MainActivity.this, httpUrl);
            }
        });
    }
}
