package com.dialog.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.view.dialog.R;

public class DialogIosWidget
{
	private View parentView;

	private Dialog dialog;

	public DialogIosWidget(Context context)
	{
		parentView = LayoutInflater.from(context).inflate(getLayoutResourceId(), null);

		dialog = new Dialog(context, R.style.AppDialog_Default);
		dialog.setContentView(parentView);
	}

	public void show()
	{
		Builder builder = new Builder();
		initBuilder(builder);

		TextView titleTextView = (TextView) parentView.findViewById(R.id.widget_tv_dialog_ios_title);
		initTitleTextView(titleTextView, builder);

		TextView messageTextView = (TextView) parentView.findViewById(R.id.widget_tv_dialog_ios_message);
		initMessageTextView(messageTextView, builder);

		Button negativeButton = (Button) parentView.findViewById(R.id.widget_btn_dialog_ios_negative);
		initNegativeButton(negativeButton, builder);

		Button positiveButton = (Button) parentView.findViewById(R.id.widget_btn_dialog_ios_positive);
		initPositiveButton(positiveButton, builder);

		dialog.setCanceledOnTouchOutside(builder.isCanceledOnTouchOutside());
		initDialog(dialog);
		dialog.show();
	}

	/**
	 * 初始化 基础 数据
	 *
	 * @param builder
	 */
	protected void initBuilder(Builder builder)
	{

	}

	/**
	 * 设置标题
	 *
	 * @param textView
	 * @param builder
	 */
	protected void initTitleTextView(TextView textView, Builder builder)
	{
		textView.setText(builder.getTitle());
	}

	/**
	 * 设置内容
	 *
	 * @param textView
	 * @param builder
	 */
	protected void initMessageTextView(TextView textView, Builder builder)
	{
		textView.setText(builder.getMessage());
	}

	/**
	 * 消极按钮
	 *
	 * @param negativeButton
	 * @param builder
	 */
	protected void initNegativeButton(final Button negativeButton, final Builder builder)
	{
		negativeButton.setText(builder.getNegativeText());
		negativeButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != builder.getNegativeListener())
				{
					negativeButton.setOnClickListener(builder.getNegativeListener());
					dialog.dismiss();
				}
				else
				{
					dialog.dismiss();
				}
			}
		});
	}

	/**
	 * 积极按钮
	 *
	 * @param positiveButton
	 * @param builder
	 */
	protected void initPositiveButton(Button positiveButton, final Builder builder)
	{
		positiveButton.setText(builder.getPositiveText());
		positiveButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != builder.getPositiveListener())
				{
					builder.getPositiveListener().onClick(v);
					dialog.dismiss();
				}
				else
				{
					dialog.dismiss();
				}
			}
		});
	}

	/**
	 * 初始化 dialog
	 *
	 * @param dialog
	 */
	protected void initDialog(Dialog dialog)
	{
	}

	/**
	 * 这里不可能定义 所有 性质
	 * 仅仅定义最简单的数据，作为摄入值 使用
	 */
	public static class Builder
	{
		private String title = "标题";

		private String message = "消息内容";

		private String positiveText = "确定";

		private String negativeText = "取消";

		private int backgroundResourceId = R.drawable.widget_dialog_ios_bg;

		private View.OnClickListener positiveListener = null;

		private View.OnClickListener negativeListener = null;

		private boolean isCanceledOnTouchOutside = false; // 默认 不打开
		
		public String getTitle()
		{
			return title;
		}

		public void setTitle(String title)
		{
			this.title = title;
		}

		public String getMessage()
		{
			return message;
		}

		public void setMessage(String message)
		{
			this.message = message;
		}

		public String getPositiveText()
		{
			return positiveText;
		}

		public void setPositiveText(String positiveText)
		{
			this.positiveText = positiveText;
		}

		public String getNegativeText()
		{
			return negativeText;
		}

		public void setNegativeText(String negativeText)
		{
			this.negativeText = negativeText;
		}

		public int getBackgroundResourceId()
		{
			return backgroundResourceId;
		}

		public void setBackgroundResourceId(int backgroundResourceId)
		{
			this.backgroundResourceId = backgroundResourceId;
		}

		public View.OnClickListener getPositiveListener()
		{
			return positiveListener;
		}

		public void setPositiveListener(View.OnClickListener positiveListener)
		{
			this.positiveListener = positiveListener;
		}

		public View.OnClickListener getNegativeListener()
		{
			return negativeListener;
		}

		public void setNegativeListener(View.OnClickListener negativeListener)
		{
			this.negativeListener = negativeListener;
		}

		public boolean isCanceledOnTouchOutside()
		{
			return isCanceledOnTouchOutside;
		}

		public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside)
		{
			isCanceledOnTouchOutside = canceledOnTouchOutside;
		}
	}

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 提供重写的数据 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

	/**
	 * 最外层资源文件
	 *
	 * @return
	 */
	protected int getLayoutResourceId()
	{
		return R.layout.widget_dialog_ios;
	}
}
