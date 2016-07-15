package com.system.app.caller;

import com.system.app.activity.AppConstant;
import com.yline.log.LogFileUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * android.permission.CALL_PHONE
 * @author YLine
 *
 * 2016年7月15日 下午9:36:45
 */
public class CallHelper
{
    /**
     * 拨打电话
     * @param context
     * @param tel   563850
     */
    public void call(Context context, String tel)
    {
        LogFileUtil.v(AppConstant.TAG_CALLER, "CallHelper -> call");
        
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tel));
        context.startActivity(intent);
    }
}
