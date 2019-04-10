package com.receiver.normal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.yline.utils.LogUtil;

/**
 * UI刷新专用，类似EventBus
 * 跨进程
 * 回调，直接在主线程里面实现
 *
 * @author yline  2019/4/10 -- 10:24
 */
public class UIRefreshReceiver extends BroadcastReceiver {
    private static final String KEY_REFRESH_FROM = "refresh_from";
    private static final String KEY_REFRESH_TO = "refresh_to";

    private static final String ACTION_REFRESH = "com.receiver.normal.ui.refresh";

    private OnRefreshListener mOnRefreshListener;
    /* 刷新用的常量【用于标识位置，从哪来到哪去】 */
    public static final int UI_MAIN_HOME = 10000; // ui 主页 - 首页

    /**
     * 注册广播
     *
     * @param context 上下文
     */
    public static UIRefreshReceiver register(Context context) {
        UIRefreshReceiver refreshReceiver = new UIRefreshReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_REFRESH);
        context.registerReceiver(refreshReceiver, intentFilter);
        return refreshReceiver;
    }

    /**
     * 发送消息
     *
     * @param context 发送方的上下文
     * @param from    通知刷新的，来源方
     * @param to      需要实现刷新的，实现方
     */
    public static void sendAction(Context context, int from, int to) {
        Intent intent = new Intent(UIRefreshReceiver.ACTION_REFRESH);
        intent.putExtra(KEY_REFRESH_FROM, from);
        intent.putExtra(KEY_REFRESH_TO, to);
        context.sendBroadcast(intent);
    }

    /**
     * 注销广播
     *
     * @param context         上下文
     * @param refreshReceiver 注册广播的对象
     */
    public static void unRegister(Context context, UIRefreshReceiver refreshReceiver) {
        context.unregisterReceiver(refreshReceiver);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            String action = intent.getAction();
            LogUtil.v("refresh receiver, action = " + action);
            if (ACTION_REFRESH.equals(action)) {
                int from = intent.getIntExtra(KEY_REFRESH_FROM, -1);
                int to = intent.getIntExtra(KEY_REFRESH_TO, -1);
                LogUtil.v("refresh receiver, from = " + from + ", to = " + to);
                handleReceiver(from, to);
            }
        } else {
            LogUtil.v("refresh receiver, intent is null");
        }
    }

    private void handleReceiver(int from, int to) {
        if (null != mOnRefreshListener) {
            mOnRefreshListener.onReceiver(from, to);
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    public interface OnRefreshListener {
        /**
         * 暂时未处理，回调在广播的周期中执行，不允许阻塞
         *
         * @param from 通知刷新的，来源方
         * @param to   需要实现刷新的，实现方
         */
        void onReceiver(int from, int to);
    }
}
