package com.yline.slide

import android.os.Bundle
import com.yline.application.SDKManager
import com.yline.base.BaseAppCompatActivity
import com.yline.log.LogUtil
import com.yline.slide.view.LiveDrawerLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainDrawerLayout.setOnDrawerListener(object : LiveDrawerLayout.OnDrawerListener{
            override fun onClose() {
                LogUtil.v("Drawer onClose")
            }

            override fun onOpen() {
                LogUtil.v("Drawer onOpen")
            }

            override fun onFraction(fraction: Float) {
                // LogUtil.v("Drawer fraction: $fraction")
            }
        })

        mainDrawerOpen.setOnClickListener {
            LogUtil.v("mainDrawerOpen")
            mainDrawerLayout.open()
        }
        mainDrawerClose.setOnClickListener {
            LogUtil.v("mainDrawerClose")
            mainDrawerLayout.close()
        }

        mainSlideEmpty.setOnClickListener {
            LogUtil.v("mainSlideEmpty")
            mainDrawerLayout.close()
        }

        mainSlideContent.setOnClickListener {
            LogUtil.v("mainSlideContent")
            SDKManager.toast("mainSlideContent")
        }
    }
}
