package com.yline.slide.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager.widget.ViewPager
import com.yline.slide.util.EventDebugUtil
import kotlin.math.max
import kotlin.math.min
import com.yline.slide.view.LiveDrawerInfo.LayoutStatus
import com.yline.slide.view.LiveDrawerInfo.PopupPosition
import com.yline.utils.UIScreenUtil


/**
 * 借助 ViewDragHelper 实现手势滑动
 *
 * 特殊逻辑: 默认全屏，只是计算偏移量的时候，按照目标偏移量计算【只有右侧这么做了兼容】
 *
 * created on 2020-07-09 -- 19:56
 * @author linjiang
 */
open class LiveDrawerLayout constructor(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private var layoutStatus: LayoutStatus = LayoutStatus.Close // 当前 drawer 的状态，默认关闭

    /**
     * 无论打开动作还是关闭动作，完全打开是1，完全关闭是0
     */
    private var fraction = 0f   // 当前移动的百分比

    private var hasLayout = false   // 是否 onLayout 执行过

    private var oldX = 0.0
    private var oldY = 0.0

    private var isOpened = false

    private lateinit var placeHolder: View
    private lateinit var mChild: View
    private lateinit var dragHelper: ViewDragHelper

    var drawerInfo = LiveDrawerInfo()   // 配置信息统一
    private var drawerListener: OnDrawerListener? = null



    private var downX = 0.0f
    private var isDownInTarget = false
    private var targetClzSet = HashSet<Class<*>>()

    private var callback = object : ViewDragHelper.Callback() {
        private var isJumpMoved = false
        private var clampDx = 0

        override fun tryCaptureView(view: View, i: Int): Boolean {
            return !(dragHelper.continueSettling(true))
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return 1
        }

        // 破坏，shouldInterceptTouchEvent  MotionEvent.ACTION_MOVE
        override fun getViewVerticalDragRange(child: View): Int {
            return -1
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            clampDx = dx

            return if (child === placeHolder) child.left else fixLeft(left)
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)

            if (changedView === placeHolder) {
                // LogUtil.v("changed placeHolder")
                mChild.let {
                    val newLeft: Int
                    if (!isJumpMoved) {
                        val offset = 3 * minTouchSlop   // 边界滑动太快
                        newLeft = fixLeft(it.left - childBlank + offset + clampDx)
                        isJumpMoved = true
                    } else {
                        newLeft = fixLeft(it.left + clampDx)
                    }

                    // LogUtil.v("newLeft = $newLeft, childBlank = $childBlank")
                    it.layout(newLeft, it.top, newLeft + it.measuredWidth, it.bottom)

                    calcFraction(newLeft)
                }
            } else {
                // LogUtil.v("changed other, $left")
                calcFraction(left)
            }
        }

        /**
         * releasedChild 代表的仅仅是，开始滑动时，手指的位置
         *
         * xvel: 如果中途停止，则为 0； 从左往右滑动，则为很大的"正数"；从右往左，则为很大的"负数"
         *
         */
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)

            isJumpMoved = false
            // LogUtil.v("close xvel = $xvel, yvel = $yvel, ${releasedChild === mChild}, ${releasedChild === placeHolder}, $fraction")

            /*if (releasedChild === placeHolder && abs(xvel) <= 1 && fraction > 0.98) { // 点击空白区域，关闭的逻辑
                LogUtil.v("close by placeHolder")
                closeInner()
                return
            }*/

            /*if (releasedChild === mChild && isToLeft && !canChildScrollLeft && xvel < -500) {
                close()
                return
            }*/

            if (drawerInfo.openPosition === PopupPosition.Left) {
                if (xvel < -1000) {  // 判断为快速滑动
                    closeInner()
                    return
                }

                if (xvel > 1000) {  // 判断为快速滑动
                    openInner()
                    return
                }

                val centerLeft = -mChild.measuredWidth / 2
                if (mChild.left < centerLeft) {
                    closeInner()
                } else {
                    openInner()
                }
            } else {
                if (xvel > 1000) {  // 判断为快速滑动
                    closeInner()
                    return
                }

                if (xvel < -1000) {  // 判断为快速滑动
                    openInner()
                    return
                }

                val centerLeft = measuredWidth - childWidth / 2
                // LogUtil.v("centerLeft = $centerLeft, childLeft = ${mChild.left + childBlank}")
                if (mChild.left + childBlank < centerLeft) {
                    openInner()
                } else {
                    closeInner()
                }
            }
        }
    }

    private var childWidth = 0  // 目标子类的宽度，可以设置的值
    private var childBlank = 0  // 总的宽度 - 目标子类的宽度

    private val minTouchSlop = ViewConfiguration.get(context).scaledTouchSlop   // 最小滑动距离

    init {
        dragHelper = ViewDragHelper.create(this, callback)

        post {
            childWidth = UIScreenUtil.dp2px(context, 200.0f)
            childBlank = width - childWidth
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        placeHolder = getChildAt(0)     // 底部控件
        mChild = getChildAt(1) // 测滑控件
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // 底部位置
        placeHolder.layout(0, 0, placeHolder.measuredWidth, placeHolder.measuredHeight)

        // 上层位置
        if (!hasLayout) {
            if (drawerInfo.openPosition === PopupPosition.Left) {
                mChild.layout(-mChild.measuredWidth, 0, 0, measuredHeight)
            } else {
                mChild.layout(
                    measuredWidth,
                    0,
                    measuredWidth + mChild.measuredWidth,
                    measuredHeight
                )
            }
            hasLayout = true
        } else {
            mChild.layout(mChild.left, mChild.top, mChild.right, mChild.bottom)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!drawerInfo.enableDrag) {
            return EventDebugUtil.debugReturn(super.onInterceptTouchEvent(ev))
        }

        val isMovingToLeft = ev.x < oldX    // 表示 正在往左滑动
        val isToLeft = ev.x + minTouchSlop < downX   // 表示 已经往左滑动了一段距离；经测试 minTouchSlop 华为P30 上为 24
        oldX = ev.x.toDouble()
        oldY = ev.y.toDouble()

        //        boolean canChildScrollRight = canScroll(this, ev.getX(), ev.getY(), -1);
        val canChildScrollLeft = canScroll(this, ev.x, ev.y, 1)
        if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL) {
            oldX = 0.0
            oldY = 0.0
        }

        val isIntercept = dragHelper.shouldInterceptTouchEvent(ev)
        if (isMovingToLeft && !canChildScrollLeft && isToLeft) {
            if (isDownInTarget) {
                return EventDebugUtil.debugReturn(
                    false,
                    "inTargetView: $isMovingToLeft, $isToLeft, $canChildScrollLeft, $isIntercept"
                )
            }
            return EventDebugUtil.debugReturn(
                isIntercept,
                "left: $isMovingToLeft, $isToLeft, $canChildScrollLeft, $isIntercept"
            )
        }

        // 视图在打开的状态下，如果当前为应该滚动，就直接拦截掉。如果不是，就让他继续可以点击
        if (isOpened) {
            return EventDebugUtil.debugReturn(isIntercept)
        }

        return EventDebugUtil.debugReturn(
            false,
            "end: $isMovingToLeft, $isToLeft, $canChildScrollLeft, $isIntercept"
        )
        /*val canChildScrollHorizontal = canScroll(this, ev.x, ev.y)
        return EventDebugUtil.debugReturn(
                if (!canChildScrollHorizontal) isIntercept else super.onInterceptTouchEvent(ev),
                "end: $canChildScrollHorizontal, $isIntercept")*/
    }

    /**
     * 判断，在 LiveDrawerLayout 上层，是否有需要消费事件的 ViewGroup
     * true 有
     */
    private fun isInTargetRect(view: ViewGroup, x: Float, y: Float): Boolean {
        var exist = false
        for (index in 0 until view.childCount) {
            val child = view.getChildAt(index)

            // 子控件也必须是 ViewGroup
            if (child is ViewGroup) {
                // 存在目标，并且，在节点内，就直接返回 true
                if (child.javaClass in targetClzSet) {
                    val location = IntArray(2)
                    child.getLocationInWindow(location)
                    val rect = Rect(
                        location[0], location[1], location[0] + child.width,
                        location[1] + child.height
                    )

                    val inRect = isInRect(x, y, rect)
                    if (inRect) {
                        return true
                    }
                }

                exist = exist.or(isInTargetRect(child, x, y))
            }

            // view 就不用考虑了
        }
        return exist
    }

    private fun canScroll(group: ViewGroup, x: Float, y: Float, direction: Int = 0): Boolean {
        for (i in 0 until group.childCount) {
            val child = group.getChildAt(i)
            val location = IntArray(2)
            child.getLocationInWindow(location)
            val rect = Rect(
                location[0], location[1], location[0] + child.width,
                location[1] + child.height
            )

            val inRect = isInRect(x, y, rect)
            if (inRect && child is ViewGroup) {
                return if (child is ViewPager) {
                    if (direction == 0) {
                        child.canScrollHorizontally(-1) || child.canScrollHorizontally(1)
                    } else child.canScrollHorizontally(direction)
                } else if (child is HorizontalScrollView) {
                    if (direction == 0) {
                        child.canScrollHorizontally(-1) || child.canScrollHorizontally(1)
                    } else child.canScrollHorizontally(direction)
                } else {
                    canScroll(child, x, y, direction)
                }
            }
        }
        return false
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!drawerInfo.enableDrag) {
            return EventDebugUtil.debugReturn(super.onTouchEvent(event))
        }

        if (dragHelper.continueSettling(true)) {
            return EventDebugUtil.debugReturn(true)
        }

        dragHelper.processTouchEvent(event)
        return EventDebugUtil.debugReturn(true)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 手势拦截判断
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                isDownInTarget = isInTargetRect(this, ev.x, ev.y)
                downX = ev.x
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                isDownInTarget = false
                downX = 0f
            }
        }

        return EventDebugUtil.debugReturn(
            super.dispatchTouchEvent(ev),
            "action: ${ev?.action}"
        )
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        drawerInfo.customStatusBar(this, canvas, fraction)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        hasLayout = false
        fraction = 0f
    }

    /**
     * 打开或关闭 drawer
     */
    fun switch() {
        if (isOpened) {
            openInner()
        } else {
            closeInner()
        }
    }

    /**
     * 打开 drawer
     */
    fun open() {
        if (isOpened) {
            return
        }

        openInner()
    }

    /**
     * 关闭 drawer
     */
    fun close() {
        if (!isOpened) {
            return
        }

        closeInner()
    }

    fun isOpened(): Boolean {
        return isOpened
    }

    fun clearTargetClz() {
        targetClzSet.clear()
    }

    fun addTargetClz(clazz: Class<*>) {
        targetClzSet.add(clazz)
    }

    /**
     * 设置监听事件
     */
    fun setOnDrawerListener(listener: OnDrawerListener) {
        this.drawerListener = listener
    }

    private fun openInner() {
        post {
            dragHelper.smoothSlideViewTo(
                mChild,
                if (drawerInfo.openPosition === PopupPosition.Left) 0 else measuredWidth - mChild.measuredWidth,
                0
            )
            ViewCompat.postInvalidateOnAnimation(this@LiveDrawerLayout)

            isOpened = true
        }
    }

    private fun closeInner() {
        if (dragHelper.continueSettling(true)) {
            return
        }

        post {
            dragHelper.smoothSlideViewTo(
                mChild,
                if (drawerInfo.openPosition === PopupPosition.Left) -mChild.measuredWidth else measuredWidth,
                0
            )
            ViewCompat.postInvalidateOnAnimation(this@LiveDrawerLayout)

            isOpened = false
        }
    }

    private fun isInRect(x: Float, y: Float, rect: Rect): Boolean {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom
    }

    private fun calcFraction(left: Int) {
        if (drawerInfo.openPosition === PopupPosition.Left) {
            mChild.let {
                fraction = (left + it.measuredWidth) * 1f / it.measuredWidth
                if (left == -it.measuredWidth && layoutStatus !== LayoutStatus.Close) {
                    layoutStatus = LayoutStatus.Close

                    drawerListener?.onClose()
                }
            }
        } else if (drawerInfo.openPosition === PopupPosition.Right) {
            mChild.let {
                fraction = (measuredWidth - left) * 1f / it.measuredWidth
                if (left == measuredWidth && layoutStatus !== LayoutStatus.Close) {
                    layoutStatus = LayoutStatus.Close

                    drawerListener?.onClose()
                }
            }
        }

        drawerInfo.customBackgroundColor(this@LiveDrawerLayout, fraction)

        drawerListener?.let {
            it.onFraction(fraction)
            if (fraction == 1f && layoutStatus !== LayoutStatus.Open) {
                layoutStatus = LayoutStatus.Open

                it.onOpen()
            }
        }
    }

    private fun fixLeft(left: Int): Int {
        var result = left
        if (drawerInfo.openPosition === PopupPosition.Left) {
            result = max(result, -mChild.measuredWidth)
            result = min(result, 0)
        } else if (drawerInfo.openPosition === PopupPosition.Right) {
            result = max(result, measuredWidth - mChild.measuredWidth)
            result = min(result, measuredWidth)
        }
        return result
    }

    interface OnDrawerListener {
        /**
         * 关闭状态
         */
        fun onClose()

        /**
         * 打开状态
         */
        fun onOpen()

        /**
         * 关闭过程中执行
         *
         * @param fraction 关闭的百分比
         */
        fun onFraction(fraction: Float)
    }
}
