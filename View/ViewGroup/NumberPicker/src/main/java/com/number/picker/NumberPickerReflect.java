package com.number.picker;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.yline.log.LogFileUtil;

import java.lang.reflect.Field;

/**
 * 利用反射的原理，对private值进行赋值
 * 该类，针对NumberPicker
 *
 * @author yline 2017/4/27 -- 10:46
 * @version 1.0.0
 */
public class NumberPickerReflect
{
	private Drawable divideDrawable = null;

	private int textColor = 0xff101010;

	public NumberPickerReflect setDivideDrawable(Drawable divideDrawable)
	{
		this.divideDrawable = divideDrawable;
		return this;
	}

	public NumberPickerReflect setTextColor(int textColor)
	{
		this.textColor = textColor;
		return this;
	}

	public void commit(NumberPicker numberPicker)
	{
		Field[] pickerFields = NumberPicker.class.getDeclaredFields();

		String fieldName;
		for (Field pf : pickerFields)
		{
			try
			{
				fieldName = pf.getName();
				if (fieldName.equals("mSelectionDivider"))
				{
					pf.setAccessible(true);
					pf.set(numberPicker, divideDrawable);
				}
				else if (fieldName.equals("mInputText"))
				{
					pf.setAccessible(true);

					EditText editText = (EditText) pf.get(numberPicker);
					editText.setTextColor(textColor);
				}
				else if (fieldName.equals("mSelectorWheelPaint"))
				{
					pf.setAccessible(true);

					Paint paint = (Paint) pf.get(numberPicker);
					paint.setColor(textColor);
				}
			} catch (IllegalArgumentException e)
			{
				LogFileUtil.e("reflect", "IllegalArgumentException", e);
			} catch (Resources.NotFoundException e)
			{
				LogFileUtil.e("reflect", "NotFoundException", e);
			} catch (IllegalAccessException e)
			{
				LogFileUtil.e("reflect", "IllegalAccessException", e);
			}
		}
	}
}
