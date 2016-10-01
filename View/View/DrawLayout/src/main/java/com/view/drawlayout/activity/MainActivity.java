package com.view.drawlayout.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.view.drawlayout.R;
import com.yline.base.BaseFragmentActivity;

public class MainActivity extends BaseFragmentActivity implements NavigationView.OnNavigationItemSelectedListener
{
	private DrawerLayout drawerLayout;

	private NavigationView navigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView()
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationView = (NavigationView) findViewById(R.id.view_navigate);
		navigationView.setNavigationItemSelectedListener(this);
	}

	@Override
	public void onBackPressed()
	{
		if (drawerLayout.isDrawerOpen(GravityCompat.START))
		{
			drawerLayout.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.nav_camera)
		{
			MainApplication.toast("nav_camera");
		}
		else if (id == R.id.nav_gallery)
		{
			MainApplication.toast("nav_gallery");
		}
		else if (id == R.id.nav_slideshow)
		{
			MainApplication.toast("nav_slideshow");
		}
		else if (id == R.id.nav_manage)
		{
			MainApplication.toast("nav_manage");
		}
		else if (id == R.id.nav_share)
		{
			MainApplication.toast("nav_share");
		}
		else if (id == R.id.nav_send)
		{
			MainApplication.toast("nav_send");
		}
		
		drawerLayout.closeDrawer(GravityCompat.START);
		return true;
	}
}
