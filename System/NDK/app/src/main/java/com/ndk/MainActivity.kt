package com.ndk

import android.os.Bundle
import android.view.View
import com.ndk.jni.JniManager
import com.ndk.jni.JniProvider
import com.yline.test.BaseTestActivity

/**
 * @author yline 2018/4/24 -- 10:16
 * @version 1.0.0
 */
class MainActivity : BaseTestActivity() {
    private var mEncodedStr: String? = null

    override fun testStart(view: View?, savedInstanceState: Bundle?) {
        /* ------------------------------------ Java调用C, 测试 ------------------------------------ */
        val libTextView = addTextView("")
        addButton("stringFromJNI") {
            val str = JniManager.getInstance().stringFromJNI()
            libTextView.text = str
        }

        val logTextView = addTextView("")
        addButton("logByJni") {
            val logStr = JniManager.getInstance().logByJni("Android->JNI->Log")
            logTextView.text = logStr
        }

        addButton("crashByJni") {
            JniManager.getInstance().doCrash("Crash Happened")
        }

        /* ------------------------------------ C调用Java, 测试 ------------------------------------ */
        addButton("doProvider") { JniProvider.getInstance().doProvider() }

        addButton("doStaticProvider") { JniProvider.getInstance().doStaticProvider() }

        /* ------------------------------------ Java调用C, 加密解密 ------------------------------------ */
        val encodeEditText = addEditText("", "yline&乾天")
        val encodeTextView = addTextView("")
        addButton("encode") {
            val inputStr = encodeEditText.text.toString().trim { it <= ' ' }
            mEncodedStr = JniManager.getInstance().encode(inputStr, 0)
            encodeTextView.text = mEncodedStr
        }

        val decodeTextView = addTextView("")
        addButton("decode") {
            if (null != mEncodedStr) {
                decodeTextView.text = JniManager.getInstance().decode(mEncodedStr, 0)
            }
        }
    }
}