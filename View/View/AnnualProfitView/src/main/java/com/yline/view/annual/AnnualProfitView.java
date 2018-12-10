package com.yline.view.annual;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yline.utils.UIScreenUtil;
import com.yline.view.annual.model.RateModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 七日年化，自定义View
 *
 * @author linjiang@kjtpay.com  2018/9/25 -- 20:46
 */
public class AnnualProfitView extends View {
	private OnPointSelectListener onPointSelectListener;
	
	public AnnualProfitView(Context context) {
		this(context, null);
	}
	
	public AnnualProfitView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public AnnualProfitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		initBgNet();
		initCoordinate();
		initCoordinateText();
		initContent();
		initPoint();
	}
	
	public void setOnPointSelectListener(OnPointSelectListener onPointSelectListener) {
		this.onPointSelectListener = onPointSelectListener;
	}
	
	/* ---------------------------- 设置参数，并更新UI -------------------------- */
	private final static int AXIS_Y_SIZE = 4; // Y轴文字个数
	
	private float yMaxLimit, yMinLimit; // Y轴上限和下限
	private List<Float> yValueList; // Y 的值
	private List<String> yAxisList; // Y轴标注
	private List<String> xAxisList; // X轴标注
	
	public void setData(List<RateModel> rateModelList) {
		if (null != rateModelList && !rateModelList.isEmpty()) {
			int length = rateModelList.size();
			
			yValueList = new ArrayList<>(length);
			xAxisList = new ArrayList<>(length);
			for (RateModel rateModel : rateModelList) {
				yValueList.add(rateModel.getRatePerWeek()); // 七日年化收益
				
				String benefitDate = rateModel.getBenefitDate(); // 1125 这种格式
				xAxisList.add(benefitDate); // X轴文字，初始化
			}
			
			updateYLimitValue(yValueList);
			yAxisList = new ArrayList<>(AXIS_Y_SIZE);
			float unit = (yMaxLimit - yMinLimit) / (AXIS_Y_SIZE + 1);
			for (int i = 0; i < AXIS_Y_SIZE; i++) {
				String yAxis = String.format(Locale.CHINA, "%1.3f", yMinLimit + (i + 1) * unit);
				yAxisList.add(yAxis);
			}
			
			contentPixelArray = null;
			invalidate();
		}
	}
	
	/**
	 * 计算图标的上下值
	 *
	 * @param yValueList 当前的内容
	 */
	private void updateYLimitValue(List<Float> yValueList) {
		float max = yValueList.get(0), min = yValueList.get(0);
		for (float yValue : yValueList) {
			max = Math.max(max, yValue);
			min = Math.min(min, yValue);
		}
		
		float diff = max - min;
		yMaxLimit = max + diff;
		yMinLimit = min - 2 * diff;
	}
	
	private int mTouchIndex = -1; // 当前按住的位置
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x = event.getX();
				
				int touchIndex = getTouchIndex(contentPixelArray, x);
				if (touchIndex >= 0 && touchIndex < yValueList.size() && mTouchIndex != touchIndex) { // touchIndex在范围内，并且有改变
					mTouchIndex = touchIndex;
					if (null != onPointSelectListener) { // 更新事件
						onPointSelectListener.onSelect(touchIndex);
					}
				}
				break;
			//				滑动效果，暂时不要
			//			case MotionEvent.ACTION_MOVE:
			//				x = event.getX();
			//				mTouchIndex = getTouchIndex(xContentPixelArray, x);
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
	 * @param contentValueArray 当前内容点，像素点值
	 * @param xValue            点击的像素点
	 * @return 第几个
	 */
	private int getTouchIndex(Point[] contentValueArray, float xValue) {
		if (null != contentValueArray && contentValueArray.length > 1) {
			int xSpace = contentValueArray[1].x - contentValueArray[0].x;
			int relateValue = (int) xValue - contentValueArray[0].x + xSpace / 2;
			return relateValue / xSpace;
		}
		return -1;
	}
	
	private final Rect charRect = new Rect(); // 中心坐标，以内的位置
	private final Rect charOffsetRect = new Rect(); // 中心坐标，往两侧偏移的距离
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		
		// 主体定位坐标
		final int offsetLeft = coordinateTextYRect.width() + 10; // 左侧间距，10px
		final int offsetBottom = coordinateTextXRect.height() + 13; // 底部间距，13px
		charOffsetRect.set(offsetLeft, 0, 0, offsetBottom);
		
		// 图标X方向，开始与结束，位置
		final int startX = getPaddingLeft() + charOffsetRect.left, endX = width - getPaddingRight() - charOffsetRect.right;
		// 图标Y方向，开始与结束，位置
		final int startY = getPaddingTop() + charOffsetRect.top, endY = height - getPaddingBottom() - charOffsetRect.bottom;
		charRect.set(startX, startY, endX, endY);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (null != xAxisList && null != yAxisList) {
			//int xSpace = (endX - startX) / (xAxisList.size() - 1); // x坐标之间的间隔（像素）
			int xSpace = charRect.width() / (xAxisList.size() - 1); // x坐标之间的间隔（像素）
			// int ySpace = (endY - startY) / (yAxisList.size() + 1); // y坐标之间的间隔（像素）
			int ySpace = charRect.height() / (yAxisList.size() + 1); // y坐标之间的间隔（像素）
			
			drawBgNet(canvas, charRect, ySpace, yAxisList.size());
			if (null != yValueList && !yValueList.isEmpty()) {
				drawContent(canvas, charRect, xSpace, yValueList, yMaxLimit, yMinLimit);
				
				int index = mTouchIndex > -1 ? mTouchIndex : yValueList.size() - 1; // 如果小于最小值，默认展示最后一个
				index = Math.min(yValueList.size() - 1, index); // 不允许超过最大值
				drawPoint(canvas, contentPixelArray[index].x, contentPixelArray[index].y, yValueList.get(index));
			}
			drawCoordinate(canvas, charRect);
			drawCoordinateText(canvas, charRect, charOffsetRect, xSpace, ySpace, xAxisList, yAxisList);
		}
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
		
		canvas.drawRoundRect(pointTextBgRect, bgHeight / 2, bgHeight / 2, pointPaint);
		
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
	
	private void drawCoordinateText(Canvas canvas, Rect charRect, Rect charOffsetRect, int xSpace, int ySpace, List<String> xTextList, List<String> yTextList) {
		// X轴提示文字
		int textEnd = charRect.bottom + charOffsetRect.bottom;
		for (int i = 0; i < xTextList.size(); i++) {
			String textXValue = xTextList.get(i);
			
			int textXCenter = charRect.left + xSpace * i;
			canvas.drawText(textXValue, textXCenter - coordinateTextXRect.width() / 2, textEnd, coordinateTextPaint);
		}
		
		// Y轴提示文字
		int textLeft = charRect.left - charOffsetRect.left;
		for (int i = 0; i < yTextList.size(); i++) {
			String textYValue = yTextList.get(i);
			
			int textYCenter = charRect.top + ySpace * (yTextList.size() - i);
			canvas.drawText(textYValue, textLeft, textYCenter + coordinateTextYRect.height() / 2, coordinateTextPaint);
		}
	}
	
	/* ---------------------------- 绘制中间内容（阴影 + 折线） -------------------------- */
	private Paint contentPaint = new Paint(); // 内容绘制
	private Path contentPath = new Path(); // 内容路径
	
	// 每个内容点的位置（像素点）
	private Point[] contentPixelArray;
	
	private void initContent() {
		contentPaint.setAntiAlias(true);
		contentPaint.setStyle(Paint.Style.FILL);
		contentPaint.setStrokeWidth(4);
		contentPaint.setStrokeCap(Paint.Cap.ROUND);
		contentPaint.setStrokeJoin(Paint.Join.ROUND);
	}
	
	/**
	 * @param yValueList y坐标的值  数组
	 * @param yMaxLimit  y坐标 最大值
	 */
	private void drawContent(Canvas canvas, Rect charRect, int xSpace, List<Float> yValueList, float yMaxLimit, float yMinLimit) {
		updatePixelArray(charRect, xSpace, yValueList, yMaxLimit, yMinLimit);
		
		// 绘制阴影
		contentPaint.setColor(ContextCompat.getColor(getContext(), R.color.design_red_33));
		
		contentPath.reset(); // 必须清楚之前的路径，不然不会清除
		contentPath.moveTo(charRect.left, charRect.bottom);
		for (int i = 0; i < contentPixelArray.length; i++) {
			contentPath.lineTo(contentPixelArray[i].x, contentPixelArray[i].y);
		}
		contentPath.lineTo(charRect.right, charRect.bottom);
		contentPath.close();
		canvas.drawPath(contentPath, contentPaint);
		
		// 绘制 折线
		contentPaint.setColor(ContextCompat.getColor(getContext(), R.color.design_red));
		
		Point lastPoint = new Point(0, 0);
		for (int i = 0; i < contentPixelArray.length; i++) {
			if (i != 0) {
				canvas.drawLine(lastPoint.x, lastPoint.y, contentPixelArray[i].x, contentPixelArray[i].y, contentPaint);
			}
			lastPoint.set(contentPixelArray[i].x, contentPixelArray[i].y);
		}
	}
	
	private void updatePixelArray(Rect charRect, int xSpace, List<Float> yValueList, float yMaxLimit, float yMinLimit) {
		if (null == contentPixelArray || contentPixelArray.length != yValueList.size()) { // 数据变化了
			int length = yValueList.size(); // 值的个数
			contentPixelArray = new Point[length];
			
			float unit = charRect.height() / (yMaxLimit - yMinLimit); // 每个点的收益率，对应的像素个数
			for (int i = 0; i < length; i++) {
				// 计算坐标
				int textXCenter;
				if (i == length - 1) { // 解决计算误差导致的倾斜
					textXCenter = charRect.right;
				} else {
					textXCenter = charRect.left + xSpace * i;
				}
				
				int yValue = (int) ((yMaxLimit - yValueList.get(i)) * unit);
				contentPixelArray[i] = new Point(textXCenter, yValue);
			}
		}
	}
	
	public interface OnPointSelectListener {
		/**
		 * 选择 point
		 *
		 * @param index 选择的位置
		 */
		void onSelect(int index);
	}
}
