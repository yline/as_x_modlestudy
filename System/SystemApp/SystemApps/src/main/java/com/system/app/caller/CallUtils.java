package com.system.app.caller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.system.app.activity.AppConstant;
import com.yline.log.LogFileUtil;

/**
 * android.permission.CALL_PHONE
 *
 * @author yline 2019/4/17 -- 18:02
 */
public class CallUtils {
    /**
     * 拨打电话
     *
     * @param phoneNum 563850
     */
    public static void call(Context context, String phoneNum) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }
}
