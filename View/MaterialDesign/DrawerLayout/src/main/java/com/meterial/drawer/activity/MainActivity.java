package com.meterial.drawer.activity;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;

import com.meterial.drawer.R;
import com.yline.base.BaseActivity;
import com.yline.utils.UIScreenUtil;

import java.lang.reflect.Field;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    // private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

//        findViewById(R.id.btn_left_open).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
        findViewById(R.id.btn_right_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        increaseSwipeEdgeOfDrawer(drawerLayout);

        /*navigationView = (NavigationView) findViewById(R.id.navigate_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    /**
     * https://www.thinbug.com/q/34877797
     *
     * 扩充滑动区域，但效果不是很理想，可以参考
     *
     * @param mDlSearchDrawer
     */
    public static void increaseSwipeEdgeOfDrawer(DrawerLayout mDlSearchDrawer) {
        try {

            Field mDragger = mDlSearchDrawer.getClass().getDeclaredField(
                "mRightDragger");//mRightDragger or mLeftDragger based on Drawer Gravity
            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper) mDragger
                .get(mDlSearchDrawer);

            Field mEdgeSize = draggerObj.getClass().getDeclaredField(
                "mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);

            mEdgeSize.setInt(draggerObj, UIScreenUtil.getScreenWidth(mDlSearchDrawer.getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            IApplication.toast("nav_camera");
        } else if (id == R.id.nav_gallery) {
            IApplication.toast("nav_gallery");
        } else if (id == R.id.nav_slideshow) {
            IApplication.toast("nav_slideshow");
        } else if (id == R.id.nav_manage) {
            IApplication.toast("nav_manage");
        } else if (id == R.id.nav_share) {
            IApplication.toast("nav_share");
        } else if (id == R.id.nav_send) {
            IApplication.toast("nav_send");
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
