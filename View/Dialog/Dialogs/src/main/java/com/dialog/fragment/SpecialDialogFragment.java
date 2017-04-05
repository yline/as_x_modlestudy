package com.dialog.fragment;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.view.dialog.R;
import com.yline.test.BaseTestFragment;

public class SpecialDialogFragment extends BaseTestFragment
{
	@Override
	protected void testStart()
	{
		addButton("底部弹出", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_special_bottom, null);

				Dialog dialog = new Dialog(getContext(), R.style.dialog);// android.R.style.Theme_Holo_Light_Dialog_NoActionBar
				dialog.setContentView(view);

				Window dialogWindow = dialog.getWindow();
				dialogWindow.setGravity(Gravity.BOTTOM);

				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
				lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
				dialog.onWindowAttributesChanged(lp);

				dialog.show();
			}
		});

		addButton("全屏", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_special_bottom, null);

				Dialog dialog = new Dialog(getContext(), R.style.dialog);
				dialog.setContentView(view);
				
				Window dialogWindow = dialog.getWindow();
				dialogWindow.setGravity(Gravity.BOTTOM);

				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
				lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
				dialog.onWindowAttributesChanged(lp);

				dialog.show();
			}
		});
	}
}
