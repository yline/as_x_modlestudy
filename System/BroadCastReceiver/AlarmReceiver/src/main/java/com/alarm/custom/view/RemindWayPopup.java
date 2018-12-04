package com.alarm.custom.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.alarm.R;

public class RemindWayPopup {
	private OnRemindWayListener remindWayListener;
	
	private PopupWindow mPopupWindow;
	
	public RemindWayPopup(Context context) {
		initView(context);
	}
	
	private void initView(Context context) {
		mPopupWindow = new PopupWindow(context);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
		mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		// mPopupWindow.setAnimationStyle(R.style.AnimBottom);
		
		View view = initPopupWindowView(context);
		mPopupWindow.setContentView(view);
		
		mPopupWindow.getContentView().setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mPopupWindow.setFocusable(false);
				mPopupWindow.dismiss();
				return true;
			}
		});
	}
	
	private View initPopupWindowView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.popup_way, null);
		
		view.findViewById(R.id.tv_drugway_1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != remindWayListener) {
					remindWayListener.onResult(REMIND_WAY.VIBRATE);
				}
				dismiss();
			}
		});
		
		view.findViewById(R.id.tv_drugway_2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != remindWayListener) {
					remindWayListener.onResult(REMIND_WAY.RING);
				}
				dismiss();
			}
		});
		
		return view;
	}
	
	private void dismiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}
	
	public void setOnRemindWayListener(OnRemindWayListener listener) {
		this.remindWayListener = listener;
	}
	
	public interface OnRemindWayListener {
		void onResult(REMIND_WAY way);
	}
	
	public enum REMIND_WAY {
		VIBRATE("震动"), RING("铃声");
		
		REMIND_WAY(String cn) {
			this.cn = cn;
		}
		
		/**
		 * 中文名
		 */
		private final String cn;
		
		public String getCn() {
			return cn;
		}
	}
	
	public void showPopup(View rootView) {
		// 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
		mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
	}
}
