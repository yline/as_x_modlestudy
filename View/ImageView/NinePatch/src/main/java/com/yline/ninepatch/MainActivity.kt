package com.yline.ninepatch

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yline.utils.LogUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val inputStream = assets.open("live_test_pic_bubble_live.png")
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val patchBuilder = NinePatchBuilder(resources, bitmap)
            patchBuilder.addXCenteredRegion(1)
            patchBuilder.addYCenteredRegion(1)

            val drawable = patchBuilder.build()

            mainNinePatchTv.text = "123djkafjdklsajfdsajfkdlaj"
            mainNinePatchTv.background = drawable
        } catch (ex: Throwable) {
            LogUtil.e("error:", ex)
        }
    }
}
