package com.view.webview.webview;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * js与java桥接
 */
public class JSBridge {

    private static String JS_CODE = null;
    protected static final String HEAD = "kjtpayApp://";


    public synchronized static String loadInitJS(Context context) {
        if (!TextUtils.isEmpty(JS_CODE)) {
            return JS_CODE;
        }

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("kjtBridge.js")));
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        JS_CODE = sb.toString();
        return JS_CODE;
    }


}
