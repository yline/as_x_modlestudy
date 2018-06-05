package com.manager.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yline.base.BaseActivity

/**
 * 欢迎界面
 * @author yline 2018/6/5 -- 15:28
 * @version 1.0.0
 */
class WelcomeActivity : BaseActivity() {
    companion object {
        fun launcher(context: Context) {
            val intent = Intent()
            intent.setClass(context, WelcomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WelcomeStyleManager.showMoveDownStyle(this@WelcomeActivity)
    }
}