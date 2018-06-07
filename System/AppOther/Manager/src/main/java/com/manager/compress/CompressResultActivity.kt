package com.manager.compress

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.manager.R
import com.yline.base.BaseActivity
import com.yline.view.fresco.FrescoManager
import com.yline.view.fresco.view.FrescoView
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter
import com.yline.view.recycler.holder.RecyclerViewHolder
import java.io.File

/**
 * 实现压缩，并展现结果
 * @author yline 2018/6/6 -- 19:18
 * @version 1.0.0
 */
class CompressResultActivity : BaseActivity() {
    companion object {
        private const val PATH_LIST = "showPathList"

        fun launcher(context: Context, showPathList: ArrayList<String>) {
            val intent = Intent()
            intent.setClass(context, CompressResultActivity::class.java)
            intent.putExtra(PATH_LIST, showPathList)
            context.startActivity(intent)
        }
    }

    private lateinit var mShowPathList: List<String>
    private lateinit var mRecyclerAdapter: CompressResultRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compress_result)

        mShowPathList = intent.getStringArrayListExtra(PATH_LIST)

        initView()
        initData()
    }

    private fun initView() {
        mRecyclerAdapter = CompressResultRecyclerAdapter()

        val recyclerView = findViewById(R.id.compress_result_recycler) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this@CompressResultActivity)
        recyclerView.adapter = mRecyclerAdapter
    }

    private fun initData() {
        mRecyclerAdapter.setDataList(mShowPathList, true)
    }

    // 传入原始
    private class CompressResultRecyclerAdapter : AbstractRecyclerAdapter<String>() {
        override fun getItemRes(): Int {
            return R.layout.item_compress_result
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.setText(R.id.item_compress_result_info, computeInfo(get(position)))

            val frescoView = holder.get<FrescoView>(R.id.item_compress_result_fresco)
            FrescoManager.setImageLocal(frescoView, get(position), null)
        }

        private fun computeInfo(path: String): String {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inSampleSize = 1
            BitmapFactory.decodeFile(path, options)

            val line1 = "图片路径：$path"
            val line2 = String.format("图片参数：%d * %d，%dk", options.outWidth, options.outHeight, (File(path).length() shr 10))
            return "$line1 \n$line2"
        }
    }
}