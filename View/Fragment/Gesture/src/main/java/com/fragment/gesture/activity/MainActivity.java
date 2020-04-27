package com.fragment.gesture.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.fragment.gesture.GestureFragment;
import com.view.fragment.gesture.R;
import com.yline.base.BaseFragmentActivity;

/**
 * 实现接口
 *
 * @author YLine
 * <p>
 * 2016年8月4日 上午7:39:15
 */
public class MainActivity extends BaseFragmentActivity implements GestureFragment.onGestureJugdeCallback {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private GestureFragment gestureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initData();
    }

    private void initData() {
        gestureFragment = new GestureFragment();

        fragmentManager.beginTransaction().add(R.id.content_frame, gestureFragment, "content_player").commit();
    }

    @Override
    public void onGestureResult(GestureFragment.GestureJudge result) {
        MainApplication.toast(result + "");
        switch (result) {
            case DOUBLE_CLICK:

                break;
            case HG_LEFT:

                break;
            case HG_RIGHT:

                break;
            case VG_LEFT_DOWN:

                break;
            case VG_LEFT_UP:

                break;
            case VG_RIGHT_DOWN:

                break;
            case VG_RIGHT_UP:

                break;
            case NONE:

                break;
        }
    }

}
