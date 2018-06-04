package com.pager.swipe.adapter

import android.content.Context
import com.yline.view.recycler.simple.SimpleHeadFootRecyclerAdapter

/**
 * Created by yline on 2018/6/1.
 */
class SwipeCommonAdapter(context: Context) : SimpleHeadFootRecyclerAdapter(context) {
    fun isDataEmpty(): Boolean {
        return dataList.isEmpty()
    }
}