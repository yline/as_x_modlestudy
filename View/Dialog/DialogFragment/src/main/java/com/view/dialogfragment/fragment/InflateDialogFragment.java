package com.view.dialogfragment.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.view.dialogfragment.R;
import com.view.dialogfragment.activity.MainApplication;

public class InflateDialogFragment extends DialogFragment
{
	private View mContainer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContainer = inflater.inflate(R.layout.dialogfragment_inflate, container, false);
		return mContainer;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		Button btn_dismiss = (Button) mContainer.findViewById(R.id.btn_alter_dismiss);
		btn_dismiss.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dismiss();
				MainApplication.toast("InflateDialogFragment dismiss");
			}
		});
	}
}
