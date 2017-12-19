package com.felix.lessplus.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.felix.lessplus.R
import com.felix.lessplus.utils.GlideImageLoader
import com.felix.lessplus.utils.StatusBarUtil
import com.felix.lessplus.utils.StatusBarUtil.Companion.getStatusBarHeight
import kotlinx.android.synthetic.main.activity_music_list.*
import kotlinx.android.synthetic.main.layout_music_list_head.*
import java.util.ArrayList

class MusicListActivity : BaseActivity(), NestedScrollView.OnScrollChangeListener {

    private var mAdapter: ListAdapter? = null
    private var slidingDistance: Int = 0
    //高斯图背景的高度
    private var imageBgHeight: Int = 0

    private var mImgUrl: String = "http://a.hiphotos.baidu.com/ting/pic/item/09fa513d269759ee4764e3adb1fb43166d22dfa4.jpg"

    override fun setContentLayout(): Int = R.layout.activity_music_list

    override fun initData(bundle: Bundle?) {
        setTItelBar()

        mAdapter = ListAdapter()
        val list = ArrayList<String>()
        for (i in 0..49) {
            list.add("AAA")
        }
        vRlmusic.isNestedScrollingEnabled = false
        vRlmusic.setHasFixedSize(false)
        vRlmusic.isFocusable = false
        vRlmusic.layoutManager = LinearLayoutManager(this)
        vRlmusic.adapter = mAdapter
        mAdapter!!.setNewData(list)

        initSlideShapeTheme()
    }

    /**
     * scroll view change
     */
    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        scrollChangeHeader(scrollY)
    }

    private fun setTItelBar() {
        setSupportActionBar(vToolBar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        vToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * 初始化滑动渐变
     */
    private fun initSlideShapeTheme() {
        GlideImageLoader.displayImageWithBlur(this, mImgUrl, vIvHeadBg)
        GlideImageLoader.displayImageWithBlur(this, mImgUrl, vIvTitleBg)

        // toolbar的高度
        val toolbarHeight = vToolBar.layoutParams.height
        // toolbar+状态栏的高度
        val headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this)

        // 使背景图向上移动到图片的最底端，保留toolbar+状态栏的高度
        val params = vIvTitleBg.layoutParams
        val ivTitleHeadBgParams = vIvTitleBg.layoutParams as ViewGroup.MarginLayoutParams
        val marginTop = params.height - headerBgHeight
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0)

        // 为头部是View的界面设置状态栏透明
        StatusBarUtil.setTranslucentImageHeader(this, 0, vToolBar)

        val imgItemBgparams = vIvHeadBg.layoutParams
        // 获得高斯图背景的高度
        imageBgHeight = imgItemBgparams.height

        // 监听改变透明度
        vNvList.setOnScrollChangeListener(this)
        val titleBarAndStatusHeight = (resources.getDimension(R.dimen.nav_bar_height) + getStatusBarHeight(this)).toInt()
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - resources.getDimension(R.dimen.nav_bar_height_more).toInt()
    }

    /**
     * 根据页面滑动距离改变Header透明度方法
     */
    private fun scrollChangeHeader(y: Int) {
        if (vIvTitleBg.visibility == View.GONE) {
            vIvTitleBg.visibility = View.VISIBLE
        }
        var scrolledY = y

        if (scrolledY < 0) {
            scrolledY = 0
        }
        val alpha = Math.abs(scrolledY) * 1.0f / slidingDistance
        val drawable = vIvTitleBg.drawable

        if (drawable != null) {
            if (scrolledY <= slidingDistance) {
                // title部分的渐变
                vIvTitleBg.drawable.alpha = (alpha * 255).toInt()
            } else {
                vIvTitleBg.drawable.alpha = 255
            }
        }
    }

    inner class ListAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_music_list) {

        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.tv_title, item)
        }
    }

}
