package com.manager.apn

import android.content.ContentValues
import android.content.Context
import android.net.Uri

/**
 * APN 管理
 * @author yline 2018/6/5 -- 14:08
 * @version 1.0.0
 */
object APNManager {
    /**
     * 所有的APN配配置信息位置;用于查找
     */
    private val APN_TABLE_URI = Uri.parse("content://telephony/carriers")

    /**
     * 当前的APN位置;用于更新
     */
    private val PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn")

    /**
     * 限定符,限定只查找这几项;本工具去掉
     */
    private val PROJECTION = arrayOf("_id", "apn", "type", "current", "proxy", "port", "name")

    /**
     * 打印所有APN
     */
    @Deprecated("需要权限{android.permission.WRITE_APN_SETTINGS}, 高版本一定异常")
    fun getAPNList(context: Context): ArrayList<HashMap<String, String>> {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(APN_TABLE_URI, null, null, null, null)

        val resultList = ArrayList<HashMap<String, String>>()
        if (null != cursor && cursor.moveToFirst()) {
            do {
                val hashMap = HashMap<String, String>()

                val columnNameArray = cursor.columnNames
                for (columnName in columnNameArray) {
                    val value = cursor.getString(cursor.getColumnIndex(columnName))
                    hashMap[columnName] = value
                }

                resultList.add(hashMap)
            } while (cursor.moveToNext())
            cursor.close()
        }

        return resultList
    }

    /**
     * 打印可用的APN
     */
    @Deprecated("需要权限{android.permission.WRITE_APN_SETTINGS}, 高版本一定异常")
    fun getAvailableAPNList(context: Context): ArrayList<HashMap<String, String>> {
        // current不为空表示可以使用的APN
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(APN_TABLE_URI, null, "current is not null", null, null)

        val resultList = ArrayList<HashMap<String, String>>()
        if (null != cursor && cursor.moveToFirst()) {
            do {
                val hashMap = HashMap<String, String>()

                val columnNameArray = cursor.columnNames
                for (columnName in columnNameArray) {
                    val value = cursor.getString(cursor.getColumnIndex(columnName))
                    hashMap[columnName] = value
                }

                resultList.add(hashMap)
            } while (cursor.moveToNext())
            cursor.close()
        }

        return resultList
    }

    /**
     * 打印当前APN,部分信息
     */
    @Deprecated("需要权限{android.permission.WRITE_APN_SETTINGS}, 高版本一定异常")
    fun getCurrentApn(context: Context): ArrayList<HashMap<String, String>> {
        val contentResolver = context.contentResolver
        // val PROJECTION = String[] { "_id" };
        val cursor = contentResolver.query(PREFERRED_APN_URI, PROJECTION, null, null, null)

        val resultList = ArrayList<HashMap<String, String>>()
        if (null != cursor && cursor.moveToFirst()) {
            do {
                val hashMap = HashMap<String, String>()

                val columnNameArray = cursor.columnNames
                for (columnName in columnNameArray) {
                    val value = cursor.getString(cursor.getColumnIndex(columnName))
                    hashMap[columnName] = value
                }

                resultList.add(hashMap)
            } while (cursor.moveToNext())
            cursor.close()
        }

        return resultList
    }

    /**
     * 并获取 当前APN id
     */
    @Deprecated("需要权限{android.permission.WRITE_APN_SETTINGS}, 高版本一定异常")
    fun getCurrentApnId(context: Context): String {
        val contentResolver = context.contentResolver
        val projection = arrayOf("_id")
        val cursor = contentResolver.query(PREFERRED_APN_URI, projection, null, null, null)

        var apnId = ""
        if (null != cursor && cursor.moveToFirst()) {
            apnId = cursor.getString(cursor.getColumnIndex("_id"))
            cursor.close()
        }

        return apnId
    }

    /**
     * 打印WAP APN的部分信息
     */
    @Deprecated("需要权限{android.permission.WRITE_APN_SETTINGS}, 高版本一定异常")
    fun getWapApnId(context: Context): ArrayList<HashMap<String, String>> {
        val contentResolver = context.contentResolver
        // 查询cmwapAPN
        val cursor = contentResolver.query(APN_TABLE_URI, PROJECTION, "apn = \'cmwap\' and current = 1", null, null)
        // wap APN 端口不为空
        val resultList = ArrayList<HashMap<String, String>>()
        if (null != cursor && cursor.moveToFirst()) {
            do {
                val hashMap = HashMap<String, String>()

                val columnNameArray = cursor.columnNames
                for (columnName in columnNameArray) {
                    val value = cursor.getString(cursor.getColumnIndex(columnName))
                    hashMap[columnName] = value
                }

                resultList.add(hashMap)
            } while (cursor.moveToNext())
            cursor.close()
        }

        return resultList
    }

    /**
     * 切换apn需要一定时间，需要等待几秒，与机子性能有关
     */
    @Deprecated("需要权限{android.permission.WRITE_APN_SETTINGS}, 高版本一定异常")
    fun switchApnByName(context: Context, name: String): Boolean {
        val cursor = context.contentResolver.query(APN_TABLE_URI, null, "name = ?", arrayOf(name), null)
        if (null != cursor && cursor.moveToFirst()) {
            val newApnId = cursor.getString(cursor.getColumnIndex("_id"))
            val rowId = setCurrentApn(context, newApnId)
            cursor.close()
            return (rowId != -1)
        }
        return false
    }

    /**
     * 设置当前APN
     * @param newApnId 新的ApnId
     * @return row 返回的行数
     */
    @Deprecated("需要权限{android.permission.WRITE_APN_SETTINGS}, 高版本一定异常")
    fun setCurrentApn(context: Context, newApnId: String): Int {
        val resolver = context.contentResolver
        val values = ContentValues()
        values.put("apn_id", newApnId)
        return resolver.update(PREFERRED_APN_URI, values, null, null)
    }
}