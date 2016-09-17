package com.view.waveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 通过改变相位,达到改变波纹的效果
 * Created by yline on 2016/9/17.
 */
public class WavePhaseView extends View
{
	//TODO: 常量
	private static final String TAG = "WavePhaseView";

	//TODO: 保存原始值,初始化后不再变化,或仅由外界设置而间接变化

	/** 直径 */
	private int tempDiameter = -1;

	/** 用于保存波纹的总y值,最大值 */
	private float[] tempMaxY;

	/** 用于保存波纹的总y值,最大值 */
	private float[] tempMinY;

	/** 绘制用的画笔 */
	private Paint tempWavePaint;

	//TODO: 变化的值,一直内部变化

	/** 淡色数组,波纹值 */
	private float[] tempResetOneY;

	/** 深色数组,波纹值 */
	private float[] tempResetTwoY;

	/** 绘制线的值 */
	private float tempStopY;

	/** 水波纹的波动高度 */
	private float tempWavePercent = 1.0f;

	/** 打印时间 */
	private long tempTime;

	/** 偏移量 */
	private float tempOffsetOneX, tempOffsetTwoX;

	//TODO: 变化的值,直接由外界设置,内部不直接变化

	/** 颜色 */
	private int waveColor = 0x994fedec;

	/** 水百分比 = 水高度/直径 */
	private float waterPercent = 25.0f / 50;

	/** 波纹高度 */
	private int waveMaxHeight = 30;

	/** 一个直径有多少个周期 */
	private float waveCycle = 1.0f;

	/** 移动速度,单位:直径/秒 */
	private float waveSpeedOne = 1.0f, waveSpeedTwo = 1.33f;

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
	 * 基础水波纹高度
	 * @param percent 百分比
	 */
	public void setWaterPercent(float percent)
	{
		this.waterPercent = percent;
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
	 * 设置 一个直径有多少个周期
	 * @param cycleNumber
	 */
	public void setCycleNumber(float cycleNumber)
	{
		this.waveCycle = cycleNumber;
	}

	/**
	 * 设置波纹速度
	 * @param speedOne 直径/秒
	 * @param speedTwo 直径/秒
	 */
	public void setWaveSpeed(float speedOne, float speedTwo)
	{
		this.waveSpeedOne = speedOne;
		this.waveSpeedTwo = speedTwo;
	}

	public WavePhaseView(Context context, AttributeSet attrs)
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
		tempWavePaint.setStyle(Paint.Style.FILL);
		// 设置画笔颜色
		tempWavePaint.setColor(waveColor);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);

		tempDiameter = Math.min(right - left, bottom - top);

		initVariable(tempDiameter);
	}

	private void initVariable(int diameter)
	{
		tempMaxY = new float[diameter];
		tempMinY = new float[diameter];
		tempResetOneY = new float[diameter];
		tempResetTwoY = new float[diameter];

		for (int i = 0; i < diameter; i++)
		{
			tempMinY[i] = getMinY(diameter / 2, i);
			tempMaxY[i] = diameter - tempMinY[i];
		}
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

		// 重置Y值
		resetY(tempDiameter, waveCycle, tempOffsetOneX, tempOffsetTwoX);

		// 重新绘制
		drawLines(canvas, tempDiameter, waterPercent, tempWavePercent);

		// LogUtil.v(MainApplication.TAG + " -> durateTime = " + (System.currentTimeMillis() - tempTime) + ", time = " + tempTime);
		// 重置X值
		resetX(waveSpeedOne, waveSpeedTwo, System.currentTimeMillis() - tempTime, waveCycle);

		postInvalidate();
	}

	/**
	 * 计算当前的偏移量
	 * @param speedOne 浅色波纹
	 * @param speedTwo 深色波纹
	 * @param diffTime 绘制一次的时间差
	 * @param cycle    一个直径多少个周期
	 */
	private void resetX(float speedOne, float speedTwo, float diffTime, float cycle)
	{
		tempOffsetOneX = tempOffsetOneX + (float) ((speedOne * diffTime * 2 * Math.PI / cycle / 1000) % (2 * Math.PI));
		tempOffsetTwoX = tempOffsetTwoX + (float) ((speedTwo * diffTime * 2 * Math.PI / cycle / 1000) % (2 * Math.PI));
	}

	/**
	 * 重置Y值
	 * @param diameter   圆环的直径
	 * @param cycle      一个直径有多少个周期
	 * @param offsetOneX 淡色波纹的偏移量
	 * @param offsetTwoX 深色波纹的偏移量
	 */
	private void resetY(int diameter, float cycle, float offsetOneX, float offsetTwoX)
	{
		for (int i = 0; i < diameter; i++)
		{
			tempResetOneY[i] = waveHeightFunction(waveMaxHeight, i, (int) (diameter / cycle), offsetOneX);
			tempResetTwoY[i] = waveHeightFunction(waveMaxHeight, i, (int) (diameter / cycle), offsetTwoX);
		}
	}

	/**
	 * 波纹的正弦函数
	 * @param maxHeight   振幅
	 * @param x           X值
	 * @param cycleNumber 一个周期的X值(如果是直径的一半,那么一个直径就有两个波纹了)
	 * @param offset      0 - 2*PI
	 * @return
	 */
	private float waveHeightFunction(int maxHeight, int x, int cycleNumber, float offset)
	{
		return (float) (maxHeight * Math.sin(x * (2 * Math.PI / cycleNumber) + offset));
	}

	/**
	 * 从下往上画;重新绘制
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
}
