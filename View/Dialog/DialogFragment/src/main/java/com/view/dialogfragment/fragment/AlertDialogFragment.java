package com.view.dialogfragment.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.view.dialogfragment.R;

/**
 * Created by yline on 2016/9/25.
 */
public class AlertDialogFragment extends DialogFragment
{
	private EditText mUsername;

	private EditText mPassword;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_alter, null);
		mUsername = (EditText) view.findViewById(R.id.et_alter_username);
		mPassword = (EditText) view.findViewById(R.id.et_alter_password);

		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setView(view);

		dialog.setPositiveButton("Sign in", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				if (getActivity() instanceof AlterInputListener)
				{
					((AlterInputListener) getActivity()).onAlertInputComplete(mUsername.getText().toString(), mPassword.getText().toString());
				}
			}
		});
		dialog.setNegativeButton("Cancel", null);

		return dialog.create();
	}

	public interface AlterInputListener
	{
		/**
		 * 输入点击确定后执行操作
		 * @param username
		 * @param password
		 */
		void onAlertInputComplete(String username, String password);
	}
}
