package com.yline.slide.view

import android.animation.ArgbEvaluator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.yline.utils.UIScreenUtil

/**
 * 单个配置信息
 *
 * created on 2020-07-09 -- 16:08
 * @author linjiang
 */
class LiveDrawerInfo {
    /**
     * true 启用拖拽
     */
    var enableDrag = true
    /**
     * 出现的位置
     */
    var openPosition = PopupPosition.Right

    /* ---------------- 通过api 被使用到的 ---------------- */
    private var hasShadowBg = true

    private var argbEvaluator = ArgbEvaluator()
    private var bgStartValue = Color.TRANSPARENT
    private var bgEndValue = Color.parseColor("#9F000000")

    /**
     * 百分比变化时，绘制背景；可以不绘制呀
     * @param fraction 百分比，[0, 1]
     */
    fun customBackgroundColor(drawerLayout: LiveDrawerLayout, fraction: Float) {
        if (hasShadowBg) {
            val bgColor = argbEvaluator.evaluate(fraction, bgStartValue, bgEndValue) as Int
            drawerLayout.setBackgroundColor(bgColor)
        }
    }

    private var isDrawStatusBarShadow = false
    private var paint: Paint? = null
    private var statusBarShadow: Rect? = null
    private var statusBarStartValue = Color.TRANSPARENT
    private var statusBarEndValue = Color.parseColor("#9F000000")

    /**
     * 绘制 状态栏，需要搭配使用
     * @param fraction 百分比，[0, 1]
     */
    fun customStatusBar(drawerLayout: LiveDrawerLayout, canvas: Canvas, fraction: Float) {
        if (isDrawStatusBarShadow) {
            if (paint == null) {
                paint = Paint()

                statusBarShadow = Rect(
                    0,
                    0,
                    drawerLayout.width,
                    UIScreenUtil.getStatusHeight(drawerLayout.context)
                )
            }

            paint!!.color =
                argbEvaluator.evaluate(fraction, statusBarStartValue, statusBarEndValue) as Int
            canvas.drawRect(statusBarShadow!!, paint!!)
        }
    }

    /**
     * 出现的位置
     */
    enum class PopupPosition {
        Left, Right
    }

    enum class LayoutStatus {
        Open, Close, Opening, Closing
    }
}

