package com.alarm.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alarm.R;

import java.util.ArrayList;
import java.util.List;

public class RemindCyclePopup
{
	private OnRemindCycleListener onRemindCycleListener;

	private List<REMIND_CYCLE> remindCycleList = new ArrayList<>();

	private PopupWindow mPopupWindow;

	private Drawable navDrawable;

	private TextView tvOnce, tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday;

	private Button btnSure;

	public RemindCyclePopup(Context context)
	{
		initView(context);
	}

	private void initView(Context context)
	{
		mPopupWindow = new PopupWindow(context);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		mPopupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		//mPopupWindow.setAnimationStyle(R.style.AnimBottom);
		mPopupWindow.setContentView(initPopupWindow(context));
		mPopupWindow.getContentView().setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				mPopupWindow.setFocusable(false);
				return true;
			}
		});
	}

	private View initPopupWindow(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.popup_cycle, null);

		tvOnce = (TextView) view.findViewById(R.id.tv_once);
		tvMonday = (TextView) view.findViewById(R.id.tv_monday);
		tvTuesday = (TextView) view.findViewById(R.id.tv_tuesday);
		tvWednesday = (TextView) view.findViewById(R.id.tv_wednesday);
		tvThursday = (TextView) view.findViewById(R.id.tv_thursday);
		tvFriday = (TextView) view.findViewById(R.id.tv_friday);
		tvSaturday = (TextView) view.findViewById(R.id.tv_saturday);
		tvSunday = (TextView) view.findViewById(R.id.tv_sunday);
		btnSure = (Button) view.findViewById(R.id.btn_sure);

		navDrawable = context.getResources().getDrawable(R.drawable.cycle_check);
		navDrawable.setBounds(0, 0, navDrawable.getMinimumWidth(), navDrawable.getMinimumHeight());

		tvOnce.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != tvOnce.getCompoundDrawables()[2])
				{
					tvOnce.setCompoundDrawables(null, null, null, null);
					remindCycleList.remove(REMIND_CYCLE.ONCE);
				}
				else
				{
					tvOnce.setCompoundDrawables(null, null, navDrawable, null);
					remindCycleList.add(REMIND_CYCLE.ONCE);
				}
			}
		});

		tvMonday.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != tvMonday.getCompoundDrawables()[2])
				{
					tvMonday.setCompoundDrawables(null, null, null, null);
					remindCycleList.remove(REMIND_CYCLE.MONDAY);
				}
				else
				{
					tvMonday.setCompoundDrawables(null, null, navDrawable, null);
					remindCycleList.add(REMIND_CYCLE.MONDAY);
				}
			}
		});

		tvTuesday.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != tvTuesday.getCompoundDrawables()[2])
				{
					tvTuesday.setCompoundDrawables(null, null, null, null);
					remindCycleList.remove(REMIND_CYCLE.TUESDAY);
				}
				else
				{
					tvTuesday.setCompoundDrawables(null, null, navDrawable, null);
					remindCycleList.add(REMIND_CYCLE.TUESDAY);
				}
			}
		});

		tvWednesday.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != tvWednesday.getCompoundDrawables()[2])
				{
					tvWednesday.setCompoundDrawables(null, null, null, null);
					remindCycleList.remove(REMIND_CYCLE.WEDNESDAY);
				}
				else
				{
					tvWednesday.setCompoundDrawables(null, null, navDrawable, null);
					remindCycleList.add(REMIND_CYCLE.WEDNESDAY);
				}
			}
		});

		tvThursday.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != tvThursday.getCompoundDrawables()[2])
				{
					tvThursday.setCompoundDrawables(null, null, null, null);
					remindCycleList.remove(REMIND_CYCLE.THURSDAY);
				}
				else
				{
					tvThursday.setCompoundDrawables(null, null, navDrawable, null);
					remindCycleList.add(REMIND_CYCLE.THURSDAY);
				}
			}
		});

		tvFriday.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != tvFriday.getCompoundDrawables()[2])
				{
					tvFriday.setCompoundDrawables(null, null, null, null);
					remindCycleList.remove(REMIND_CYCLE.FRIDAY);
				}
				else
				{
					tvFriday.setCompoundDrawables(null, null, navDrawable, null);
					remindCycleList.add(REMIND_CYCLE.FRIDAY);
				}
			}
		});

		tvSaturday.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != tvSaturday.getCompoundDrawables()[2])
				{
					tvSaturday.setCompoundDrawables(null, null, null, null);
					remindCycleList.remove(REMIND_CYCLE.SATURDAY);
				}
				else
				{
					tvSaturday.setCompoundDrawables(null, null, navDrawable, null);
					remindCycleList.add(REMIND_CYCLE.SATURDAY);
				}
			}
		});

		tvSunday.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != tvSunday.getCompoundDrawables()[2])
				{
					tvSunday.setCompoundDrawables(null, null, null, null);
					remindCycleList.add(REMIND_CYCLE.SUNDAY);
				}
				else
				{
					tvSunday.setCompoundDrawables(null, null, navDrawable, null);
					remindCycleList.add(REMIND_CYCLE.SUNDAY);
				}
			}
		});

		btnSure.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != onRemindCycleListener)
				{
					onRemindCycleListener.onResult(remindCycleList);
					dismiss();
				}
			}
		});

		return view;
	}

	public void setOnRemindCycleListener(OnRemindCycleListener listener)
	{
		this.onRemindCycleListener = listener;
	}

	public interface OnRemindCycleListener
	{
		void onResult(List<REMIND_CYCLE> cycleList);
	}

	public enum REMIND_CYCLE
	{
		ONCE("只响一次"), MONDAY("周一"), TUESDAY("周二"), WEDNESDAY("周三"), THURSDAY("周四"), FRIDAY("周五"), SATURDAY("周六"), SUNDAY("周日");

		REMIND_CYCLE(String cn)
		{
			this.cn = cn;
		}

		/** 中文含义 */
		private final String cn;

		public String getCn()
		{
			return this.cn;
		}
	}

	private void dismiss()
	{
		if (null != mPopupWindow && mPopupWindow.isShowing())
		{
			mPopupWindow.dismiss();
		}
	}

	public void showPopup(View rootView)
	{
		// 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
		mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
	}
}
