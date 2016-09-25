package com.view.dialogfragment.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.view.dialogfragment.R;
import com.view.dialogfragment.fragment.AlertDialogFragment;
import com.view.dialogfragment.fragment.InflateDialogFragment;
import com.view.dialogfragment.fragment.ToastDialogFragment;
import com.yline.base.BaseFragmentActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseFragmentActivity implements AlertDialogFragment.AlterInputListener
{
	private static final String TAG = "MainActivity";

	/** 对话框的Dialog */
	private AlertDialogFragment alertDialogFragment;

	/** 单纯的toast */
	private ToastDialogFragment toastDialogFragment;

	/** 插入到内容的DialogFragment */
	private InflateDialogFragment inflateDialogFragment;

	private FragmentManager mFragmentManager = getSupportFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		alertDialogFragment = new AlertDialogFragment();
		findViewById(R.id.btn_dialog_alert).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_dialog_alert");
				alertDialogFragment.show(getSupportFragmentManager(), "alertDialog");
			}
		});

		toastDialogFragment = new ToastDialogFragment();
		findViewById(R.id.btn_dialog_toast).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_dialog_toast");
				toastDialogFragment.show(getSupportFragmentManager(), "toastDialog");
			}
		});

		inflateDialogFragment = new InflateDialogFragment();
		findViewById(R.id.btn_dialog_inflate).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_dialog_inflate");
				mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fl_content, inflateDialogFragment).commit();
			}
		});
	}

	@Override
	public void onAlertInputComplete(String username, String password)
	{
		MainApplication.toast("username = " + username + "\n" + "password = " + password);
	}
}
