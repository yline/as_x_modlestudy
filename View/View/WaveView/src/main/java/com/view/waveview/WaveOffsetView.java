package com.view.waveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 通过移动来达到波纹移动的效果
 */
public class WaveOffsetView extends View
{
	//TODO: 常量
	private static final String TAG = "WaveOffsetView";

	//TODO: 保存原始值,初始化后不再变化,或仅由外界设置而间接变化

	/** 直径 */
	private int tempDiameter = -1;

	/** 用于保存波纹的总y值,最大值 */
	private float[] tempMaxY;

	/** 用于保存波纹的总y值,最大值 */
	private float[] tempMinY;

	/** 原始波纹值,重置 */
	private float[] tempStartY;

	/** 绘制用的画笔 */
	private Paint tempWavePaint;

	//TODO: 变化的值,一直内部变化

	/** 绘制时,波纹的终点 */
	private float tempStopY;

	/** 淡色数组,波纹值 */
	private float[] tempResetOneY;

	/** 深色数组,波纹值 */
	private float[] tempResetTwoY;

	/** 淡色总值,X方向偏移量 */
	private int tempOneOffsetX;

	/** 深色总值,X方向偏移量 */
	private int tempTwoOffsetX;

	/** 打印时间 */
	private long tempTime;

	/** 水波纹的波动高度 */
	private float tempWavePercent = 1.0f;

	//TODO: 变化的值,直接由外界设置,内部不直接变化

	/** 颜色 */
	private int waveColor = 0x994fedec;

	/** 波纹高度 */
	private int waveMaxHeight = 30;

	/** 一个直径有多少个周期 */
	private float waveCycle = 1.0f;

	/** 水百分比 = 水高度/直径 */
	private float mWaterPercent = 25.0f / 50;

	/** 水波纹移动速度 */
	private int oneUnit = 4, twoUnit = 7;

	/** 波动的方式 */
	private WAVETYPE waveType = WAVETYPE.NORMAL;

	/**
	 * 设置波纹的颜色
	 * @param color
	 */
	public void setWaveColor(int color)
	{
		this.waveColor = color;
		this.tempWavePaint.setColor(waveColor);
	}

	/**
	 * 设置波纹的振幅
	 * @param maxHeight
	 */
	public void setWaveMaxHeight(int maxHeight)
	{
		this.waveMaxHeight = maxHeight;
	}

	/**
	 * 设置 一个直径有多少个周期;因为是移动,所以非整数可能会造成断层
	 * @param cycleNumber
	 */
	public void setCycleNumber(float cycleNumber)
	{
		this.waveCycle = cycleNumber;
		for (int i = 0; i < tempDiameter; i++)
		{
			tempStartY[i] = waveHeightFunction(waveMaxHeight, i, (int) (tempDiameter / waveCycle), 0);
		}
	}

	/**
	 * 基础水波纹高度
	 * @param percent 百分比
	 */
	public void setWaterPercent(float percent)
	{
		this.mWaterPercent = percent;
	}

	/**
	 * 设置水波纹的移动速度
	 * @param oneUnit 第一个水波纹移动速度
	 * @param twoUnit 第二个水波纹移动速度
	 */
	public void setWaveSpeed(int oneUnit, int twoUnit)
	{
		this.oneUnit = oneUnit;
		this.twoUnit = twoUnit;
	}

	/**
	 * 设置波纹的类型
	 * @param type
	 */
	public void setWaveType(WAVETYPE type)
	{
		this.waveType = type;
	}

