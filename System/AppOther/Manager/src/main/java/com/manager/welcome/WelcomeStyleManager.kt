package com.manager.welcome

import android.app.Activity
import android.content.Intent
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.manager.MainActivity
import com.manager.R
import com.yline.application.SDKManager

/**
 * 欢迎界面 各种方式
 * @author yline 2018/6/5 -- 15:33
 * @version 1.0.0
 */
object WelcomeStyleManager {
    /**
     * 闪烁时间
     */
    private const val SPLASH_TIME = 2000

    /**
     * 向下移动动画
     */
    fun showMoveDownStyle(activity: Activity) {
        activity.setContentView(R.layout.welcome_style_move_action)

        val title = activity.findViewById(R.id.welcome_style_move_tv_logo) as TextView
        val logo = activity.findViewById(R.id.welcome_style_move_iv_logo) as ImageView

        val logoDown = AnimationUtils.loadAnimation(activity, R.anim.welcome_style_move_action_down)
        val logoUp = AnimationUtils.loadAnimation(activity, R.anim.welcome_style_move_action_up)
        logo.startAnimation(logoDown)
        title.startAnimation(logoUp)

        SDKManager.getHandler().postDelayed(Runnable {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }, SPLASH_TIME.toLong())
    }
}