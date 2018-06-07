package com.manager

import android.os.Bundle
import android.view.View
import com.manager.apn.APNManagerTest
import com.manager.compress.CompressActivity
import com.manager.intent.IntentActivity
import com.manager.welcome.WelcomeActivity
import com.yline.test.BaseTestActivity

class MainActivity : BaseTestActivity() {
    override fun testStart(view: View?, savedInstanceState: Bundle?) {
        addButton("APN Manager") {
            APNManagerTest.test()
        }

        addButton("Welcome Style Manager") {
            WelcomeActivity.launcher(this@MainActivity)
        }

        addButton("Intent Manager") {
            IntentActivity.launcher(this@MainActivity)
        }

        addButton("Compress Manager") {
            CompressActivity.launcher(this@MainActivity)
        }
    }
}