package com.view.pattern.lock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import com.view.pattern.lock.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 九宫格 锁
 * 太像源码了，很怀疑是copy的系统源码
 * https://github.com/mengliguhun/LockPatternView
 *
 * @author yline 2018/9/11 -- 14:36
 */
public class LockPatternView extends View {
	private static final String SAVE_SUPER_STATE = "super_state";
	private static final String SAVE_PATTERN = "pattern";
	private static final String SAVE_DISPLAY_MODE = "display_mode";
	private static final String SAVE_INPUT_ENABLE = "input_enable";
	private static final String SAVE_STEALTH_MODE = "stealth_mode";
	private static final String SAVE_FEEDBACK_ENABLE = "feedback_enable";
	
	private static final int ASPECT_SQUARE = 0; // 正方形
	private static final int ASPECT_LOCK_WIDTH = 1; // 高度 = min（宽，高）
	private static final int ASPECT_LOCK_HEIGHT = 2; // 宽度 = min（宽，高）
	
	private static final boolean PROFILE_DRAWING = false; // 系统调试开关
	
	private static final int MILLIS_PER_CIRCLE_ANIMATING = 700; // 自动播放时，每次跳转的时间间隔
	
	private static final float HIT_FACTOR = 0.6f; // 多少范围内，算是触摸成功
	
	/* ------------------------------临时变量---------------------------------- */
	private float mSquareWidth; // 三分之一，控件宽度
	private float mSquareHeight; // 三分之一，控件高度
	
	private boolean mDrawingProfilingStarted = false; // 系统调试是否开始
	
	private float mInProgressX = -1; // 绘制中的x坐标
	private float mInProgressY = -1; // 绘制中的y坐标
	
	private long mAnimatingPeriodStart; // 动画是否开始
	
	private boolean mPatternInProgress = false; // 是否在绘制中
	
	private DisplayMode mPatternDisplayMode = DisplayMode.Normal; // 当前绘制状态
	
	private ArrayList<Cell> mPattern = new ArrayList<>(9); // 所有选中的Cell
	
	private boolean[][] mPatternDrawLookup = new boolean[3][3]; // 视图中，是否选中
	
	private final Path mCurrentPath = new Path();
	private final Rect mInvalidate = new Rect();
	
	private float lastLineX, lastLineY; // 绘制线条时，临时变量
	
	/* ------------------------------通过 attr 直接控制 或 间接控制---------------------------------- */
	private OnPatternListener mOnPatternListener; // 监听器
	
	private int mAspect;
	
	private final Matrix mCircleBgMatrix = new Matrix();
	private Bitmap mBitmapCircleBgStart; // 单个点，背景图片，未选中状态
	private Bitmap mBitmapCircleBgNormal; // 单个点，背景图片，选中状态
	private Bitmap mBitmapCircleBgError; // 单个点，背景图片，选中状态，但选错状态
	
	private int mBitmapCircleBgWidth; // 单个点，背景宽度
	private int mBitmapCircleBgHeight; // 单个点，背景高度
	
	private final Matrix mCircleMatrix = new Matrix();
	private Bitmap mBitmapCircleStart; // 单个点，未选中状态
	private Bitmap mBitmapCircleNormal; // 单个点，选中状态
	private Bitmap mBitmapCircleError; // 单个点，选中状态，但选错状态
	
	private Paint mPaint = new Paint();
	
	private int mBitmapCircleWidth; // 单个点，宽度
	private int mBitmapCircleHeight; // 单个点，高度
	
	private Paint mLinePaint = new Paint(); // 绘制路径
	private int mLineColorNormal; // 单个点，正常绘制路径颜色
	private int mLineColorError; // 单个点，异常绘制路径颜色
	private int mLineWidth = 2; // 线条宽度
	
	private boolean mIsDrawLine = true; // 是否绘制线条
	private boolean mIsDrawBgCircle = true; // 是否绘制外圆圈
	private boolean mIsLineThrough = true; // true(线条从中心 到 中心)， false(线条从边界到边界); 不通过中心点，则穿过时，必须穿过
	
	private boolean mEnableHapticFeedback = true; // 震动开关
	private boolean mInputEnabled = true; // 是否允许手势绘制
	
	private float mSquareFactor = 0.9f; // 当图片分辨率，大于屏幕上绘制的图案的分辨率时，设定间隔比率（用于调整点的大小）
	
