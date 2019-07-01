package com.yline.process.alive.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yline.process.alive.service.DaemonService;
import com.yline.utils.LogUtil;

/**
 * 可能有些广播，需要动态注册；
 * 但是：如果动态注册的还在，就不需要广播拉起进程了。因此，动态注册逻辑上不需要
 *
 * @author yline 2019/7/1 -- 16:10
 */
public class KeepAliveReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.v("action = " + action);

        if ("com.yline.KEEP_ALIVE".equals(action)) {
            // todo 同类型的app，唤醒
            wakeUpApp(context);
        } else if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            // todo 开机
            wakeUpApp(context);
        } else if ("android.intent.action.SCREEN_ON".equals(action)) {
            // todo 亮屏
            wakeUpApp(context);
        } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
            // todo 灭屏
            wakeUpApp(context);
        } else if ("android.intent.action.HEADSET_PLUG".equals(action)) {
            // todo 插拔有线耳机
            wakeUpApp(context);
        } else if ("android.intent.action.BATTERY_OKAY".equals(action)) {
            // todo 电量充足
            wakeUpApp(context);
        }
    }

    /**
     * 唤醒APP
     */
    private void wakeUpApp(Context context) {
        DaemonService.launcher(context);
    }
}
