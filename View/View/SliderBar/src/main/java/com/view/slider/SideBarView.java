package com.view.slider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SideBarView extends View
{
	private boolean isBackgroundColorable = false;

	private static final int BACKGROUND_COLOR = 0x40000000;

	private static final int LETTER_CHOOSED = 0xff3399ff;

	private static final int LETTER_NORMAL = Color.BLACK;

	private int chooseLetter = -1;

	private final Paint paint = new Paint();

	private OnLetterTouchedListener letterTouchedListener;

	/** 字母 */
	public static String[] letters = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	public void setOnLetterTouchedListener(OnLetterTouchedListener letterTouchedListener)
	{
		this.letterTouchedListener = letterTouchedListener;
	}

	public SideBarView(Context context)
	{
		this(context, null);
	}

	public SideBarView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public SideBarView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setAntiAlias(true);
		paint.setTextSize(20);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (isBackgroundColorable)
		{
			canvas.drawColor(BACKGROUND_COLOR);
		}

		for (int i = 0; i < letters.length; i++)
		{
			if (i == chooseLetter)
			{
				paint.setColor(LETTER_CHOOSED);
				paint.setFakeBoldText(true);
			}
			else
			{
				paint.setColor(LETTER_NORMAL);
				paint.setFakeBoldText(false);
			}

			float xPosition = getWidth() / 2 - paint.measureText(letters[i]) / 2;
			float yPosition = (getHeight() / letters.length) * (i + 1);

			canvas.drawText(letters[i], xPosition, yPosition, paint);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		final int oldChoose = chooseLetter;
		final int eventPosition = (int) (event.getY() / getHeight() * letters.length);

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				isBackgroundColorable = true;
				if (eventPosition != oldChoose && eventPosition >= 0 && eventPosition < letters.length)
				{
					if (null != letterTouchedListener)
					{
						letterTouchedListener.onTouched(eventPosition, letters[eventPosition]);
						chooseLetter = eventPosition;
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (eventPosition != oldChoose && eventPosition >= 0 && eventPosition < letters.length)
				{
					if (null != letterTouchedListener)
					{
						letterTouchedListener.onTouched(eventPosition, letters[eventPosition]);
						chooseLetter = eventPosition;
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				isBackgroundColorable = false;
				chooseLetter = -1;
				invalidate();
				break;
		}

		return true;
	}

	public interface OnLetterTouchedListener
	{
		/**
		 * @param position 当前位置
		 * @param str      当前字符
		 */
		void onTouched(int position, String str);
	}
}