	public WaveOffsetView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		initPaint();
	}

	private void initPaint()
	{
		// 初始绘制波纹的画笔
		tempWavePaint = new Paint();
		// 去除画笔锯齿
		tempWavePaint.setAntiAlias(true);
		// 设置风格为实线
		tempWavePaint.setStyle(Style.FILL);
		// 设置画笔颜色
		tempWavePaint.setColor(waveColor);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
		tempDiameter = Math.min(r - l, b - t);

		initVariable(tempDiameter);
	}

	private void initVariable(int diameter)
	{
		tempMaxY = new float[diameter];
		tempMinY = new float[diameter];
		tempStartY = new float[diameter];
		tempResetOneY = new float[diameter];
		tempResetTwoY = new float[diameter];

		for (int i = 0; i < diameter; i++)
		{
			tempMinY[i] = getMinY(diameter / 2, i);
			tempMaxY[i] = diameter - tempMinY[i];

			// 根据view总宽度得出所有对应的y值
			tempStartY[i] = waveHeightFunction(waveMaxHeight, i, (int) (diameter / waveCycle), 0);
		}
	}

	/**
	 * 波纹的正弦函数
	 * @param maxHeight   振幅
	 * @param x           X值
	 * @param cycleNumber 一个周期的X值(如果是直径的一半,那么一个直径就有两个波纹了)
	 * @param offset
	 * @return
	 */
	private float waveHeightFunction(int maxHeight, int x, int cycleNumber, int offset)
	{
		return (float) (maxHeight * Math.sin(x * (2 * Math.PI / cycleNumber) + offset));
	}

	/**
	 * 计算最小值
	 * @param r 半径
	 * @param x 此时X方向上位置
	 * @return
	 */
	private float getMinY(int r, int x)
	{
		return r - (float) Math.sqrt(r * r - (r - x) * (r - x));
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		tempTime = System.currentTimeMillis();

		resetPositionX(tempDiameter, oneUnit, twoUnit);
		resetPositionY(tempDiameter);
		drawLines(canvas, tempDiameter, mWaterPercent, tempWavePercent);

		if (waveType == WAVETYPE.NORMAL)
		{
			tempWavePercent = 1.0f;
		}
		else if (waveType == WAVETYPE.DEGREE)
		{
			tempWavePercent = setTempWavePercentDegree(tempOneOffsetX, tempDiameter);
		}
		else
		{
			tempWavePercent = 1.0f;
		}

		// LogUtil.v(MainApplication.TAG + " -> durateTime = " + (System.currentTimeMillis() - tempTime) + ", time = " + tempTime);

		postInvalidate();
	}

	private float setTempWavePercentDegree(int variable, int count)
	{
		return Math.abs(2 * (variable * 1.0f / count - 0.5f));
	}

	/**
	 * 计算好tempOneOffsetX,tempTwoOffsetX
	 * @param diameter 直径
	 * @param speedOne one速度
	 * @param speedTwo two速度
	 */
	private void resetPositionX(int diameter, int speedOne, int speedTwo)
	{
		tempOneOffsetX = (tempOneOffsetX + speedOne) % diameter;
		tempTwoOffsetX = (tempTwoOffsetX + speedTwo) % diameter;
	}

	/**
	 * 计算好tempResetOneY,tempResetTwoY
	 * 要求tempOneOffsetX,tempTwoOffsetX计算ok
	 * @param diameter 直径
	 */
	private void resetPositionY(int diameter)
	{
		/** 使用System.arraycopy方式重新填充第一条波纹的数据 */
		System.arraycopy(tempStartY, tempOneOffsetX, tempResetOneY, 0, diameter - tempOneOffsetX);
		System.arraycopy(tempStartY, 0, tempResetOneY, diameter - tempOneOffsetX, tempOneOffsetX);
		/** 使用System.arraycopy方式重新填充第二条波纹的数据 */
		System.arraycopy(tempStartY, tempTwoOffsetX, tempResetTwoY, 0, diameter - tempTwoOffsetX);
		System.arraycopy(tempStartY, 0, tempResetTwoY, diameter - tempTwoOffsetX, tempTwoOffsetX);
	}

	/**
	 * 从下往上画
	 * 要求tempResetOneY[i]、tempResetTwoY[i]有值
	 * @param canvas       画布
	 * @param diameter     直径
	 * @param waterPercent 水高的百分比
	 */
	private void drawLines(Canvas canvas, int diameter, float waterPercent, float wavePercent)
	{
		for (int i = 0; i < diameter; i++)
		{
			/** 淡色 */
			drawLine(canvas, tempWavePaint, i, waterPercent, diameter, wavePercent * tempResetOneY[i]);
			/** 深色 */
			drawLine(canvas, tempWavePaint, i, waterPercent, diameter, wavePercent * tempResetTwoY[i]);
		}
	}

	/**
	 * 画一条线
	 * @param canvas   画布
	 * @param paint    画笔
	 * @param i        X方向上第几个
	 * @param percent  当前水高的百分比
	 * @param diameter 直径
	 * @param waveY    水波高度
	 */
	private void drawLine(Canvas canvas, Paint paint, int i, float percent, int diameter, float waveY)
	{
		tempStopY = diameter - (percent * diameter + waveY);
		tempStopY = tempStopY > tempMinY[i] ? tempStopY : tempMinY[i];
		tempStopY = tempStopY < tempMaxY[i] ? tempStopY : tempMaxY[i];
		canvas.drawLine(i, tempMaxY[i], i, tempStopY, paint);
	}

	public enum WAVETYPE
	{
		/** 一直波动 */
		NORMAL,
		/** 波动一次 */
		DEGREE;
	}
	/*********************************** 初始化变量 ***********************************************************/
}
