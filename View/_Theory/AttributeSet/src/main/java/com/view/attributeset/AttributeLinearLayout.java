package com.view.attributeset;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yline.base.BaseLinearLayout;

/**
 * Created by yline on 2016/10/30.
 */
public class AttributeLinearLayout extends BaseLinearLayout
{
	private final int ICON_LEFT = 0;

	private final int ICON_RIGHT = 1;

	private final int ICON_ABOVE = 2;

	private final int ICON_BELOW = 3;

	private TextView mTextView;

	private ImageView mImageView;

	private int mRelation = ICON_LEFT;

	private int mIcon;

	private String mText = "";

	private float mTextSize;

	private int mTextColor;

	private int mSpace;

	public AttributeLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.attributeset_iconText);
		mRelation = a.getInt(R.styleable.attributeset_iconText_attributeset_relation, ICON_LEFT);        // 方向
		mIcon = a.getResourceId(R.styleable.attributeset_iconText_icon, R.mipmap.ic_launcher);    // 图像
		mText = a.getString(R.styleable.attributeset_iconText_text);
		mTextSize = a.getDimensionPixelSize(R.styleable.attributeset_iconText_text_size, 12);    // 文字大小
		mTextColor = a.getColor(R.styleable.attributeset_iconText_text_color, 0xffff00ff);
		mSpace = a.getDimensionPixelSize(R.styleable.attributeset_iconText_space, 5);
		a.recycle();

		mTextView = new TextView(context);
		mTextView.setText(mText);
		mTextView.setTextSize(mTextSize);
		mTextView.setTextColor(mTextColor);

		mImageView = new ImageView(context);
		setLinear(mImageView, 160, 160);
		mImageView.setImageResource(mIcon);

		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		int orientation = HORIZONTAL;
		int textViewIndex = 0;
		switch (mRelation)
		{
			case ICON_ABOVE:
				orientation = VERTICAL;
				bottom = mSpace;
				textViewIndex = 1;
				break;
			case ICON_BELOW:
				orientation = VERTICAL;
				top = mSpace;
				break;
			case ICON_LEFT:
				right = mSpace;
				textViewIndex = 1;
				break;
			case ICON_RIGHT:
				left = mSpace;
				break;
		}
		this.setOrientation(orientation);
		this.addView(mImageView);
		mImageView.setPadding(left, top, right, bottom);
		this.addView(mTextView, textViewIndex);
	}

	private void setLinear(View view, int width, int height)
	{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			lp = new LinearLayout.LayoutParams(1, 1);
		}

		lp.width = width;
		lp.height = height;

		view.setLayoutParams(lp);
	}
}
