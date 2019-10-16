package com.swipe.menu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.swipe.menu.R;

import java.util.ArrayList;

/**
 * 侧滑没问题；缺陷：一个界面同时只能够打开1个；
 *
 * @author yline 2019/10/16 -- 15:27
 */
public class EasySwipeMenuLayout extends ViewGroup {
    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);
    
    private boolean mIsSwiping;
    private float mFinalYDistanceX;
    
    private PointF mLastP = new PointF();
    private PointF mFirstP = new PointF();
    
    private State mCacheState;
    private static EasySwipeMenuLayout mCacheView; // 在里面放着，省事，不用adapter回调去处理，也还行
    
    /* 控件 */
    private View mLeftView;
    private View mRightView;
    private View mContentView;
    private MarginLayoutParams mContentViewLp;
    
    /* 初始化就确定的参数 */
    private final int mScaledTouchSlop;
    private final Scroller mScroller;
    
    private final int mLeftViewResID;
    private final int mRightViewResID;
    private final int mContentViewResID;
    private final float mFraction;
    
    public EasySwipeMenuLayout(Context context) {
        this(context, null);
    }
    
    public EasySwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public EasySwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        // 获取配置的属性值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EasySwipeMenuLayout, defStyleAttr, 0);
        try {
            mLeftViewResID = a.getResourceId(R.styleable.EasySwipeMenuLayout_leftMenuView, -1);
            mRightViewResID = a.getResourceId(R.styleable.EasySwipeMenuLayout_rightMenuView, -1);
            mContentViewResID = a.getResourceId(R.styleable.EasySwipeMenuLayout_contentView, -1);
            mFraction = a.getFloat(R.styleable.EasySwipeMenuLayout_fraction, 0.5f);
        } finally {
            a.recycle();
        }
        
        // 创建辅助对象
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取childView的个数
        setClickable(true);
        
        //参考frameLayout测量代码
        final boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        
        mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        // 遍历childViews
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT || lp.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }
        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
        
        int count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final View child = mMatchParentChildren.get(i);
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                
                final int childWidthMeasureSpec;
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    final int width = Math.max(0, getMeasuredWidth() - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
                }
                
                final int childHeightMeasureSpec;
                if (lp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getMeasuredHeight() - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
                }
                
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }
    
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int left = 0 + getPaddingLeft();
        int right = 0 + getPaddingLeft();
        int top = 0 + getPaddingTop();
        int bottom = 0 + getPaddingTop();
        
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (mLeftView == null && child.getId() == mLeftViewResID) {
                // Log.i(TAG, "找到左边按钮view");
                mLeftView = child;
                mLeftView.setClickable(true);
            } else if (mRightView == null && child.getId() == mRightViewResID) {
                // Log.i(TAG, "找到右边按钮view");
                mRightView = child;
                mRightView.setClickable(true);
            } else if (mContentView == null && child.getId() == mContentViewResID) {
                // Log.i(TAG, "找到内容View");
                mContentView = child;
                mContentView.setClickable(true);
            }
        }
        
        // 布局contentView
        if (mContentView != null) {
            mContentViewLp = (MarginLayoutParams) mContentView.getLayoutParams();
            int cTop = top + mContentViewLp.topMargin;
            int cLeft = left + mContentViewLp.leftMargin;
            int cRight = left + mContentViewLp.leftMargin + mContentView.getMeasuredWidth();
            int cBottom = cTop + mContentView.getMeasuredHeight();
            mContentView.layout(cLeft, cTop, cRight, cBottom);
        }
        if (mLeftView != null) {
            MarginLayoutParams leftViewLp = (MarginLayoutParams) mLeftView.getLayoutParams();
            int lTop = top + leftViewLp.topMargin;
            int lLeft = 0 - mLeftView.getMeasuredWidth() + leftViewLp.leftMargin + leftViewLp.rightMargin;
            int lRight = 0 - leftViewLp.rightMargin;
            int lBottom = lTop + mLeftView.getMeasuredHeight();
            mLeftView.layout(lLeft, lTop, lRight, lBottom);
        }
        if (mRightView != null) {
            MarginLayoutParams rightViewLp = (MarginLayoutParams) mRightView.getLayoutParams();
            int rTop = top + rightViewLp.topMargin;
            int rLeft = mContentView.getRight() + mContentViewLp.rightMargin + rightViewLp.leftMargin;
            int rRight = rLeft + mRightView.getMeasuredWidth();
            int rBottom = rTop + mRightView.getMeasuredHeight();
            mRightView.layout(rLeft, rTop, rRight, rBottom);
        }
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //   System.out.println(">>>>dispatchTouchEvent() ACTION_DOWN");
                mIsSwiping = false;
                mLastP.set(ev.getRawX(), ev.getRawY());
                mFirstP.set(ev.getRawX(), ev.getRawY());
                
                // yline 判断是否有控件被关闭
                if (mCacheView != null) {
                    if (mCacheView != this) {
                        mCacheView.handlerSwipeMenuInner(State.CLOSE);
                    }
                    // Log.i(TAG, ">>>有菜单被打开");
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float distanceX = mLastP.x - ev.getRawX();
                float distanceY = mLastP.y - ev.getRawY();
                if (Math.abs(distanceY) > mScaledTouchSlop && Math.abs(distanceY) > Math.abs(distanceX)) {
                    break;
                }
                
                scrollBy((int) (distanceX), 0);//滑动使用scrollBy
                
                // 越界修正
                if (getScrollX() < 0) {
                    if (mLeftView == null) {
                        scrollTo(0, 0);
                    } else { // 左滑
                        if (getScrollX() < mLeftView.getLeft()) {
                            scrollTo(mLeftView.getLeft(), 0);
                        }
                    }
                } else if (getScrollX() > 0) {
                    if (mRightView == null) {
                        scrollTo(0, 0);
                    } else {
                        if (getScrollX() > mRightView.getRight() - mContentView.getRight() - mContentViewLp.rightMargin) {
                            scrollTo(mRightView.getRight() - mContentView.getRight() - mContentViewLp.rightMargin, 0);
                        }
                    }
                }
                
                //当处于水平滑动时，禁止父类拦截
                if (Math.abs(distanceX) > mScaledTouchSlop) { // || Math.abs(getScrollX()) > mScaledTouchSlop
                    //  Log.i(TAG, ">>>>当处于水平滑动时，禁止父类拦截 true");
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                
                mLastP.set(ev.getRawX(), ev.getRawY());
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mFinalYDistanceX = mFirstP.x - ev.getRawX();
                if (Math.abs(mFinalYDistanceX) > mScaledTouchSlop) {
                    mIsSwiping = true;
                }
                
                // yline 判断是否有控件将要被打开
                State result = isShouldOpen(getScrollX());
                handlerSwipeMenuInner(result);
                break;
            }
            default:
                break;
        }
        
        return super.dispatchTouchEvent(ev);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //  Log.d(TAG, "<<<<dispatchTouchEvent() called with: " + "ev = [" + event + "]");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动时拦截点击时间
                if (Math.abs(mFinalYDistanceX) > mScaledTouchSlop) { // || Math.abs(getScrollX()) > mScaledTouchSlop
                    // 当手指拖动值大于mScaledTouchSlop值时，认为应该进行滚动，拦截子控件的事件
                    //   Log.i(TAG, "<<<onInterceptTouchEvent true");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                //滑动后不触发contentView的点击事件
                if (mIsSwiping) {
                    mIsSwiping = false;
                    mFinalYDistanceX = 0;
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(event);
    }
    
    /**
     * 自动设置状态
     *
     * @param state 目标状态
     */
    private void handlerSwipeMenuInner(State state) {
        if (state == State.LEFT_OPEN) {
            mScroller.startScroll(getScrollX(), 0, mLeftView.getLeft() - getScrollX(), 0);
            mCacheState = state;
            mCacheView = this;
        } else if (state == State.RIGHT_OPEN) {
            mScroller.startScroll(getScrollX(), 0, mRightView.getRight() - mContentView.getRight() - mContentViewLp.rightMargin - getScrollX(), 0);
            mCacheState = state;
            mCacheView = this;
        } else {
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
            mCacheState = null;
            mCacheView = null;
        }
        invalidate();
    }
    
    @Override
    public void computeScroll() {
        // 判断Scroller是否执行完毕：
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate();
        }
    }
    
    /**
     * 根据当前的scrollX的值判断松开手后应处于何种状态
     */
    private State isShouldOpen(int scrollX) {
        if (!(mScaledTouchSlop < Math.abs(mFinalYDistanceX))) {
            return mCacheState;
        }
        
        // Log.i(TAG, ">>>mFinalYDistanceX:" + mFinalYDistanceX);
        if (mFinalYDistanceX < 0) {
            // 滑动
            // 1、展开左边按钮
            // 获得leftView的测量长度
            if (scrollX < 0 && mLeftView != null) {
                if (Math.abs(mLeftView.getWidth() * mFraction) < Math.abs(scrollX)) {
                    return State.LEFT_OPEN;
                }
            }
            
            // 2、关闭右边按钮
            if (scrollX > 0 && mRightView != null) {
                return State.CLOSE;
            }
        } else if (mFinalYDistanceX > 0) {
            // 滑动
            // 3、开启右边菜单按钮
            if (scrollX > 0 && mRightView != null) {
                if (Math.abs(mRightView.getWidth() * mFraction) < Math.abs(scrollX)) {
                    return State.RIGHT_OPEN;
                }
            }
            
            // 4、关闭左边
            if (scrollX < 0 && mLeftView != null) {
                return State.CLOSE;
            }
        }
        
        return State.CLOSE;
    }
    
    @Override
    protected void onDetachedFromWindow() {
        if (this == mCacheView) {
            mCacheView.handlerSwipeMenuInner(State.CLOSE);
        }
        super.onDetachedFromWindow();
        //  Log.i(TAG, ">>>>>>>>onDetachedFromWindow");
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this == mCacheView) {
            mCacheView.handlerSwipeMenuInner(mCacheState);
        }
        // Log.i(TAG, ">>>>>>>>onAttachedToWindow");
    }
    
    /**
     * 重置回初始状态
     */
    public static void resetState() {
        if (null != mCacheView && mCacheView.mCacheState != State.CLOSE) {
            mCacheView.handlerSwipeMenuInner(State.CLOSE);
            mCacheView = null;
        }
    }
    
    /**
     * 获取当前状态
     * PS：如果是点击其他item，获取到的是已经关闭的状态；因为onTouch在前面执行了
     *
     * @return 当前状态
     */
    public static State getState() {
        State state = null == mCacheView ? null : mCacheView.mCacheState;
        return null == state ? State.CLOSE : state;
    }
    
    public enum State {
        CLOSE, LEFT_OPEN, RIGHT_OPEN
    }
}
