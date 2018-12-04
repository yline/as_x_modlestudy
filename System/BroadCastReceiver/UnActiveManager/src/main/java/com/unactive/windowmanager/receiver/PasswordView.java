package com.unactive.windowmanager.receiver;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.system.broadcast.receiver.unactivemanager.R;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;

/**
 * 取消激活,密码保护时,弹出的输入密码界面
 */
public class PasswordView extends LinearLayout {
	private static final String TAG = "PasswordView";
	
	private final static int PASSWORD_LENGTH = 10; // 最长字符长度
	
	private KeyNumberBroadView mKeyNumberBroadView;
	
	private TextView mTvShow, mTvSecure;
	
	// 用户 输入的密码
	private String mTempResult = "";
	
	private String mTempResultBlack = "";
	
	private ArrayList<Integer> mTempResultBlackList;
	
	/**
	 * 输入结束,监听器
	 */
	private InputFinishListener mInputFinishListener;
	
	/**
	 * 输入内容为空,监听器
	 */
	private InputEmptyListener mInputEmptyListener;
	
	/**
	 * 加密显示 开关 true(密文)
	 */
	private boolean mIsSecure = true;
	
	public PasswordView(Context context) {
		this(context, null);
	}
	
	public PasswordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView(context);
		initData();
	}
	
	/**
	 * 初始化 控件
	 */
	private void initView(Context context) {
		LogFileUtil.v(TAG, "initView");
		
		// findViewById
		View containView = LayoutInflater.from(getContext()).inflate(R.layout.view_password, null);
		
		mTvShow = (TextView) containView.findViewById(R.id.tv_show);
		mTvSecure = (TextView) containView.findViewById(R.id.tv_secure);
		
		LinearLayout ll_keyBroad = (LinearLayout) containView.findViewById(R.id.ll_keyBroad);
		mKeyNumberBroadView = new KeyNumberBroadView(context);
		ll_keyBroad.addView(mKeyNumberBroadView);
		
		initLayoutParams(context, containView);
		// 添加到该 LinearLayout
		addView(containView);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		LogFileUtil.v(TAG, "initData");
		
		mTempResultBlackList = new ArrayList<Integer>();
		
		mKeyNumberBroadView.setOnNumberClickListener(new KeyNumberBroadView.onNumberClickListener() {
			
			@Override
			public void onNumberClick(View view, String number, int randomInt) {
				LogFileUtil.v(TAG, "initData -> onNumberClick");
				
				if (mTempResult.length() < PASSWORD_LENGTH) {
					// 明文
					mTempResult += number;
					
					// 密文
					mTempResultBlackList.add(randomInt);
					for (int i = 0; i < randomInt; i++) {
						mTempResultBlack += "•";
					}
					
					// 显示  明文 or 密文
					if (mIsSecure) {
						setNumberMessage(mTempResultBlack);
					} else {
						setNumberMessage(mTempResult);
					}
				}
			}
		});
		
		mKeyNumberBroadView.setOnDeleteClickListener(new KeyNumberBroadView.onDeleteClickListener() {
			
			@Override
			public void onDeleteClick(View view) {
				LogFileUtil.v(TAG, "initData -> onDeleteClick");
				
				if (mTempResult.length() > 0) {
					// 明文
					mTempResult = mTempResult.substring(0, mTempResult.length() - 1);
					
					// 密文
					mTempResultBlack = mTempResultBlack.substring(0, mTempResultBlack.length() - mTempResultBlackList.get(mTempResult.length()));
					mTempResultBlackList.remove(mTempResult.length());
					
					// 显示  明文 or 密文
					if (mIsSecure) {
						setNumberMessage(mTempResultBlack);
					} else {
						setNumberMessage(mTempResult);
					}
					
					// 已经删除到空了
					if (TextUtils.isEmpty(mTempResult) || "".equals(mTempResult)) {
						setInputEmptyListener();
					}
				} else {
					setInputEmptyListener();
				}
			}
		});
		
		mKeyNumberBroadView.setOnSureClickListener(new KeyNumberBroadView.onSureClickListener() {
			
			@Override
			public void onSureClick(View view) {
				if (!TextUtils.isEmpty(mTempResult)) {
					setInputFinishListener(mTempResult);
					
					// 清空已经输入的内容
					mTempResult = "";
					mTempResultBlack = "";
					mTempResultBlackList.clear();
				}
			}
		});
		
		mTvSecure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mIsSecure = !mIsSecure;
				
				// 显示  明文 or 密文
				if (mIsSecure) {
					setNumberMessage(mTempResultBlack);
					mTvSecure.setBackgroundResource(R.drawable.eyes_close);
				} else {
					setNumberMessage(mTempResult);
					mTvSecure.setBackgroundResource(R.drawable.eyes_open);
				}
				
				// 为空
				if (TextUtils.isEmpty(mTempResult) || "".equals(mTempResult)) {
					setInputEmptyListener();
				}
			}
		});
	}
	
	public void setHintMessage(CharSequence text) {
		mTvShow.setTextColor(0x66666666);
		mTvShow.setText(text);
	}
	
	/**
	 * 设置为屏幕大小
	 *
	 * @param view
	 */
	private void initLayoutParams(Context context, View view) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		
		LayoutParams localLayoutParams = new LayoutParams(1, 1);
		localLayoutParams.width = (int) (outMetrics.widthPixels);
		localLayoutParams.height = (int) (outMetrics.heightPixels);
		LogFileUtil.v(TAG, "localLayoutParams.width = " + localLayoutParams.width + ",localLayoutParams.height = " + localLayoutParams.height);
		view.setLayoutParams(localLayoutParams);
	}
	
	/**
	 * 显示当前的输入信息
	 *
	 * @param text
	 */
	private void setNumberMessage(CharSequence text) {
		mTvShow.setTextColor(0xff000000);
		mTvShow.setText(text);
	}
	
	public void setOnInputEmptyListener(InputEmptyListener listener) {
		this.mInputEmptyListener = listener;
	}
	
	/**
	 * 输入字符为空时,触发(包含刚刚进入)
	 */
	private void setInputEmptyListener() {
		if (null != mInputEmptyListener) {
			mInputEmptyListener.onEmpty();
		}
	}
	
	public interface InputEmptyListener {
		/**
		 * 输入字符为空时,触发
		 * (包含空时回退)
		 */
		void onEmpty();
	}
	
	public void setOnInputFinishListener(InputFinishListener listener) {
		this.mInputFinishListener = listener;
	}
	
	/**
	 * 点击确定时,触发
	 *
	 * @param result 输入结果
	 */
	private void setInputFinishListener(String result) {
		if (null != mInputFinishListener) {
			mInputFinishListener.onResult(result);
		}
	}
	
	public interface InputFinishListener {
		/**
		 * 点击确定时,触发
		 * 空,不触发
		 *
		 * @param result 输入结果
		 */
		void onResult(String result);
	}
}
