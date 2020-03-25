package com.yline.webview.study.jsbridge;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsBridgeUtil {
    public final static String YY_OVERRIDE_SCHEMA = "yy://";
    public final static String YY_RETURN_DATA = "yy://return/";//格式为   yy://return/{function}/returncontent

    final static String YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/";
    final static String EMPTY_STR = "";
    final static String UNDERLINE_STR = "_";
    final static String SPLIT_MARK = "/";

    final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";
    public final static String JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
    public final static String JS_FETCH_QUEUE_FROM_JAVA = "javascript:WebViewJavascriptBridge._fetchQueue();";
    public final static String JAVASCRIPT_STR = "javascript:";

    public static String parseFunctionName(String jsUrl) {
        return jsUrl.replace("javascript:WebViewJavascriptBridge.", "").replaceAll("\\(.*\\);", "");
    }

    public static String getDataFromReturnUrl(String url) {
        if (url.startsWith(YY_FETCH_QUEUE)) {
            return url.replace(YY_FETCH_QUEUE, EMPTY_STR);
        }

        String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
        String[] functionAndData = temp.split(SPLIT_MARK);

        if (functionAndData.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < functionAndData.length; i++) {
                sb.append(functionAndData[i]);
            }
            return sb.toString();
        }
        return null;
    }

    public static String getFunctionFromReturnUrl(String url) {
        String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
        String[] functionAndData = temp.split(SPLIT_MARK);
        if (functionAndData.length >= 1) {
            return functionAndData[0];
        }
        return null;
    }

    /**
     * 没用过
     * js 文件将注入为第一个script引用
     */
    public static void webViewLoadJs(WebView webView, String url) {
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"" + url + "\";";
        js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
        webView.loadUrl("javascript:" + js);
    }

    public static void loadLocalJsForWebview(WebView webView) {
        String jsContent = loadInitJs(webView.getContext(), LOAD_JS_PATH);
        webView.loadUrl("javascript:" + jsContent);
    }

    private static String JS_CODE = null;
    private static final String LOAD_JS_PATH = "WebViewJavascriptBridge.js";

    private static String loadInitJs(Context context, String urlStr) {
        if (!TextUtils.isEmpty(JS_CODE)) {
            return JS_CODE;
        }

        InputStream in = null;
        try {
            in = context.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line = null;
            StringBuilder sb = new StringBuilder();
            do {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*")) {
                    sb.append(line);
                }
            } while (line != null);

            bufferedReader.close();
            in.close();

            JS_CODE = sb.toString();
            return JS_CODE;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