	public LockPatternView(Context context) {
		this(context, null);
	}
	
	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setClickable(true);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LockPatternView);
		mAspect = a.getInt(R.styleable.LockPatternView_aspect, ASPECT_SQUARE);
		
		/* ------------- 背景 ------------- */
		final int circleBgStart = a.getResourceId(R.styleable.LockPatternView_img_circle_bg_start, -1);
		mBitmapCircleBgStart = BitmapFactory.decodeResource(getResources(), circleBgStart);
		
		final int circleBgNormal = a.getResourceId(R.styleable.LockPatternView_img_circle_bg_normal, R.drawable.pattern_circle_bg_normal);
		mBitmapCircleBgNormal = BitmapFactory.decodeResource(getResources(), circleBgNormal);
		
		final int circleBgError = a.getResourceId(R.styleable.LockPatternView_img_circle_bg_error, R.drawable.pattern_circle_bg_error);
		mBitmapCircleBgError = BitmapFactory.decodeResource(getResources(), circleBgError);
		
		mBitmapCircleBgWidth = Math.max(mBitmapCircleBgNormal.getWidth(), mBitmapCircleBgError.getWidth());
		mBitmapCircleBgHeight = Math.max(mBitmapCircleBgNormal.getHeight(), mBitmapCircleBgError.getHeight());
		
		/* ------------- 内容 ------------- */
		final int circleStart = a.getResourceId(R.styleable.LockPatternView_img_circle_start, R.drawable.pattern_circle_start);
		mBitmapCircleStart = BitmapFactory.decodeResource(getResources(), circleStart);
		
		final int circleNormal = a.getResourceId(R.styleable.LockPatternView_img_circle_normal, R.drawable.pattern_circle_normal);
		mBitmapCircleNormal = BitmapFactory.decodeResource(getResources(), circleNormal);
		
		final int circleError = a.getResourceId(R.styleable.LockPatternView_img_circle_error, R.drawable.pattern_circle_error);
		mBitmapCircleError = BitmapFactory.decodeResource(getResources(), circleError);
		
		mBitmapCircleWidth = Math.max(mBitmapCircleNormal.getWidth(), mBitmapCircleError.getWidth());
		mBitmapCircleHeight = Math.max(mBitmapCircleNormal.getHeight(), mBitmapCircleError.getHeight());
		
		mSquareFactor = a.getFloat(R.styleable.LockPatternView_circle_bg_square_factor, 0.9f);
		
		/* ------------- 线条 ------------- */
		mLineWidth = a.getInt(R.styleable.LockPatternView_line_width, 2); // 线条宽度
		mLineColorNormal = a.getColor(R.styleable.LockPatternView_line_color_normal, Color.YELLOW);
		mLineColorError = a.getColor(R.styleable.LockPatternView_line_color_error, Color.RED);
		
		/* ------------- 其他 ------------- */
		mIsDrawLine = a.getBoolean(R.styleable.LockPatternView_is_draw_line, true);
		mInputEnabled = a.getBoolean(R.styleable.LockPatternView_is_input_enable, true);
		mIsLineThrough = a.getBoolean(R.styleable.LockPatternView_is_line_through, true);
		mIsDrawBgCircle = a.getBoolean(R.styleable.LockPatternView_is_draw_circle_bg, true);
		mEnableHapticFeedback = a.getBoolean(R.styleable.LockPatternView_is_haptic_feedback, false);
		a.recycle();
		
		mLinePaint.setAntiAlias(true); // 抗锯齿
		mLinePaint.setDither(true); // 抗抖动
		mLinePaint.setAlpha(128); // 半透明
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setStrokeJoin(Paint.Join.ROUND);
		mLinePaint.setStrokeCap(Paint.Cap.ROUND);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		final int width = w - getPaddingLeft() - getPaddingRight();
		mSquareWidth = width / 3.0f;
		
		final int height = h - getPaddingTop() - getPaddingBottom();
		mSquareHeight = height / 3.0f;
	}
	
	@Override
	protected int getSuggestedMinimumWidth() {
		// View should be large enough to contain 3 side-by-side target bitmaps
		return 3 * mBitmapCircleBgWidth;
	}
	
	@Override
	protected int getSuggestedMinimumHeight() {
		// View should be large enough to contain 3 side-by-side target bitmaps
		return 3 * mBitmapCircleBgHeight;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int minimumWidth = getSuggestedMinimumWidth();
		final int minimumHeight = getSuggestedMinimumHeight();
		int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
		int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
		
		switch (mAspect) {
			case ASPECT_SQUARE:
				viewWidth = viewHeight = Math.min(viewWidth, viewHeight);
				break;
			case ASPECT_LOCK_WIDTH:
				viewHeight = Math.min(viewWidth, viewHeight);
				break;
			case ASPECT_LOCK_HEIGHT:
				viewWidth = Math.min(viewWidth, viewHeight);
				break;
		}
		setMeasuredDimension(viewWidth, viewHeight);
	}
	
	private int resolveMeasured(int measureSpec, int desired) {
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (MeasureSpec.getMode(measureSpec)) {
			case MeasureSpec.UNSPECIFIED:
				return desired;
			case MeasureSpec.AT_MOST:
				return Math.max(specSize, desired);
			case MeasureSpec.EXACTLY:
			default:
				return specSize;
		}
	}
	
	/* ---------------------------------Touch------------------------------------- */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mInputEnabled || !isEnabled()) {
			return false;
		}
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				handleActionDown(event);
				return true;
			case MotionEvent.ACTION_MOVE:
				handleActionMove(event);
				return true;
			case MotionEvent.ACTION_UP:
				handleActionUp(event);
				stopProfileDrawing();
				return true;
			case MotionEvent.ACTION_CANCEL:
				resetPattern();
				mPatternInProgress = false;
				notifyPatternCleared();
				stopProfileDrawing();
				return true;
		}
		return false;
	}
	
	private void handleActionDown(MotionEvent event) {
		resetPattern();
		final float x = event.getX();
		final float y = event.getY();
		final Cell hitCell = detectAndAddHit(x, y);
		if (hitCell != null) {
			mPatternInProgress = true;
			mPatternDisplayMode = DisplayMode.Normal;
			notifyPatternStarted();
		} else {
			mPatternInProgress = false;
			notifyPatternCleared();
		}
		
		if (hitCell != null) {
			final float startX = getCenterXForColumn(hitCell.column);
			final float startY = getCenterYForRow(hitCell.row);
			
			final float widthOffset = mSquareWidth / 2f;
			final float heightOffset = mSquareHeight / 2f;
			
			invalidate((int) (startX - widthOffset),
					(int) (startY - heightOffset),
					(int) (startX + widthOffset), (int) (startY + heightOffset));
		}
		
		mInProgressX = x;
		mInProgressY = y;
		
		startProfileDrawing();
	}
	
	private void handleActionMove(MotionEvent event) {
		// Handle all recent motion events so we don't skip any cells even when
		// the device
		// is busy...
		final int historySize = event.getHistorySize();
		for (int i = 0; i < historySize + 1; i++) {
			final float x = i < historySize ? event.getHistoricalX(i) : event
					.getX();
			final float y = i < historySize ? event.getHistoricalY(i) : event
					.getY();
			final int patternSizePreHitDetect = mPattern.size();
			Cell hitCell = detectAndAddHit(x, y);
			final int patternSize = mPattern.size();
			if (hitCell != null && patternSize == 1) {
				mPatternInProgress = true;
				notifyPatternStarted();
			}
			// note current x and y for rubber banding of in progress patterns
			final float dx = Math.abs(x - mInProgressX);
			final float dy = Math.abs(y - mInProgressY);
			if (dx + dy > mSquareWidth * 0.01f) {
				float oldX = mInProgressX;
				float oldY = mInProgressY;
				
				mInProgressX = x;
				mInProgressY = y;
				
				if (mPatternInProgress && patternSize > 0) {
					final ArrayList<Cell> pattern = mPattern;
					final float lineWidth = mLineWidth;
					
					final Cell lastCell = pattern.get(patternSize - 1);
					
					float startX = getCenterXForColumn(lastCell.column);
					float startY = getCenterYForRow(lastCell.row);
					
					float left;
					float top;
					float right;
					float bottom;
					
					final Rect invalidateRect = mInvalidate;
					
					if (startX < x) {
						left = startX;
						right = x;
					} else {
						left = x;
						right = startX;
					}
					
					if (startY < y) {
						top = startY;
						bottom = y;
					} else {
						top = y;
						bottom = startY;
					}
					
					// Invalidate between the pattern's last cell and the
					// current location
					invalidateRect.set((int) (left - lineWidth),
							(int) (top - lineWidth), (int) (right + lineWidth),
							(int) (bottom + lineWidth));
					
					if (startX < oldX) {
						left = startX;
						right = oldX;
					} else {
						left = oldX;
						right = startX;
					}
					
					if (startY < oldY) {
						top = startY;
						bottom = oldY;
					} else {
						top = oldY;
						bottom = startY;
					}
					
					// Invalidate between the pattern's last cell and the
					// previous location
					invalidateRect.union((int) (left - lineWidth),
							(int) (top - lineWidth), (int) (right + lineWidth),
							(int) (bottom + lineWidth));
					
					// Invalidate between the pattern's new cell and the
					// pattern's previous cell
					if (hitCell != null) {
						startX = getCenterXForColumn(hitCell.column);
						startY = getCenterYForRow(hitCell.row);
						
						if (patternSize >= 2) {
							// (re-using hitcell for old cell)
							hitCell = pattern.get(patternSize - 1
									- (patternSize - patternSizePreHitDetect));
							oldX = getCenterXForColumn(hitCell.column);
							oldY = getCenterYForRow(hitCell.row);
							
							if (startX < oldX) {
								left = startX;
								right = oldX;
							} else {
								left = oldX;
								right = startX;
							}
							
							if (startY < oldY) {
								top = startY;
								bottom = oldY;
							} else {
								top = oldY;
								bottom = startY;
							}
						} else {
							left = right = startX;
							top = bottom = startY;
						}
						
						final float widthOffset = mSquareWidth / 2f;
						final float heightOffset = mSquareHeight / 2f;
						
						invalidateRect.set((int) (left - widthOffset),
								(int) (top - heightOffset),
								(int) (right + widthOffset),
								(int) (bottom + heightOffset));
					}
					
					invalidate(invalidateRect);
				} else {
					invalidate();
				}
			}
		}
	}
	
	private void handleActionUp(MotionEvent event) {
		// report pattern detected
		if (!mPattern.isEmpty()) {
			mPatternInProgress = false;
			notifyPatternDetected();
			invalidate();
		}
	}
	
	/**
	 * Determines whether the point x, y will add a new point to the current
	 * pattern (in addition to finding the cell, also makes heuristic choices
	 * such as filling in gaps based on current pattern).
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	private Cell detectAndAddHit(float x, float y) {
		final Cell cell = checkForNewHit(x, y);
		if (cell != null) {
			// check for gaps in existing pattern
			Cell fillInGapCell = null;
			final ArrayList<Cell> pattern = mPattern;
			if (!pattern.isEmpty()) {
				final Cell lastCell = pattern.get(pattern.size() - 1);
				int dRow = cell.row - lastCell.row;
				int dColumn = cell.column - lastCell.column;
				
				int fillInRow = lastCell.row;
				int fillInColumn = lastCell.column;
				
				if (Math.abs(dRow) == 2 && Math.abs(dColumn) != 1) {
					fillInRow = lastCell.row + ((dRow > 0) ? 1 : -1);
				}
				
				if (Math.abs(dColumn) == 2 && Math.abs(dRow) != 1) {
					fillInColumn = lastCell.column + ((dColumn > 0) ? 1 : -1);
				}
				
				fillInGapCell = Cell.of(fillInRow, fillInColumn);
			}
			
			// 如果手指选择的，和上一个选择的cell，之间也有cell，则也添加
			if (fillInGapCell != null && !mPatternDrawLookup[fillInGapCell.row][fillInGapCell.column]) {
				addCellToPattern(fillInGapCell);
			}
			
			// 视图上，添加手指选择的cell
			addCellToPattern(cell);
			
			// 震动
			if (mEnableHapticFeedback) {
				performHapticFeedback(
						HapticFeedbackConstants.VIRTUAL_KEY,
						HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
								| HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
			}
			return cell;
		}
		return null;
	}
	
	private void addCellToPattern(Cell newCell) {
		mPatternDrawLookup[newCell.getRow()][newCell.getColumn()] = true;
		mPattern.add(newCell);
		notifyCellAdded();
	}
	
	/**
	 * 依据，坐标值，确定对应的Cell值
	 *
	 * @param x x坐标
	 * @param y y坐标
	 * @return Cell值
	 */
	private Cell checkForNewHit(float x, float y) {
		final int rowHit = getRowHit(y);
		if (rowHit < 0) {
			return null;
		}
		final int columnHit = getColumnHit(x);
		if (columnHit < 0) {
			return null;
		}
		
		if (mPatternDrawLookup[rowHit][columnHit]) {
			return null;
		}
		return Cell.of(rowHit, columnHit);
	}
	
	/**
	 * 依据，y坐标，确定行数
	 *
	 * @param y y坐标
	 * @return The row that y falls in, or -1 if it falls in no row.
	 */
	private int getRowHit(float y) {
		final float squareHeight = mSquareHeight;
		float hitSize = squareHeight * HIT_FACTOR;
		
		float offset = getPaddingTop() + (squareHeight - hitSize) / 2f;
		for (int i = 0; i < 3; i++) {
			
			final float hitTop = offset + squareHeight * i;
			if (y >= hitTop && y <= hitTop + hitSize) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 依据，x坐标，确定列数
	 *
	 * @param x x坐标
	 * @return The column that x falls in, or -1 if it falls in no column.
	 */
	private int getColumnHit(float x) {
		final float squareWidth = mSquareWidth;
		float hitSize = squareWidth * HIT_FACTOR;
		
		float offset = getPaddingLeft() + (squareWidth - hitSize) / 2f;
		for (int i = 0; i < 3; i++) {
			
			final float hitLeft = offset + squareWidth * i;
			if (x >= hitLeft && x <= hitLeft + hitSize) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 系统调试时，帮助日志输出
	 */
	private void startProfileDrawing() {
		if (PROFILE_DRAWING) {
			if (!mDrawingProfilingStarted) {
				Debug.startMethodTracing("LockPatternDrawing");
				mDrawingProfilingStarted = true;
			}
		}
	}
	
	/**
	 * 系统调试时，帮助日志输出
	 */
	private void stopProfileDrawing() {
		if (PROFILE_DRAWING) {
			if (mDrawingProfilingStarted) {
				Debug.stopMethodTracing();
				mDrawingProfilingStarted = false;
			}
		}
	}
	
	/* ---------------------------------Draw------------------------------------- */
	@Override
	protected void onDraw(Canvas canvas) {
		final ArrayList<Cell> pattern = mPattern;
		final int count = pattern.size();
		final boolean[][] drawLookup = mPatternDrawLookup;
		
		if (mPatternDisplayMode == DisplayMode.Animate) {
			// figure out which circles to draw
			
			// + 1 so we pause on complete pattern
			final int oneCycle = (count + 1) * MILLIS_PER_CIRCLE_ANIMATING;
			final int spotInCycle = (int) (SystemClock.elapsedRealtime() - mAnimatingPeriodStart)
					% oneCycle;
			final int numCircles = spotInCycle / MILLIS_PER_CIRCLE_ANIMATING;
			
			clearPatternDrawLookup();
			for (int i = 0; i < numCircles; i++) {
				final Cell cell = pattern.get(i);
				drawLookup[cell.getRow()][cell.getColumn()] = true;
			}
			
			// figure out in progress portion of ghosting line
			final boolean needToUpdateInProgressPoint = numCircles > 0
					&& numCircles < count;
			
			if (needToUpdateInProgressPoint) {
				final float percentageOfNextCircle = ((float) (spotInCycle % MILLIS_PER_CIRCLE_ANIMATING))
						/ MILLIS_PER_CIRCLE_ANIMATING;
				
				final Cell currentCell = pattern.get(numCircles - 1);
				final float centerX = getCenterXForColumn(currentCell.column);
				final float centerY = getCenterYForRow(currentCell.row);
				
				final Cell nextCell = pattern.get(numCircles);
				final float dx = percentageOfNextCircle * (getCenterXForColumn(nextCell.column) - centerX);
				final float dy = percentageOfNextCircle * (getCenterYForRow(nextCell.row) - centerY);
				mInProgressX = centerX + dx;
				mInProgressY = centerY + dy;
			}
			// TODO: Infinite loop here...
			invalidate();
		}
		
		// draw the arrows associated with the path (unless the user is in
		// progress, and
		// we are in stealth mode)
		boolean oldFlag = (mPaint.getFlags() & Paint.FILTER_BITMAP_FLAG) != 0;
		mPaint.setFilterBitmap(true); // draw with higher quality since we
		drawLine(canvas, pattern, drawLookup);
		
		// draw the circles
		final int paddingTop = getPaddingTop();
		final int paddingLeft = getPaddingLeft();
		
		final float squareWidth = mSquareWidth;
		final float squareHeight = mSquareHeight;
		
		for (int i = 0; i < 3; i++) {
			float topY = paddingTop + i * squareHeight;
			// float centerY = mPaddingTop + i * mSquareHeight + (mSquareHeight / 2);
			for (int j = 0; j < 3; j++) {
				float leftX = paddingLeft + j * squareWidth;
				drawCircle(canvas, (int) leftX, (int) topY, drawLookup[i][j]);
			}
		}
		
		mPaint.setFilterBitmap(oldFlag); // restore default flag
	}
	
	/**
	 * 绘制线条
	 *
	 * @param canvas     画布
	 * @param pattern    图案
	 * @param drawLookup 绘制的标志量
	 */
	private void drawLine(Canvas canvas, ArrayList<Cell> pattern, boolean[][] drawLookup) {
		float lineWidth = mLineWidth;
		mLinePaint.setStrokeWidth(lineWidth);
		
		final Path currentPath = mCurrentPath;
		currentPath.rewind();
		
		// TODO: the path should be created and cached every time we hit-detect
		// a cell
		// only the last segment of the path should be computed here
		// draw the path of the pattern (unless the user is in progress, and
		// we are in stealth mode)
		final boolean drawPath = (mIsDrawLine || mPatternDisplayMode == DisplayMode.Error);
		
		// render with transforms
		// draw the lines
		if (drawPath) {
			// 不同模式，颜色不同
			if (mPatternDisplayMode == DisplayMode.Error) {
				mLinePaint.setColor(mLineColorError);
			} else {
				mLinePaint.setColor(mLineColorNormal);
			}
			
			// 绘制已经确定的线条
			boolean anyCircles = false;
			for (int i = 0; i < pattern.size(); i++) {
				Cell cell = pattern.get(i);
				
				// only draw the part of the pattern stored in
				// the lookup table (this is only different in the case
				// of animation).
				if (!drawLookup[cell.row][cell.column]) {
					break;
				}
				anyCircles = true;
				
				float centerX = getCenterXForColumn(cell.column); // 当前点的中心位置 X
				float centerY = getCenterYForRow(cell.row); // 当前点的中心位置Y
				if (i == 0) {
					lastLineX = centerX;
					lastLineY = centerY;
					// currentPath.moveTo(centerX, centerY); // 移动到目标位置
				} else {
					drawSingleLine(canvas, lastLineX, lastLineY, centerX, centerY, false);
					
					lastLineX = centerX;
					lastLineY = centerY;
					// currentPath.lineTo(centerX, centerY); // 绘制直线
				}
			}
			
			// 还未确定位置时，手势的位置
			if ((mPatternInProgress || mPatternDisplayMode == DisplayMode.Animate) && anyCircles) {
				drawSingleLine(canvas, lastLineX, lastLineY, mInProgressX, mInProgressY, true);
				// currentPath.lineTo(mInProgressX, mInProgressY);
			}
			// canvas.drawPath(currentPath, mLinePaint);
		}
	}
	
	/**
	 * @param isProgress 是否是手势，从而判断结尾是否需要动态计算
	 */
	private void drawSingleLine(Canvas canvas, float startX, float startY, float endX, float endY, boolean isProgress) {
		if (mIsLineThrough) {
			canvas.drawLine(startX, startY, endX, endY, mLinePaint);
		} else {
			float sx = Math.min(mSquareFactor * mSquareWidth / mBitmapCircleBgWidth, 1.0f);
			int radius = (int) (mBitmapCircleBgWidth * sx / 2);
			
			// 如果，还在圆圈内，则直接不绘制
			if ((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY) < radius * radius) {
				return;
			}
			
			if (Math.abs(startX - endX) < 10) {
				boolean isDownArrow = (endY > startY);
				
				startY = isDownArrow ? (startY + radius) : (startY - radius);
				endY = isProgress ? endY : (isDownArrow ? (endY - radius) : (endY + radius));
				
				canvas.drawLine(startX, startY, endX, endY, mLinePaint);
			} else if (Math.abs(startY - endY) < 10) {
				boolean isRightArrow = (endX > startX);
				
				startX = isRightArrow ? (startX + radius) : (startX - radius);
				endX = isProgress ? endX : (isRightArrow ? endX - radius : endX + radius);
				
				canvas.drawLine(startX, startY, endX, endY, mLinePaint);
			} else {
				float tan = (endY - startY) / (endX - startX); // 求 tan
				float cos = (float) (1.0f / Math.sqrt(1 + tan * tan)); // cos = 1 / [(1 + tan^2) ^ (1/2)]
				float width = cos * radius; // width = cos * radius
				float height = Math.abs(tan) * cos * radius; // height = sin * radius = |tan| * cos * radius
				
				boolean isDownArrow = (endY > startY);
				boolean isRightArrow = (endX > startX);
				
				startX = isRightArrow ? (startX + width) : (startX - width);
				endX = isProgress ? endX : (isRightArrow ? endX - width : endX + width);
				
				startY = isDownArrow ? (startY + height) : (startY - height);
				endY = isProgress ? endY : (isDownArrow ? endY - height : endY + height);
				
				canvas.drawLine(startX, startY, endX, endY, mLinePaint);
			}
		}
	}
	
	/**
	 * @param canvas        画布
	 * @param leftX         起始x坐标
	 * @param topY          起始y坐标
	 * @param partOfPattern Whether this circle is part of the pattern.（圆是否被用户选中）
	 */
	private void drawCircle(Canvas canvas, int leftX, int topY, boolean partOfPattern) {
		Bitmap outerCircle;
		Bitmap innerCircle;
		
		if (!partOfPattern) { // 未绘制状态
			// unselected circle
			outerCircle = mBitmapCircleBgStart;
			innerCircle = mBitmapCircleStart;
		} else if (mPatternInProgress) { // 正在绘制，用户选中状态
			// user is in middle of drawing a pattern
			outerCircle = mBitmapCircleBgNormal;
			innerCircle = mBitmapCircleNormal;
		} else if (mPatternDisplayMode == DisplayMode.Error) { // 绘制结束，绘制错误状态
			// the pattern is wrong
			outerCircle = mBitmapCircleBgError;
			innerCircle = mBitmapCircleError;
		} else if (mPatternDisplayMode == DisplayMode.Normal || mPatternDisplayMode == DisplayMode.Animate) { // 绘制结束，绘制成功状态 + 绘制过程浏览状态
			// the pattern is correct
			outerCircle = mBitmapCircleBgNormal;
			innerCircle = mBitmapCircleNormal;
		} else {
			throw new IllegalStateException("unknown display mode " + mPatternDisplayMode);
		}
		
		// 控件，1/3，宽高
		final float squareWidth = mSquareWidth;
		final float squareHeight = mSquareHeight;
		
		// Allow circles to shrink if the view is too small to hold them.
		float sx = Math.min(mSquareFactor * mSquareWidth / mBitmapCircleBgWidth, 1.0f); // 图形很小，0.9f则保证留有间隔
		float sy = Math.min(mSquareFactor * mSquareHeight / mBitmapCircleBgHeight, 1.0f);
		
		// 背景，宽高
		final int bgWidth = mBitmapCircleBgWidth;
		final int bgHeight = mBitmapCircleBgHeight;
		
		int bgOffsetX = (int) ((squareWidth - bgWidth) / 2.0f);
		int bgOffsetY = (int) ((squareHeight - bgHeight) / 2.0f);
		
		mCircleBgMatrix.setTranslate(leftX + bgOffsetX, topY + bgOffsetY);
		
		mCircleBgMatrix.preTranslate(mBitmapCircleBgWidth / 2, mBitmapCircleBgHeight / 2);
		mCircleBgMatrix.preScale(sx, sy);
		mCircleBgMatrix.preTranslate(-mBitmapCircleBgWidth / 2, -mBitmapCircleBgHeight / 2);
		
		if (null != outerCircle && mIsDrawBgCircle) {
			canvas.drawBitmap(outerCircle, mCircleBgMatrix, mPaint);
		}
		
		// 小图标，宽高
		final int width = mBitmapCircleWidth;
		final int height = mBitmapCircleHeight;
		
		int offsetX = (int) ((squareWidth - width) / 2f);
		int offsetY = (int) ((squareHeight - height) / 2f);
		
		mCircleMatrix.setTranslate(leftX + offsetX, topY + offsetY); // x、y方向平移
		
		mCircleMatrix.preTranslate(mBitmapCircleWidth / 2, mBitmapCircleHeight / 2);
		mCircleMatrix.preScale(sx, sy);
		mCircleMatrix.preTranslate(-mBitmapCircleWidth / 2, -mBitmapCircleHeight / 2);
		
		if (innerCircle != null) {
			canvas.drawBitmap(innerCircle, mCircleMatrix, mPaint);
		}
	}
	
	private float getCenterXForColumn(int column) {
		return getPaddingLeft() + column * mSquareWidth + mSquareWidth / 2f;
	}
	
	private float getCenterYForRow(int row) {
		return getPaddingTop() + row * mSquareHeight + mSquareHeight / 2f;
	}
	
	/**
	 * Reset all pattern state.
	 */
	private void resetPattern() {
		mPattern.clear();
		clearPatternDrawLookup();
		mPatternDisplayMode = DisplayMode.Normal;
		invalidate();
	}
	
	/**
	 * Clear the pattern lookup table.
	 */
	private void clearPatternDrawLookup() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mPatternDrawLookup[i][j] = false;
			}
		}
	}
	
	/* -----------------------无关乎逻辑的操作 + set get + 外置api--------------------------- */
	
	/**
	 * 设置将要展示的，视图内容
	 *
	 * @param displayMode How to display the pattern.
	 * @param pattern     The pattern.
	 */
	public void setPattern(DisplayMode displayMode, List<Cell> pattern) {
		mPattern.clear();
		mPattern.addAll(pattern);
		
		clearPatternDrawLookup();
		for (Cell cell : pattern) {
			mPatternDrawLookup[cell.getRow()][cell.getColumn()] = true;
		}
		
		setDisplayMode(displayMode);
	}
	
	/**
	 * Set the display mode of the current pattern. This can be useful, for
	 * instance, after detecting a pattern to tell this view whether change the
	 * in progress result to correct or wrong.
	 *
	 * @param displayMode The display mode.
	 */
	public void setDisplayMode(DisplayMode displayMode) {
		mPatternDisplayMode = displayMode;
		if (displayMode == DisplayMode.Animate) {
			if (mPattern.size() == 0) {
				throw new IllegalStateException(
						"you must have a pattern to "
								+ "animate if you want to set the display mode to animate");
			}
			mAnimatingPeriodStart = SystemClock.elapsedRealtime();
			final Cell first = mPattern.get(0);
			mInProgressX = getCenterXForColumn(first.getColumn());
			mInProgressY = getCenterYForRow(first.getRow());
			clearPatternDrawLookup();
		}
		invalidate();
	}
	
	/**
	 * Clear the pattern.
	 */
	public void clearPattern() {
		resetPattern();
	}
	
	/**
	 * 是否震动开关开启
	 *
	 * @return Whether the view has tactile feedback enabled.
	 */
	public boolean isTactileFeedbackEnabled() {
		return mEnableHapticFeedback;
	}
	
	/**
	 * 设置是否用户选择cell时，震动
	 * Set whether the view will use tactile feedback. If true, there will be
	 * tactile feedback as the user enters the pattern.
	 *
	 * @param tactileFeedbackEnabled Whether tactile feedback is enabled
	 */
	public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
		mEnableHapticFeedback = tactileFeedbackEnabled;
	}
	
	/**
	 * Set the call back for pattern detection.
	 *
	 * @param onPatternListener The call back.
	 */
	public void setOnPatternListener(OnPatternListener onPatternListener) {
		mOnPatternListener = onPatternListener;
	}
	
	private void notifyPatternStarted() {
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternStart();
		}
		sendAccessEvent("开始绘制图案");
	}
	
	private void notifyCellAdded() {
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternCellAdded(mPattern);
		}
		sendAccessEvent("已添加单元格");
	}
	
	private void notifyPatternDetected() {
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternDetected(mPattern);
		}
		sendAccessEvent("图案绘制完成");
	}
	
	private void notifyPatternCleared() {
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternCleared();
		}
		sendAccessEvent("图案已清除");
	}
	
	/**
	 * 为无障碍设置
	 */
	private void sendAccessEvent(String eventString) {
		setContentDescription(eventString);
		sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
		setContentDescription(null);
	}
	
	/**
	 * 关闭手势输入
	 */
	public void disableInput() {
		mInputEnabled = false;
	}
	
	/**
	 * 开启手势输入
	 */
	public void enableInput() {
		mInputEnabled = true;
	}
	
	/**
	 * 当前展示的状态
	 */
	public enum DisplayMode {
		/**
		 * 正常情况绘制时
		 */
		Normal,
		
		/**
		 * 自动绘制状态时
		 */
		Animate,
		
		/**
		 * 绘制异常时
		 */
		Error
	}
	
	/**
	 * The call back interface for detecting patterns entered by the user.
	 */
	public interface OnPatternListener {
		
		/**
		 * A new pattern has begun.
		 */
		void onPatternStart();
		
		/**
		 * The user extended the pattern currently being drawn by one cell.
		 *
		 * @param pattern The pattern with newly added cell.
		 */
		void onPatternCellAdded(List<Cell> pattern);
		
		/**
		 * A pattern was detected from the user.
		 *
		 * @param pattern The pattern.
		 */
		void onPatternDetected(List<Cell> pattern);
		
		/**
		 * The pattern was cleared.
		 */
		void onPatternCleared();
	}
	
	/**
	 * Represents a cell in the 3 X 3 matrix of the unlock pattern view.
	 */
	public static class Cell {
		private int row; // 行数
		private int column; // 列数
		
		/**
		 * @param row    The row of the cell.
		 * @param column The column of the cell.
		 */
		public static synchronized Cell of(int row, int column) {
			checkRange(row, column);
			return sCells[row][column];
		}
		
		private int getRow() {
			return row;
		}
		
		private int getColumn() {
			return column;
		}
		
		// keep # objects limited to 9
		private final static Cell[][] sCells = new Cell[3][3];
		static {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					sCells[i][j] = new Cell(i, j);
				}
			}
		}
		
		/**
		 * @param row    The row of the cell.
		 * @param column The column of the cell.
		 */
		private Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}
		
		private static void checkRange(int row, int column) {
			if (row < 0 || row > 2) {
				throw new IllegalArgumentException("row must be in range 0-2");
			}
			if (column < 0 || column > 2) {
				throw new IllegalArgumentException(
						"column must be in range 0-2");
			}
		}
		
		public String toString() {
			return "(row=" + row + ",column=" + column + ")";
		}
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle(6);
		
		Parcelable superState = super.onSaveInstanceState();
		bundle.putParcelable(SAVE_SUPER_STATE, superState);
		
		String patternString = LockPatternView.patternToString(mPattern);
		bundle.putString(SAVE_PATTERN, patternString);
		
		bundle.putInt(SAVE_DISPLAY_MODE, mPatternDisplayMode.ordinal());
		bundle.putBoolean(SAVE_INPUT_ENABLE, mInputEnabled);
		bundle.putBoolean(SAVE_STEALTH_MODE, mIsDrawLine);
		bundle.putBoolean(SAVE_FEEDBACK_ENABLE, mEnableHapticFeedback);
		return bundle;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		final Bundle bundle = (Bundle) state;
		
		String patternString = bundle.getString(SAVE_PATTERN);
		setPattern(DisplayMode.Normal, LockPatternView.stringToPattern(patternString));
		
		int ordinal = bundle.getInt(SAVE_DISPLAY_MODE);
		mPatternDisplayMode = DisplayMode.values()[ordinal];
		
		mInputEnabled = bundle.getBoolean(SAVE_INPUT_ENABLE);
		mIsDrawLine = bundle.getBoolean(SAVE_STEALTH_MODE);
		mEnableHapticFeedback = bundle.getBoolean(SAVE_FEEDBACK_ENABLE);
		
		super.onRestoreInstanceState(bundle.getParcelable(SAVE_SUPER_STATE));
	}
	
	/* ------------------对外提供的工具类-------------- */
	
	/**
	 * Deserialize a pattern. 将String转成Pattern
	 *
	 * @return The pattern.
	 */
	public static List<Cell> stringToPattern(String string) {
		List<Cell> result = new ArrayList<>();
		if (null != string) {
			final byte[] bytes = string.getBytes();
			for (byte b : bytes) {
				result.add(LockPatternView.Cell.of(b / 3, b % 3));
			}
		}
		return result;
	}
	
	/**
	 * Serialize a pattern. 将Pattern转成String
	 *
	 * @param pattern The pattern.
	 * @return The pattern in string form.
	 */
	public static String patternToString(List<Cell> pattern) {
		if (pattern == null) {
			return "";
		}
		final int patternSize = pattern.size();
		
		byte[] res = new byte[patternSize];
		for (int i = 0; i < patternSize; i++) {
			LockPatternView.Cell cell = pattern.get(i);
			res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
		}
		return new String(res);
	}
}
