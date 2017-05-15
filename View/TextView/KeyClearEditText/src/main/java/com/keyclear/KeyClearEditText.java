package com.keyclear;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class KeyClearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher
{
	// 一键删除的按钮
	private Drawable mClearDrawable;

	// 获得主题的颜色
	private int colorAccent;

	// 控件是否有焦点
	private boolean hasFocus;

	public KeyClearEditText(Context context)
	{
		this(context, null);
	}

	public KeyClearEditText(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.editTextStyle);
	}

	public KeyClearEditText(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorAccent});
		colorAccent = array.getColor(0, Color.MAGENTA); // 0xFF00FF
		array.recycle();
		initClearDrawable(context);
	}

	private void initClearDrawable(Context context)
	{
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null)
		{
			mClearDrawable = ContextCompat.getDrawable(context, R.drawable.widget_et_key_clear);
		}

		// 设置删除按钮的颜色和TextColor的颜色一致
		DrawableCompat.setTint(mClearDrawable, colorAccent);
		// 设置Drawable的宽高和TextSize的大小一致
		mClearDrawable.setBounds(0, 0, (int) getTextSize(), (int) getTextSize());

		setClearIconVisible(true);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 *
	 * @param visible
	 */
	private void setClearIconVisible(boolean visible)
	{
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mClearDrawable != null && event.getAction() == MotionEvent.ACTION_UP)
		{
			int x = (int) event.getX();
			// 判断触摸点是否在水平范围内
			boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight()))
					&& (x < (getWidth() - getPaddingRight()));
			// 获取删除图标的边界，返回一个Rect对象
			Rect rect = mClearDrawable.getBounds();
			// 获取删除图标的高度
			int height = rect.height();
			int y = (int) event.getY();
			// 计算图标底部到控件底部的距离
			int distance = (getHeight() - height) / 2;
			// 判断触摸点是否在竖直范围内(可能会有点误差)
			// 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
			boolean isInnerHeight = (y > distance) && (y < (distance + height));
			if (isInnerHeight && isInnerWidth)
			{
				this.setText("");
				Toast.makeText(getContext(), "一键清除", Toast.LENGTH_SHORT).show();//为了看清效果，测试
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
		this.hasFocus = hasFocus;
		if (hasFocus)
		{
			setClearIconVisible(getText().length() > 0);
		}
		else
		{
			setClearIconVisible(false);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
	{
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		if (hasFocus)
		{
			setClearIconVisible(text.length() > 0);
		}
	}

	@Override
	public void afterTextChanged(Editable s)
	{

	}
}
