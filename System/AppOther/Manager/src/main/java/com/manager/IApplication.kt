package com.manager

import com.yline.application.BaseApplication
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * 入口
 * @author yline 2018/6/5 -- 13:45
 * @version 1.0.0
 */
class IApplication : BaseApplication() {
    companion object {
        private var sFixedThreadPool: ExecutorService? = null

        fun fixedThreadExecutor(runnable: Runnable) {
            if (null == sFixedThreadPool) {
                sFixedThreadPool = Executors.newFixedThreadPool(5)
            }
            sFixedThreadPool?.execute(runnable)
        }

        fun <T> fixedThreadExecutor(callable: Callable<T>): Future<T> {
            if (null == sFixedThreadPool) {
                sFixedThreadPool = Executors.newFixedThreadPool(5)
            }
            return sFixedThreadPool!!.submit(callable)
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}