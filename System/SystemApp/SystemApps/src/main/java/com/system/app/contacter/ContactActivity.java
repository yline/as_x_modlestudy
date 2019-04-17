package com.system.app.contacter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

/**
 * 联系人 todo 测试权限问题；
 * 暂时用不到，没空去弄
 *
 * @author yline 2019/4/17 -- 18:09
 */
public class ContactActivity extends BaseTestActivity {
    public static void launch(Context context){
    	if (null != context){
    		Intent intent = new Intent();
    		intent.setClass(context, ContactActivity.class);
    		if (!(context instanceof Activity)){
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        /*findViewById(R.id.btn_contacter_query_type1).setOnClickListener(new View.OnClickListener() {

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
        });*/
    }
}
