package com.meterial.drawer.activity;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;

import com.meterial.drawer.R;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        findViewById(R.id.btn_left_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.btn_right_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigate_view);
        navigationView.setNavigationItemSelectedListener(this);
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
