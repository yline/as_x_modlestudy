package com.yline.opengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yline.base.BaseActivity
import com.yline.opengl.basic.rectangle.BasicRectangleActivity
import com.yline.test.BaseTestActivity

class MainActivity : BaseTestActivity() {

    override fun testStart(view: View, savedInstanceState: Bundle?) {
        addButton("基础图形 - 矩形", View.OnClickListener {
            BasicRectangleActivity.launch(this@MainActivity)
        })
    }
}