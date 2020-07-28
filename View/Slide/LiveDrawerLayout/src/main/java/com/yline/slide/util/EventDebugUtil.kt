package com.yline.slide.util

import java.util.*

/**
 * 1, 方法参数中添加 ?, 在方法调用栈, 会多调用一次【相当于 调用了一次 全参方法】
 *
 * created on 2020-07-15 -- 17:49
 * @author linjiang
 */
object EventDebugUtil {
    fun debug(content: String) {
        // debugInner(null, content)
    }

    @Synchronized
    fun debugReturn(result: Boolean): Boolean {
        val location = genLocation(2)
        android.util.Log.v("linjiang, $result", "$location, ")
        return result
    }

    @Synchronized
    fun debugReturn(result: Boolean, content: String): Boolean {
        val location = genLocation(2)
        android.util.Log.v("linjiang, $result", "$location, $content")
        return result
    }

    private fun genLocation(level: Int): String {
        val caller = Throwable().stackTrace[level]
        var clazzName = caller.className
        clazzName = clazzName.substring(clazzName.lastIndexOf('.') + 1)

        return String.format(
            Locale.CHINA, "%s.%s(L:%d)", clazzName, caller.methodName,
            caller.lineNumber
        )
    }
}