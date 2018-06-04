package com.pager.swipe

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import com.pager.swipe.view.SwipeFirstLayout
import com.pager.swipe.view.SwipeSecondLayout
import com.yline.base.BaseActivity
import com.yline.view.recycler.adapter.AbstractPagerAdapter
import java.util.*

/**
 * 入口
 * @author yline 2018/6/1 -- 17:52
 * @version 1.0.0
 */
class MainActivity : BaseActivity() {
    companion object {
        const val TAB_MAX_COUNT = 2
    }

    // View
    private lateinit var mFirstLayout: SwipeFirstLayout
    private lateinit var mSecondLayout: SwipeSecondLayout

    // Data
    private var mCurrentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()
    }

    private fun initView() {
        // View
        mFirstLayout = SwipeFirstLayout(this@MainActivity)
        mSecondLayout = SwipeSecondLayout(this@MainActivity)

        // TabLayout
        val tabLayout = findViewById(R.id.main_tab) as TabLayout
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.tabGravity = TabLayout.GRAVITY_CENTER

        // ViewPager
        val ordersPagerAdapter = OrdersPagerAdapter()
        val viewPager = findViewById(R.id.main_view_pager) as ViewPager
        viewPager.offscreenPageLimit = TAB_MAX_COUNT
        viewPager.adapter = ordersPagerAdapter

        // tabLayout + ViewPager
        tabLayout.setupWithViewPager(viewPager)

        // 数据
        val titleList = mutableListOf<String>("山水", "儿子")
        val viewList = mutableListOf<View>(mFirstLayout, mSecondLayout)
        ordersPagerAdapter.setData(titleList, viewList)

        // 默认展示Item
        tabLayout.getTabAt(mCurrentPosition)?.select()

        // 设置点击事件
        initViewClick(viewPager)
    }

    private fun initViewClick(viewPager: ViewPager) {
        // ViewPager切换
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mCurrentPosition = position

                val isRefresh = isDataEmpty(position)
                if (isRefresh) {
                    doLoadFirst(position)
                }
            }
        })
    }

    private fun initData() {
        doLoadFirst(mCurrentPosition)
    }

    private fun doLoadFirst(position: Int) {
        when (position) {
            0 -> mFirstLayout.doLoadFirst()
            1 -> mSecondLayout.doLoadFirst()
            else -> mFirstLayout.doLoadFirst()
        }
    }

    private fun isDataEmpty(position: Int): Boolean {
        when (position) {
            0 -> return mFirstLayout.isDataEmpty()
            1 -> return mSecondLayout.isDataEmpty()
            else -> return true
        }
    }

    private class OrdersPagerAdapter : AbstractPagerAdapter() {
        private var mTitleList = ArrayList<String>()

        override fun getPageTitle(position: Int): CharSequence {
            return if (position < mTitleList.size) mTitleList[position] else ""
        }

        fun setData(titleList: MutableList<String>, viewList: MutableList<View>) {
            if (titleList.size == viewList.size) {
                mTitleList = ArrayList(titleList)
                setViews(viewList, true)
            }
        }
    }
}