package com.yline.opengl.utils

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 资源读取
 * @author yline 2021/2/23 -- 8:34 PM
 */
object ResourceReadUtil {
    fun readTextFileFromResource(context: Context, resourceId: Int): String {
        val body = StringBuilder()
        val inputStream = context.resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var nextLine: String?
        try {
            while (bufferedReader.readLine().also { nextLine = it } != null) {
                body.append(nextLine)
                body.append('\n')
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
                inputStreamReader.close()
                bufferedReader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return body.toString()
    }
}