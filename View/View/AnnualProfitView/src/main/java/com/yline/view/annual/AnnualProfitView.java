package com.yline.view.annual;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yline.utils.UIScreenUtil;

import java.util.Locale;

/**
 * 七日年化，自定义View
 *
 * @author linjiang@kjtpay.com  2018/9/25 -- 20:46
 */
public class AnnualProfitView extends View {
	public AnnualProfitView(Context context) {
		this(context, null);
	}
	
	public AnnualProfitView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public AnnualProfitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		initBgNet();
		initCoordinate();
		initCoordinateText();
		initContent();
		initPoint();
	}
	
	/* ---------------------------- 设置参数，并更新UI -------------------------- */
	private final static int AXIS_Y_SIZE = 5; // Y轴文字个数
	
	private float yValueMax = 6.0f; // Y轴上限
	private float[] yValueArray; // y的值
	private String[] axisYValueArray = {"1.235", "2.043", "2.866", "3.729", "4.125"}; // Y轴 标注
	private String[] axisXValueArray = {"12-30", "12-31", "01-01", "01-02", "01-03", "01-04", "01-05"}; // X轴 标注
	
	/**
	 * 设置参数，并更新UI
	 *
	 * @param yValueArray      每个点的值
	 * @param xCoordinateArray X坐标的值
	 */
	public void setData(float[] yValueArray, String[] xCoordinateArray) {
		if (null != yValueArray && yValueArray.length > 0) {
			this.yValueArray = yValueArray; // 值的队列
			
			this.yValueMax = getMaxValue(yValueArray); // 计算Y的上限
			
			// 更新Y轴 坐标提示文字
			float unit = yValueMax / (AXIS_Y_SIZE + 1);
			for (int i = 0; i < AXIS_Y_SIZE; i++) {
				this.axisYValueArray[i] = String.format(Locale.CHINA, "%1.3f", (i + 1) * unit);
			}
			
			if (null != xCoordinateArray && xCoordinateArray.length == yValueArray.length) {
				this.axisXValueArray = xCoordinateArray;
			}
		}
		invalidate();
	}
	
	/**
	 * 一般情况是最大值的 3/2
	 *
	 * @param yValueArray 值的队列
	 * @return 最大值的 5/4
	 */
	private float getMaxValue(float[] yValueArray) {
		float temp = yValueArray[0];
		for (int i = 1; i < yValueArray.length; i++) {
			temp = (temp > yValueArray[i] ? temp : yValueArray[i]);
		}
		
		return temp * 3 / 2.0f;
	}
	
	private int touchIndex = -1; // 当前按住的位置
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x = event.getX();
				touchIndex = getTouchIndex(xContentPixelArray, x);
				break;
			//				滑动效果，暂时不要
			//			case MotionEvent.ACTION_MOVE:
			//				x = event.getX();
			//				touchIndex = getTouchIndex(xContentPixelArray, x);
			//				break;
			default:
				break;
		}
		postInvalidate();
		return true; // 默认消费掉，放置点透事件发生
		// return super.onTouchEvent(event);
	}
	
	/**
	 * 依据点击的位置，动态计算当前按下的 xArray的第几个
	 *
	 * @param xValueArray xArray的像素点值
	 * @param xValue      点击的像素点
	 * @return 第几个
	 */
	private int getTouchIndex(int[] xValueArray, float xValue) {
		if (null != xValueArray && xValueArray.length > 1) {
			int xSpace = xValueArray[1] - xValueArray[0];
			int relateValue = (int) xValue - xValueArray[0] + xSpace / 2;
			return relateValue / xSpace;
		}
		return -1;
	}
	
	private Rect charRect = new Rect(); // 中心坐标，以内的位置
	private Rect charOffsetRect = new Rect(); // 中心坐标，往两侧偏移的距离
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		
		// 主体定位坐标
		int offsetLeft = coordinateTextYRect.width() + 10; // 左侧间距，10px
		int offsetBottom = coordinateTextXRect.height() + 13; // 底部间距，13px
		charOffsetRect.set(offsetLeft, 0, 0, offsetBottom);
		
		// 图标X方向，开始与结束，位置
		int startX = getPaddingLeft() + charOffsetRect.left, endX = width - getPaddingRight() - charOffsetRect.right;
		// 图标Y方向，开始与结束，位置
		int startY = getPaddingTop() + charOffsetRect.top, endY = height - getPaddingBottom() - charOffsetRect.bottom;
		charRect.set(startX, startY, endX, endY);
		
		int xSpace = (endX - startX) / (axisXValueArray.length - 1); // x坐标之间的间隔（像素）
		int ySpace = (endY - startY) / (axisYValueArray.length + 1); // y坐标之间的间隔（像素）
		
		drawBgNet(canvas, charRect, ySpace, axisYValueArray.length);
		if (null != yValueArray) {
			drawContent(canvas, charRect, xSpace, yValueArray, yValueMax);
			
			int index = touchIndex > -1 ? touchIndex : yValueArray.length - 1; // 如果小于最小值，默认展示最后一个
			index = Math.min(yValueArray.length - 1, index); // 不允许超过最大值
			drawPoint(canvas, xContentPixelArray[index], yContentPixelArray[index], yValueArray[index]);
		}
		drawCoordinate(canvas, charRect);
		drawCoordinateText(canvas, charRect, charOffsetRect, xSpace, ySpace, axisXValueArray, axisYValueArray);
	}
	
	/* ---------------------------- 绘制当前点（点 + 提示文字） -------------------------- */
	// 绘制点
	private final static int POINT_RADIUS = 14; // 点的半径,像素
	private Bitmap pointBitmap;
	private Paint pointPaint = new Paint();
	private Rect pointRect = new Rect();
	
	// 绘制点对应的文字
	private Rect pointTextRect = new Rect();
	private Rect pointTextOffsetRect = new Rect();
	private RectF pointTextBgRect = new RectF();
	
	private void initPoint() {
		pointBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.view_annual_profit_point);
		
		pointPaint.setAntiAlias(true);
		pointPaint.setTextSize(UIScreenUtil.dp2px(getContext(), 12));
		
		final String formatPoint = "0.0000";
		pointPaint.getTextBounds(formatPoint, 0, formatPoint.length(), pointTextRect);
		
		pointTextOffsetRect.set(15, 8, 15, 10);
	}
	
	/**
	 * @param xCenter 点的x 像素点
	 * @param yCenter 点的y 像素点
	 * @param yValue  当前内容
	 */
	private void drawPoint(Canvas canvas, int xCenter, int yCenter, float yValue) {
		// 点
		pointRect.set(xCenter - POINT_RADIUS, yCenter - POINT_RADIUS,
				xCenter + POINT_RADIUS, yCenter + POINT_RADIUS);
		canvas.drawBitmap(pointBitmap, null, pointRect, pointPaint);
		
		drawPointText(canvas, charRect, xCenter, yCenter, yValue);
	}
	
	private void drawPointText(Canvas canvas, Rect charRect, int xCenter, int yCenter, float yValue) {
		// 背景
		pointPaint.setColor(ContextCompat.getColor(getContext(), R.color.design_red));
		
		int bgWidth = (pointTextRect.width() + pointTextOffsetRect.left + pointTextOffsetRect.right);
		int bgHeight = (pointTextRect.height() + pointTextOffsetRect.top + pointTextOffsetRect.bottom);
		int bottom = yCenter - 2 * POINT_RADIUS;
		pointTextBgRect.set(xCenter - bgWidth / 2, bottom - bgHeight, xCenter + bgWidth / 2, bottom);
		
		// 处理超越边境
		if (pointTextBgRect.left < charRect.left) {
			pointTextBgRect.offset(bgWidth / 2, 0);
		} else if (pointTextBgRect.right > charRect.right) {
			pointTextBgRect.offset(-bgWidth / 2 + pointTextOffsetRect.right, 0);
		}
		
		canvas.drawRoundRect(pointTextBgRect, bgHeight, bgHeight, pointPaint);
		
		// 点对应的文字
		pointPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
		
		String text = String.format(Locale.CHINA, "%1.4f", yValue);
		canvas.drawText(text, pointTextBgRect.left + pointTextOffsetRect.left,
				pointTextBgRect.bottom - pointTextOffsetRect.bottom, pointPaint);
	}
	
	/* ---------------------------- 绘制坐标轴(X轴 + Y轴) -------------------------- */
	private static final int ARROW_HALF_SIDE = 9; // 三角形边长的一半
	
	private Path coordinateYArrowPath = new Path(); // Y轴坐标轴的，三角形，箭头
	private Paint coordinatePaint = new Paint();
	
	private void initCoordinate() {
		coordinatePaint.setAntiAlias(true);
		coordinatePaint.setColor(ContextCompat.getColor(getContext(), R.color.grey_c4));
		coordinatePaint.setStrokeWidth(1);
	}
	
	private void drawCoordinate(Canvas canvas, Rect charRect) {
		// Y轴
		canvas.drawLine(charRect.left, charRect.top, charRect.left, charRect.bottom, coordinatePaint);
		
		// Y轴箭头
		coordinateYArrowPath.moveTo(charRect.left, charRect.top);
		coordinateYArrowPath.lineTo(charRect.left + ARROW_HALF_SIDE, charRect.top + (int) (1.5f * ARROW_HALF_SIDE));
		coordinateYArrowPath.lineTo(charRect.left - ARROW_HALF_SIDE, charRect.top + (int) (1.5f * ARROW_HALF_SIDE));
		coordinateYArrowPath.close();
		canvas.drawPath(coordinateYArrowPath, coordinatePaint);
		
		// X轴
		canvas.drawLine(charRect.left, charRect.bottom, charRect.right, charRect.bottom, coordinatePaint);
	}
	
	/* ---------------------------- 绘制背景的网格 -------------------------- */
	private Paint bgNetPaint = new Paint();
	
	private void initBgNet() {
		bgNetPaint.setAntiAlias(true);
		bgNetPaint.setStrokeWidth(1);
		bgNetPaint.setColor(ContextCompat.getColor(getContext(), R.color.grey_ee));
	}
	
	private void drawBgNet(Canvas canvas, Rect charRect, int ySpace, int yValueLength) {
		// 横线
		for (int i = 0; i < yValueLength; i++) {
			int textYCenter = charRect.top + ySpace * (i + 1);
			canvas.drawLine(charRect.left, textYCenter, charRect.right, textYCenter, bgNetPaint);
		}
	}
	
	/* ---------------------------- 绘制提示文字（X轴 + Y轴） -------------------------- */
	private Paint coordinateTextPaint = new Paint(); // 绘制文字
	
	private Rect coordinateTextXRect = new Rect();
	private Rect coordinateTextYRect = new Rect();
	
	private void initCoordinateText() {
		coordinateTextPaint.setAntiAlias(true);
		coordinateTextPaint.setTextSize(UIScreenUtil.dp2px(getContext(), 12));
		coordinateTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.grey_c4));
		
		final String formatY = "0.000";
		coordinateTextPaint.getTextBounds(formatY, 0, formatY.length(), coordinateTextYRect);
		
		final String formatX = "00-00";
		coordinateTextPaint.getTextBounds(formatX, 0, formatX.length(), coordinateTextXRect);
	}
	
	private void drawCoordinateText(Canvas canvas, Rect charRect, Rect charOffsetRect, int xSpace, int ySpace, String[] xTextArray, String[] yTextArray) {
		// X轴提示文字
		int textEnd = charRect.bottom + charOffsetRect.bottom;
		for (int i = 0; i < xTextArray.length; i++) {
			String textXValue = xTextArray[i];
			
			int textXCenter = charRect.left + xSpace * i;
			canvas.drawText(textXValue, textXCenter - coordinateTextXRect.width() / 2, textEnd, coordinateTextPaint);
		}
		
		// Y轴提示文字
		int textLeft = charRect.left - charOffsetRect.left;
		for (int i = 0; i < yTextArray.length; i++) {
			String textYValue = yTextArray[i];
			
			int textYCenter = charRect.top + ySpace * (yTextArray.length - i);
			canvas.drawText(textYValue, textLeft, textYCenter + coordinateTextYRect.height() / 2, coordinateTextPaint);
		}
	}
	
	/* ---------------------------- 绘制中间内容（阴影 + 折线） -------------------------- */
	private Paint contentPaint = new Paint(); // 内容绘制
	private Path contentPath = new Path(); // 内容路径
	
	// 搭配起来就是每个点的位置了
	private int[] xContentPixelArray; // X轴，轴点，像素位置
	private int[] yContentPixelArray; // Y轴，值，像素位置
	
	private void initContent() {
		contentPaint.setAntiAlias(true);
		contentPaint.setStyle(Paint.Style.FILL);
		contentPaint.setStrokeWidth(4);
		contentPaint.setStrokeCap(Paint.Cap.ROUND);
		contentPaint.setStrokeJoin(Paint.Join.ROUND);
	}
	
	/**
	 * @param yValueArray y坐标的值 队列
	 * @param yValueMax   y坐标 最大值
	 */
	private void drawContent(Canvas canvas, Rect charRect, int xSpace, float[] yValueArray, float yValueMax) {
		updatePixelArray(charRect, xSpace, yValueArray, yValueMax);
		
		// 绘制阴影
		contentPaint.setColor(ContextCompat.getColor(getContext(), R.color.design_red_33));
		
		contentPath.moveTo(charRect.left, charRect.bottom);
		for (int i = 0; i < yContentPixelArray.length; i++) {
			contentPath.lineTo(xContentPixelArray[i], yContentPixelArray[i]);
		}
		contentPath.lineTo(charRect.right, charRect.bottom);
		contentPath.close();
		canvas.drawPath(contentPath, contentPaint);
		
		// 绘制 折现
		contentPaint.setColor(ContextCompat.getColor(getContext(), R.color.design_red));
		
		int lastX = 0, lastY = 0;
		for (int i = 0; i < yContentPixelArray.length; i++) {
			if (i != 0) {
				canvas.drawLine(lastX, lastY, xContentPixelArray[i], yContentPixelArray[i], contentPaint);
			}
			lastX = xContentPixelArray[i];
			lastY = yContentPixelArray[i];
		}
	}
	
	private void updatePixelArray(Rect charRect, int xSpace, float[] yValueArray, float yValueMax) {
		xContentPixelArray = new int[yValueArray.length];
		yContentPixelArray = new int[yValueArray.length];
		
		float unit = charRect.height() / yValueMax; // 每个点的收益率，对应的像素个数
		for (int i = 0; i < yValueArray.length; i++) {
			// 计算坐标
			int textXCenter;
			if (i == yValueArray.length - 1) { // 解决计算误差导致的倾斜
				textXCenter = charRect.right;
			} else {
				textXCenter = charRect.left + xSpace * i;
			}
			
			int valueY = (int) ((yValueMax - yValueArray[i]) * unit);
			
			xContentPixelArray[i] = textXCenter;
			yContentPixelArray[i] = valueY;
		}
	}
}
