package com.view.webview;

import android.os.Bundle;
import android.view.View;

import com.view.webview.webview.WebviewActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Webview", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://fanyi.baidu.com/";
                String title = "";

                WebviewActivity.launch(MainActivity.this, title, url);
            }
        });

        addButton("本地js调试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "file:///android_asset/bbtuanJsBridgeTest.html";
                WebviewActivity.launch(MainActivity.this, "", url);
            }
        });
    }
}
