package com.swipe.menu

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.swipe.menu.view.SwipeMenuLayout
import com.yline.application.SDKManager
import com.yline.base.BaseActivity
import com.yline.test.StrConstant
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter
import com.yline.view.recycler.holder.RecyclerViewHolder

/**
 *
 * @author yline 2018/6/27 -- 11:22
 * @version 1.0.0
 */
class MainActivity : BaseActivity() {
    private lateinit var mRecyclerAdapter: MainRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerAdapter = MainRecyclerAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.main_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = mRecyclerAdapter

        initData()
    }

    private fun initData() {
        mRecyclerAdapter.setDataList(StrConstant.getListRandom(40), true)
    }

    private class MainRecyclerAdapter : AbstractRecyclerAdapter<String>() {
        override fun getItemRes(): Int {
            return R.layout.item_main
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            val swipeLayout = holder.get<SwipeMenuLayout>(R.id.item_main_swipe)
            swipeLayout.isLeftSwipe = (position % 2 == 0)

            holder.setText(R.id.item_main_content, if (position % 2 == 0) (get(position) + "-右") else (get(position) + "-左"))
            holder.get<View>(R.id.item_main_content).setOnLongClickListener {
                SDKManager.toast("长按$position")
                true
            }
            holder.setOnClickListener(R.id.item_main_content) {
                SDKManager.toast("内容$position")
            }

            holder.setOnClickListener(R.id.item_main_top) {
                SDKManager.toast("置顶$position")
            }

            holder.get<View>(R.id.item_main_unread).visibility = if ((position % 2 == 0)) View.VISIBLE else View.GONE
            holder.setOnClickListener(R.id.item_main_unread) {
                SDKManager.toast("标记未读$position")
            }

            holder.setOnClickListener(R.id.item_main_delete) {
                SDKManager.toast("删除$position")
            }
        }
    }
}