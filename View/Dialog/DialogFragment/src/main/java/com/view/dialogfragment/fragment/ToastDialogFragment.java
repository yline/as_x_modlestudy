package com.view.dialogfragment.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.view.dialogfragment.R;
import com.view.dialogfragment.activity.MainApplication;

public class ToastDialogFragment extends DialogFragment
{
	private View mContainer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContainer = inflater.inflate(R.layout.dialogfragment_toast, container, false);
		return mContainer;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		Button btn_sure = (Button) mContainer.findViewById(R.id.btn_inflate_sure);
		final EditText et_password = (EditText) mContainer.findViewById(R.id.et_inflate_username);
		btn_sure.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dismiss();
				MainApplication.toast("inflate_username + " + et_password.getText().toString().trim());
			}
		});
	}
}
