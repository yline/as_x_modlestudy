package com.swipe.menu.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.swipe.menu.R;

/**
 * 修改：https://github.com/mcxtzhang/SwipeDelMenuLayout
 * 1，子控件不支持 Margin
 * 2，首个子控件为主控件，第二个为Menu（最好只有两个子控件，否则容易计算错误）
 *
 * @author yline 2018/6/27 -- 9:32
 * @version 1.0.0
 */
public class SwipeMenuLayout extends ViewGroup {
    private static final int ACTION_MOVE_MIN = 10; // 移动的最小距离
    private static final int DEFAULT_MENU_LIMIT = 16; // 默认最小宽度; px
    private static final int MAX_MENU_WIDTH = 1000; // 滑动速度超过阈值（经验值）

    /* ---------------------- 默认参数 或 默认参数 --------------------------- */
    private final int mScaleTouchSlop; // 为了处理单击事件的冲突
    private final int mMaxVelocity; // 计算滑动速度用
    private boolean isLeftSwipe; // 左滑右滑的开关 true(左滑)

    /* ---------------------- 动态变量 --------------------------- */
    /**
     * 菜单宽度总和(最大滑动距离)
     */
    private int mMenuWidths;

    /**
     * 滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu
     */
    private int mMenuLimit;

    /**
     * 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
     */
    private boolean isUserSwiped;

    /**
     * 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的区域，关闭侧滑菜单。
     * 增加一个布尔值变量，dispatch函数里，每次down时，为true，move时判断，如果是滑动动作，设为false。
     * 在Intercept函数的up时，判断这个变量，如果仍为true 说明是点击事件，则关闭菜单。
     */
    private boolean isUnMoved = true;

    /**
     * 防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空
     */
    private boolean isTouching;

    private VelocityTracker mVelocityTracker;// 滑动速度变量

    private int mPointerId;//多点触摸只算第一根手指的速度

    private View mContentView;// 存储contentView(第一个View)

    private PointF mLastP = new PointF(); // 上一次的xy

    private PointF mFirstP = new PointF(); //up-down的坐标，判断是否是滑动，如果是，则屏蔽一切点击事件

    private SwipeMenuLayout mViewCache; //存储的是当前正在展开的View

    private ValueAnimator mExpandAnim, mCloseAnim; // Menu 展开和关闭的 动画

