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
	private float mSquareWidth; // 三分之一，宽度
	private float mSquareHeight; // 三分之一，高度
	
	private boolean mDrawingProfilingStarted = false; // 系统调试是否开始
	
	private float mInProgressX = -1; // 绘制中的x坐标
	private float mInProgressY = -1; // 绘制中的y坐标
	
	private long mAnimatingPeriodStart; // 动画是否开始
	
	private boolean mPatternInProgress = false; // 是否在绘制中
	
	private DisplayMode mPatternDisplayMode = DisplayMode.Normal; // 当前绘制状态
	
	private ArrayList<Cell> mPattern = new ArrayList<>(9); // 所有选中的Cell
	
	private boolean[][] mPatternDrawLookup = new boolean[3][3]; // 视图中，是否选中
	
	private Paint mPaint = new Paint();
	private final Matrix mCircleMatrix = new Matrix();
	
	private final Path mCurrentPath = new Path();
	private final Rect mInvalidate = new Rect();
	
	private Paint mPathPaint = new Paint(); // 绘制路径
	
	/* ------------------------------通过 attr 直接控制 或 间接控制---------------------------------- */
	private OnPatternListener mOnPatternListener; // 监听器
	
	private int mAspect;
	
	private Bitmap mBitmapCircleBg; // 单个点，背景图片
	private Bitmap mBitmapCircleNormal; // 单个点，正常绘制图片
	private Bitmap mBitmapCircleError; // 单个点，异常绘制图片
	
	private int mColorPathNormal; // 单个点，正常绘制路径颜色
	private int mColorPathError; // 单个点，异常绘制路径颜色
	
	private float mDiameterFactor = 0.20f; // 中间路径，占中心圆圈的比例（0.5则等于中心圆圈大小）
	
	private boolean mInStealthMode = false; // 是否隐身(path 和 cell将都不显示)
	private boolean mEnableHapticFeedback = true; // 震动开关
	private boolean mInputEnabled = true; // 是否允许手势绘制
	
	private int mBitmapWidth; // 单个点，宽度
	private int mBitmapHeight; // 单个点，高度
	
	public LockPatternView(Context context) {
		this(context, null);
	}
	
	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setClickable(true);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LockPatternView);
		mAspect = a.getInt(R.styleable.LockPatternView_aspect, ASPECT_SQUARE);
		
		final int circleBg = a.getResourceId(R.styleable.LockPatternView_img_circle_bg, R.drawable.gesture_pattern_item_bg);
		mBitmapCircleBg = BitmapFactory.decodeResource(getResources(), circleBg);
		
		final int circleNormal = a.getResourceId(R.styleable.LockPatternView_img_circle_normal, R.drawable.gesture_pattern_selected);
		mBitmapCircleNormal = BitmapFactory.decodeResource(getResources(), circleNormal);
		
		final int circleError = a.getResourceId(R.styleable.LockPatternView_img_circle_error, R.drawable.gesture_pattern_selected_wrong);
		mBitmapCircleError = BitmapFactory.decodeResource(getResources(), circleError);
		
		mColorPathNormal = a.getColor(R.styleable.LockPatternView_color_path_normal, Color.YELLOW);
		mColorPathError = a.getColor(R.styleable.LockPatternView_color_path_error, Color.RED);
		
		mInStealthMode = a.getBoolean(R.styleable.LockPatternView_is_stealth_mode, false);
		mEnableHapticFeedback = a.getBoolean(R.styleable.LockPatternView_is_haptic_feedback, false);
		mInputEnabled = a.getBoolean(R.styleable.LockPatternView_is_input_enable, true);
		
		mDiameterFactor = a.getFloat(R.styleable.LockPatternView_diameter_factor, 0.2f);
		a.recycle();
		
		mBitmapWidth = Math.max(mBitmapCircleBg.getWidth(),
				(Math.max(mBitmapCircleNormal.getWidth(), mBitmapCircleError.getWidth())));
		mBitmapHeight = Math.max(mBitmapCircleBg.getHeight(),
				(Math.max(mBitmapCircleNormal.getHeight(), mBitmapCircleError.getHeight())));
		
		mPathPaint.setAntiAlias(true); // 抗锯齿
		mPathPaint.setDither(true); // 抗抖动
		mPathPaint.setAlpha(128); // 半透明
		mPathPaint.setStyle(Paint.Style.STROKE);
		mPathPaint.setStrokeJoin(Paint.Join.ROUND);
		mPathPaint.setStrokeCap(Paint.Cap.ROUND);
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
		return 3 * mBitmapWidth;
	}
	
	@Override
	protected int getSuggestedMinimumHeight() {
		// View should be large enough to contain 3 side-by-side target bitmaps
		return 3 * mBitmapWidth;
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
					final float radius = mSquareWidth * mDiameterFactor * 0.5f;
					
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
					invalidateRect.set((int) (left - radius),
							(int) (top - radius), (int) (right + radius),
							(int) (bottom + radius));
					
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
					invalidateRect.union((int) (left - radius),
							(int) (top - radius), (int) (right + radius),
							(int) (bottom + radius));
					
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
				final float dx = percentageOfNextCircle
						* (getCenterXForColumn(nextCell.column) - centerX);
				final float dy = percentageOfNextCircle
						* (getCenterYForRow(nextCell.row) - centerY);
				mInProgressX = centerX + dx;
				mInProgressY = centerY + dy;
			}
			// TODO: Infinite loop here...
			invalidate();
		}
		
		final float squareWidth = mSquareWidth;
		final float squareHeight = mSquareHeight;
		
		float radius = (squareWidth * mDiameterFactor * 0.5f);
		mPathPaint.setStrokeWidth(radius);
		
		final Path currentPath = mCurrentPath;
		currentPath.rewind();
		
		// TODO: the path should be created and cached every time we hit-detect
		// a cell
		// only the last segment of the path should be computed here
		// draw the path of the pattern (unless the user is in progress, and
		// we are in stealth mode)
		final boolean drawPath = (!mInStealthMode || mPatternDisplayMode == DisplayMode.Error);
		
		// draw the arrows associated with the path (unless the user is in
		// progress, and
		// we are in stealth mode)
		boolean oldFlag = (mPaint.getFlags() & Paint.FILTER_BITMAP_FLAG) != 0;
		mPaint.setFilterBitmap(true); // draw with higher quality since we
		// render with transforms
		// draw the lines
		if (drawPath) {
			boolean anyCircles = false;
			for (int i = 0; i < count; i++) {
				Cell cell = pattern.get(i);
				
				// only draw the part of the pattern stored in
				// the lookup table (this is only different in the case
				// of animation).
				if (!drawLookup[cell.row][cell.column]) {
					break;
				}
				anyCircles = true;
				
				float centerX = getCenterXForColumn(cell.column);
				float centerY = getCenterYForRow(cell.row);
				if (i == 0) {
					currentPath.moveTo(centerX, centerY);
				} else {
					currentPath.lineTo(centerX, centerY);
				}
			}
			
			// add last in progress section
			if ((mPatternInProgress || mPatternDisplayMode == DisplayMode.Animate)
					&& anyCircles) {
				currentPath.lineTo(mInProgressX, mInProgressY);
			}
			// chang the line color in different DisplayMode
			if (mPatternDisplayMode == DisplayMode.Error) {
				mPathPaint.setColor(mColorPathError);
			} else {
				mPathPaint.setColor(mColorPathNormal);
			}
			canvas.drawPath(currentPath, mPathPaint);
		}
		
		// draw the circles
		final int paddingTop = getPaddingTop();
		final int paddingLeft = getPaddingLeft();
		
		for (int i = 0; i < 3; i++) {
			float topY = paddingTop + i * squareHeight;
			// float centerY = mPaddingTop + i * mSquareHeight + (mSquareHeight
			// / 2);
			for (int j = 0; j < 3; j++) {
				float leftX = paddingLeft + j * squareWidth;
				drawCircle(canvas, (int) leftX, (int) topY, drawLookup[i][j]);
			}
		}
		
		mPaint.setFilterBitmap(oldFlag); // restore default flag
	}
	
	/**
	 * @param canvas        画布
	 * @param leftX         起始x坐标
	 * @param topY          起始y坐标
	 * @param partOfPattern Whether this circle is part of the pattern.
	 */
	private void drawCircle(Canvas canvas, int leftX, int topY, boolean partOfPattern) {
		Bitmap outerCircle;
		Bitmap innerCircle;
		
		if (!partOfPattern || (mInStealthMode && mPatternDisplayMode != DisplayMode.Error)) {
			// unselected circle
			outerCircle = mBitmapCircleBg;
			innerCircle = null;
		} else if (mPatternInProgress) {
			// user is in middle of drawing a pattern
			outerCircle = mBitmapCircleBg;
			innerCircle = mBitmapCircleNormal;
		} else if (mPatternDisplayMode == DisplayMode.Error) {
			// the pattern is wrong
			outerCircle = mBitmapCircleBg;
			innerCircle = mBitmapCircleError;
		} else if (mPatternDisplayMode == DisplayMode.Normal || mPatternDisplayMode == DisplayMode.Animate) {
			// the pattern is correct
			outerCircle = mBitmapCircleBg;
			innerCircle = mBitmapCircleNormal;
		} else {
			throw new IllegalStateException("unknown display mode " + mPatternDisplayMode);
		}
		
		final int width = mBitmapWidth;
		final int height = mBitmapHeight;
		
		final float squareWidth = mSquareWidth;
		final float squareHeight = mSquareHeight;
		
		int offsetX = (int) ((squareWidth - width) / 2f);
		int offsetY = (int) ((squareHeight - height) / 2f);
		
		// Allow circles to shrink if the view is too small to hold them.
		float sx = Math.min(0.9f * mSquareWidth / mBitmapWidth, 1.0f); // 图形很小，0.9f则保证留有间隔
		float sy = Math.min(0.9f * mSquareHeight / mBitmapHeight, 1.0f);
		
		mCircleMatrix.setTranslate(leftX + offsetX, topY + offsetY);
		mCircleMatrix.preTranslate(mBitmapWidth / 2, mBitmapHeight / 2);
		mCircleMatrix.preScale(sx, sy);
		mCircleMatrix.preTranslate(-mBitmapWidth / 2, -mBitmapHeight / 2);
		
		canvas.drawBitmap(outerCircle, mCircleMatrix, mPaint);
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
	 * @return Whether the view is in stealth mode.
	 */
	public boolean isInStealthMode() {
		return mInStealthMode;
	}
	
	/**
	 * Set whether the view is in stealth mode. If true, there will be no
	 * visible feedback as the user enters the pattern.
	 *
	 * @param inStealthMode Whether in stealth mode.
	 */
	public void setInStealthMode(boolean inStealthMode) {
		mInStealthMode = inStealthMode;
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
		
		public int getRow() {
			return row;
		}
		
		public int getColumn() {
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
		
		String patternString = LockPatternUtils.patternToString(mPattern);
		bundle.putString(SAVE_PATTERN, patternString);
		
		bundle.putInt(SAVE_DISPLAY_MODE, mPatternDisplayMode.ordinal());
		bundle.putBoolean(SAVE_INPUT_ENABLE, mInputEnabled);
		bundle.putBoolean(SAVE_STEALTH_MODE, mInStealthMode);
		bundle.putBoolean(SAVE_FEEDBACK_ENABLE, mEnableHapticFeedback);
		return bundle;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		final Bundle bundle = (Bundle) state;
		
		String patternString = bundle.getString(SAVE_PATTERN);
		setPattern(DisplayMode.Normal, LockPatternUtils.stringToPattern(patternString));
		
		int ordinal = bundle.getInt(SAVE_DISPLAY_MODE);
		mPatternDisplayMode = DisplayMode.values()[ordinal];
		
		mInputEnabled = bundle.getBoolean(SAVE_INPUT_ENABLE);
		mInStealthMode = bundle.getBoolean(SAVE_STEALTH_MODE);
		mEnableHapticFeedback = bundle.getBoolean(SAVE_FEEDBACK_ENABLE);
		
		super.onRestoreInstanceState(bundle.getParcelable(SAVE_SUPER_STATE));
	}
}
