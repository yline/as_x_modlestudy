package com.pager.swipe.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.pager.swipe.R
import com.pager.swipe.adapter.SwipeCommonAdapter
import com.yline.application.SDKManager
import com.yline.test.StrConstant

/**
 * 第二个界面
 * @author yline 2018/6/1 -- 18:19
 * @version 1.0.0
 */
class SwipeSecondLayout : RelativeLayout {
    private lateinit var mSwipeAdapter: SwipeCommonAdapter
    private lateinit var mRefreshLayout: SwipeRefreshLayout

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_swipe_second, this, true)

        val recyclerView = view.findViewById(R.id.view_swipe_second_recycler) as RecyclerView
        mSwipeAdapter = SwipeCommonAdapter(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mSwipeAdapter

        mRefreshLayout = view.findViewById(R.id.view_swipe_second_refresh) as SwipeRefreshLayout
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, android.R.color.holo_red_light),
                ContextCompat.getColor(context, android.R.color.black),
                ContextCompat.getColor(context, android.R.color.holo_blue_bright))

        initViewClick()
    }

    private fun initViewClick() {
        mRefreshLayout.setOnRefreshListener {
            doRefreshLoad()
        }
    }

    private fun doRefreshLoad() {
        mRefreshLayout.isRefreshing = true
        SDKManager.getHandler().postDelayed({
            mRefreshLayout.isRefreshing = false
            doLoadFirst()
        }, 3000)
    }

    fun doLoadFirst() {
        mSwipeAdapter.setDataList(StrConstant.getListRandom(30), true)
    }

    fun isDataEmpty(): Boolean {
        return mSwipeAdapter.isDataEmpty()
    }
}