    private boolean isMenuExpand; // 代表当前是否是展开状态

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0);
        isLeftSwipe = ta.getBoolean(R.styleable.SwipeMenuLayout_leftSwipe, true); // 左滑右滑的开关,默认左滑打开菜单
        ta.recycle();

        setClickable(true); // 令自己可点击，从而获取触摸事件
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        mMenuLimit = DEFAULT_MENU_LIMIT;
        mMenuWidths = 0; // Menu子控件宽度
        int contentWidth = 0, maxChildHeight = 0; // 主子控件宽度、所有子控件的最高高度

        // 若子控件有 match_parent属性，则默认配置一个高度值
        final boolean isExactlyMeasureView = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildHeight = false;

        // 循环遍历，计算控件
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setClickable(true);  // 令每一个子View可点击，从而获取触摸事件
            if (childView.getVisibility() != GONE) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                maxChildHeight = Math.max(maxChildHeight, childView.getMeasuredHeight());

                final ViewGroup.LayoutParams childLp = childView.getLayoutParams();
                if (isExactlyMeasureView && childLp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true;
                }

                if (i == 0) { // 主子控件
                    mContentView = childView;
                    contentWidth = childView.getMeasuredWidth();
                } else if (i == 1) {
                    mMenuWidths += childView.getMeasuredWidth();
                }
            }
        }
        setMeasuredDimension(getPaddingLeft() + getPaddingRight() + contentWidth,
                maxChildHeight + getPaddingTop() + getPaddingBottom());//宽度取第一个Item(Content)的宽度
        mMenuLimit = Math.max(mMenuLimit, mMenuWidths * 4 / 10);// 滑动判断的临界值
        // 如果子View的height有MatchParent属性的，设置子View高度
        if (isNeedMeasureChildHeight) {
            int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(maxChildHeight, MeasureSpec.EXACTLY); // 以父布局高度构建一个Exactly的测量参数
            forceUniformHeight(childCount, widthMeasureSpec, uniformMeasureSpec);
        }
    }

    /**
     * 给MatchParent的子View设置高度
     *
     * @param count              子控件个数
     * @param widthMeasureSpec   父控件宽度
     * @param uniformMeasureSpec 统一的高度
     * @see android.widget.LinearLayout #同名方法#
     */
    private void forceUniformHeight(int count, int widthMeasureSpec, int uniformMeasureSpec) {
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                ViewGroup.LayoutParams childLp = child.getLayoutParams();
                if (childLp.height == LayoutParams.MATCH_PARENT) {
                    measureChildren(widthMeasureSpec, uniformMeasureSpec);
//                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = getPaddingLeft(); // 左滑时，左侧起始位置
        int right = getPaddingLeft(); // 右滑时，左侧起始位置
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0) { // 第一个子View是内容 宽度设置为全屏
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                    left = left + childView.getMeasuredWidth();
                } else {
                    if (isLeftSwipe) {
                        childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                        left = left + childView.getMeasuredWidth();
                    } else {
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right, getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth();
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        acquireVelocityTracker(ev);
        final VelocityTracker verTracker = mVelocityTracker;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isUserSwiped = false; // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                isUnMoved = true; // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                if (isTouching) { // 如果有别的指头摸过了，那么就return false。这样后续的move..等事件也不会再来找这个View了。
                    return false;
                } else {
                    isTouching = true; // 第一个摸的指头，赶紧改变标志，宣誓主权。
                }
                mLastP.set(ev.getRawX(), ev.getRawY());
                mFirstP.set(ev.getRawX(), ev.getRawY());// 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。

                // 如果down，view和cacheView不一样，则立马让它还原。且把它置为null
                if (mViewCache != null) {
                    if (mViewCache != this) {
                        mViewCache.smoothClose();
                    }
                    // 只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                // 求第一个触点的id， 此时可能有多个触点，但至少一个，计算滑动速率用
                mPointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                float gapX = mLastP.x - ev.getRawX(); // 水平移动的距离
                // 为了在水平滑动中禁止父类ListView等再竖直滑动
                if (Math.abs(gapX) > ACTION_MOVE_MIN || Math.abs(getScrollX()) > ACTION_MOVE_MIN) { // 修改此处，使屏蔽父布局滑动更加灵敏，
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。begin
                if (Math.abs(gapX) > mScaleTouchSlop) {
                    isUnMoved = false;
                }

                scrollBy((int) (gapX), 0);// 滑动使用scrollBy
                // 越界修正
                if (isLeftSwipe) {//左滑
                    if (getScrollX() < 0) {
                        scrollTo(0, 0);
                    }
                    if (getScrollX() > mMenuWidths) {
                        scrollTo(mMenuWidths, 0);
                    }
                } else { // 右滑
                    if (getScrollX() < -mMenuWidths) {
                        scrollTo(-mMenuWidths, 0);
                    }
                    if (getScrollX() > 0) {
                        scrollTo(0, 0);
                    }
                }

                mLastP.set(ev.getRawX(), ev.getRawY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                    isUserSwiped = true;
                }

                // 求伪瞬时速度
                verTracker.computeCurrentVelocity(MAX_MENU_WIDTH, mMaxVelocity);
                final float velocityX = verTracker.getXVelocity(mPointerId);
                if (Math.abs(velocityX) > MAX_MENU_WIDTH) {//滑动速度超过阈值
                    if (velocityX < -MAX_MENU_WIDTH) {
                        if (isLeftSwipe) {//左滑
                            // 平滑展开Menu
                            smoothExpand();
                        } else {
                            // 平滑关闭Menu
                            smoothClose();
                        }
                    } else {
                        if (isLeftSwipe) {//左滑
                            // 平滑关闭Menu
                            smoothClose();
                        } else {
                            // 平滑展开Menu
                            smoothExpand();
                        }
                    }
                } else {
                    if (Math.abs(getScrollX()) > mMenuLimit) {//否则就判断滑动距离
                        //平滑展开Menu
                        smoothExpand();
                    } else {
                        // 平滑关闭Menu
                        smoothClose();
                    }
                }

                //释放
                releaseVelocityTracker();
                isTouching = false;//没有手指在摸我了
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            // fix 长按事件和侧滑的冲突。
            case MotionEvent.ACTION_MOVE:
                //屏蔽滑动时的事件
                if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //为了在侧滑时，屏蔽子View的点击事件
                if (isLeftSwipe) {
                    if (getScrollX() > mScaleTouchSlop) {
                        //这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的的。。。
                        if (ev.getX() < getWidth() - getScrollX()) {
                            // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                            if (isUnMoved) {
                                smoothClose();
                            }
                            return true;//true表示拦截
                        }
                    }
                } else {
                    if (-getScrollX() > mScaleTouchSlop) {
                        if (ev.getX() > -getScrollX()) {//点击范围在菜单外 屏蔽
                            // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                            if (isUnMoved) {
                                smoothClose();
                            }
                            return true;
                        }
                    }
                }
                // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                if (isUserSwiped) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void smoothExpand() {
        //展开就加入ViewCache：
        mViewCache = SwipeMenuLayout.this;

        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(false);
        }

        cancelAnim();
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), isLeftSwipe ? mMenuWidths : -mMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new DecelerateInterpolator()); // 如果要甩出来，使用OvershootInterpolator
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isMenuExpand = true;
            }
        });
        mExpandAnim.setDuration(300).start();
    }

    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private void cancelAnim() {
        if (mCloseAnim != null && mCloseAnim.isRunning()) {
            mCloseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }
    }

    /**
     * 平滑关闭
     */
    public void smoothClose() {
        mViewCache = null;

        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(true);
        }

        cancelAnim();
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AccelerateInterpolator());
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isMenuExpand = false;

            }
        });
        mCloseAnim.setDuration(300).start();
    }


    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * * 释放VelocityTracker
     *
     * @see VelocityTracker#clear()
     * @see VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    // 每次ViewDetach的时候，判断一下 ViewCache是不是自己，如果是自己，关闭侧滑菜单，且ViewCache设置为null，
    // 理由：1 防止内存泄漏(ViewCache是一个静态变量)
    // 2 侧滑删除后自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.smoothClose();
            mViewCache = null;
        }
        super.onDetachedFromWindow();
    }

    // 展开时，禁止长按
    @Override
    public boolean performLongClick() {
        return Math.abs(getScrollX()) <= mScaleTouchSlop && super.performLongClick();
    }

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    public void quickClose() {
        if (this == mViewCache) {
            //先取消展开动画
            cancelAnim();
            mViewCache.scrollTo(0, 0);//关闭
            mViewCache = null;
        }
    }

    public boolean isLeftSwipe() {
        return isLeftSwipe;
    }

    public void setLeftSwipe(boolean leftSwipe) {
        isLeftSwipe = leftSwipe;
    }

    public boolean isMenuExpand() {
        return isMenuExpand;
    }

    public void setMenuExpand(boolean menuExpand) {
        isMenuExpand = menuExpand;
    }
}
