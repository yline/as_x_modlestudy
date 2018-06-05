package com.manager.apn

import com.manager.IApplication
import com.yline.utils.LogUtil

/**
 * APN Manager 的测试类
 * @author yline 2018/6/5 -- 14:37
 * @version 1.0.0
 */
object APNManagerTest {
    fun test() {
        IApplication.fixedThreadExecutor(Runnable {
            //            val apnList = APNManager.getAPNList(SDKManager.getApplication())
//            log(apnList, "All APNs")
//
//            val availableAPNList = APNManager.getAvailableAPNList(SDKManager.getApplication())
//            log(availableAPNList, "Available APNs")
//
//            val currentApn = APNManager.getCurrentApn(SDKManager.getApplication())
//            log(currentApn, "Currents APNs")
//
//            val currentApnId = APNManager.getCurrentApnId(SDKManager.getApplication())
//            LogUtil.v("-------------------getCurrentApnId---------------------")
//            LogUtil.v("-------------------$currentApnId---------------------")
//
//            val wapApnId = APNManager.getWapApnId(SDKManager.getApplication())
//            log(wapApnId, "Wap APNs")
//
//            val switchApnByName = APNManager.switchApnByName(SDKManager.getApplication(), "公安接入点")
//            LogUtil.v("-------------------switchApnByName---------------------")
//            LogUtil.v("-------------------$switchApnByName---------------------")
//
//            val newCurrentApn = APNManager.setCurrentApn(SDKManager.getApplication(), "-1")
//            LogUtil.v("-------------------setCurrentApn---------------------")
//            LogUtil.v("-------------------$newCurrentApn---------------------")
            LogUtil.v("-------------------都需要权限---------------------")
        })
    }

    private fun log(apnList: List<HashMap<String, String>>, tag: String) {
        LogUtil.v("-------------------$tag---------------------")
        for (map in apnList) {
            val stringBuilder = StringBuilder()

            var value: String?
            for (key in map.keys) {
                value = map[key]
                stringBuilder.append("$key - $value && ")
            }

            LogUtil.v(stringBuilder.toString())
        }
    }
}