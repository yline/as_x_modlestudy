package com.dialog.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.view.dialog.R;

public class DialogIosWidget
{
	private View parentView;

	private Dialog dialog;

	private View.OnClickListener onPositiveListener = null;

	private View.OnClickListener onNegativeListener = null;

	public DialogIosWidget(Context context)
	{
		parentView = LayoutInflater.from(context).inflate(getXLayoutId(), null);

		dialog = new Dialog(context, getXDialogStyle());
		dialog.setContentView(parentView);
	}

	public void show()
	{
		TextView titleTextView = parentView.findViewById(R.id.widget_tv_dialog_ios_title);
		TextView messageTextView = parentView.findViewById(R.id.widget_tv_dialog_ios_message);
		Button negativeButton = parentView.findViewById(R.id.widget_btn_dialog_ios_negative);
		Button positiveButton = parentView.findViewById(R.id.widget_btn_dialog_ios_positive);
		initXView(titleTextView, messageTextView, negativeButton, positiveButton, dialog);

		dialog.show();
	}

	/**
	 * 初始化 控件
	 * @param tvTitle 标题
	 * @param tvMsg 副标题
	 * @param btnNegative “取消”按钮
	 * @param btnPositive “确认”按钮
	 */
	protected void initXView(TextView tvTitle, TextView tvMsg, Button btnNegative, Button btnPositive, final Dialog dialog)
	{
		// 显示字体
		tvTitle.setText("标题");
		tvMsg.setText("消息内容");
		btnNegative.setText("取消");
		btnPositive.setText("确定");

		// 监听事件
		btnNegative.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != onNegativeListener)
				{
					onNegativeListener.onClick(v);
					dialog.dismiss();
				}
				else
				{
					dialog.dismiss();
				}
			}
		});
		btnPositive.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != onPositiveListener)
				{
					onPositiveListener.onClick(v);
					dialog.dismiss();
				}
				else
				{
					dialog.dismiss();
				}
			}
		});

		// Dialog设置
		dialog.setCanceledOnTouchOutside(true);
	}

	public void setOnShowListener(DialogInterface.OnShowListener listener)
	{
		this.dialog.setOnShowListener(listener);
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener listener)
	{
		this.dialog.setOnDismissListener(listener);
	}

	/**
	 * 确认按钮点击
	 *
	 * @param onPositiveListener
	 */
	public void setOnPositiveListener(View.OnClickListener onPositiveListener)
	{
		this.onPositiveListener = onPositiveListener;
	}

	/**
	 * 取消按钮点击
	 * @param onNegativeListener
	 */
	public void setOnNegativeListener(View.OnClickListener onNegativeListener)
	{
		this.onNegativeListener = onNegativeListener;
	}

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 提供重写的数据 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

	/**
	 * @return Layout资源文件
	 */
	protected int getXLayoutId()
	{
		return R.layout.widget_dialog_ios;
	}

	/**
	 * Dialog 对话框 风格
	 *
	 * @return
	 */
	protected int getXDialogStyle()
	{
		return R.style.dialog;
	}
}
