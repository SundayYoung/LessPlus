package com.felix.lessplus.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.felix.lessplus.executor.PlayOnlineMusic
import com.felix.lessplus.R
import com.felix.lessplus.model.bean.Music
import com.felix.lessplus.model.bean.MusicListResponse
import com.felix.lessplus.model.bean.OnLineMusic
import com.felix.lessplus.service.PlayCache
import com.felix.lessplus.service.PlayService
import com.felix.lessplus.utils.CommonUtil
import com.felix.lessplus.utils.GlideImageLoader
import com.felix.lessplus.utils.StatusBarUtil
import com.felix.lessplus.utils.StatusBarUtil.Companion.getStatusBarHeight
import com.felix.lessplus.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.activity_music_list.*
import kotlinx.android.synthetic.main.layout_music_list_head.*
import org.jetbrains.anko.toast

class MusicListActivity : BaseActivity(), NestedScrollView.OnScrollChangeListener {

    private var mMusicViewModel: MusicViewModel? = null
    private var mAdapter: ListAdapter? = null

    private var slidingDistance: Int = 0
    //高斯图背景的高度
    private var imageBgHeight: Int = 0
    //music type
    private var mType: Int = 0
    //music list page
    private var mOffSet: Int = 0

    override fun setContentLayout(): Int = R.layout.activity_music_list

    override fun initData(bundle: Bundle?) {
        setTitleBar()

        mType = intent.getIntExtra(CommonUtil.KEY_TYPE, 2)
        mMusicViewModel = ViewModelProviders.of(this).get(MusicViewModel::class.java)

        mAdapter = ListAdapter()
        mAdapter?.openLoadAnimation()
        mAdapter?.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
        vRlmusic.isNestedScrollingEnabled = false
        vRlmusic.setHasFixedSize(false)
        vRlmusic.isFocusable = false
        vRlmusic.layoutManager = LinearLayoutManager(this)
        vRlmusic.adapter = mAdapter
        mAdapter?.setEnableLoadMore(true)
//        mAdapter?.setOnLoadMoreListener({ loadMore() }, vRlmusic)

        initSlideShapeTheme()

        loadMusicList()
    }

    private fun loadMusicList() {
        mMusicViewModel?.loadMusicList(mType, mOffSet)?.observe(this@MusicListActivity, Observer { musicData ->
            progressBar.visibility = View.GONE
            setHeadData(musicData?.billboard)
            mAdapter!!.setNewData(musicData?.song_list)
            mAdapter!!.setOnItemChildClickListener { _, _, position ->
                toast("Play" + position)
            }
            mAdapter!!.setOnItemClickListener({ _, _, position ->
                play(mAdapter?.getItem(position)!!)
            })
        })
    }

    /**
     * scroll view change
     */
    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        scrollChangeHeader(scrollY)
    }

    private fun setTitleBar() {
        setSupportActionBar(vToolBar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        vToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setHeadData(headData: MusicListResponse.BillBoardData?) {
        vToolBar.title = headData?.name
        vTvTitle.text = getString(R.string.music_head_title, headData?.name)
        vTvUpdateDate.text = getString(R.string.music_head_updateDate, headData?.update_date)
        vTvComment.text = headData?.comment
        GlideImageLoader.displayImage(this, headData?.pic_s192!!, vIvCover)
        GlideImageLoader.displayImageWithBlur(this, headData.pic_s640!!, vIvHeadBg)
        GlideImageLoader.displayImageWithBlur(this, headData.pic_s640!!, vIvTitleBg)
    }

    /**
     * 初始化滑动渐变
     */
    private fun initSlideShapeTheme() {

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


    private fun play(onlineMusic: OnLineMusic) {
        object : PlayOnlineMusic(this, onlineMusic) {

            override fun onPrepare() {
            }

            override fun onExecuteSuccess(music: Music?) {
                PlayCache.instance.mPlayService?.play(music!!)
                println("oncLLLL " + PlayCache.instance.mPlayService)

                toast("播放")
            }

            override fun onExecuteFail(e: Exception) {
                toast("失败")
            }
        }.execute()
    }


    inner class ListAdapter : BaseQuickAdapter<OnLineMusic, BaseViewHolder>(R.layout.item_music_list) {

        override fun convert(helper: BaseViewHolder, item: OnLineMusic) {
            GlideImageLoader.displayImage(this@MusicListActivity, item.pic_big, helper.getView(R.id.iv_cover))
            helper.setText(R.id.tv_title, item.title)
            helper.setText(R.id.tv_artist, getString(R.string.music_list_title_album, item.author, item.album_title))
            helper.addOnClickListener(R.id.iv_more)
        }
    }

}
