package com.system.app.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

/**
 * 音乐 todo 测试
 *
 * @author yline 2019/4/17 -- 18:12
 */
public class MusicActivity extends BaseTestActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, MusicActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        /*findViewById(R.id.btn_music_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicHelper.getInstance().test(MainActivity.this);
            }
        });*/
    }
}
