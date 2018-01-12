package com.felix.lessplus.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.felix.lessplus.R
import com.felix.lessplus.model.bean.*
import com.felix.lessplus.utils.CommonUtil
import com.felix.lessplus.utils.GlideImageLoader
import com.felix.lessplus.utils.StatusBarUtil
import com.felix.lessplus.utils.StatusBarUtil.Companion.getStatusBarHeight
import com.felix.lessplus.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.layout_movie_head.*

class MovieDetailActivity : BaseActivity(), NestedScrollView.OnScrollChangeListener {

    private var mViewModel: MovieViewModel? = null
    private var mAdapter: ListAdapter? = null

    private var slidingDistance: Int = 0
    //高斯图背景的高度
    private var imageBgHeight: Int = 0

    private var mMovieResponse: HotMovieResponse.SubjectsBean? = null

    override fun setContentLayout(): Int = R.layout.activity_movie_detail

    override fun initData(bundle: Bundle?) {
        setTitleBar()

        mMovieResponse = intent.getSerializableExtra(CommonUtil.KEY_DATA) as HotMovieResponse.SubjectsBean?
        mViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        mAdapter = ListAdapter()
        vRlCast.isNestedScrollingEnabled = false
        vRlCast.setHasFixedSize(false)
        vRlCast.isFocusable = false
        vRlCast.layoutManager = LinearLayoutManager(this)
        vRlCast.adapter = mAdapter
        //item click
        mAdapter!!.setOnItemClickListener { _, _, position ->

            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(CommonUtil.KEY_URL, mAdapter?.getItem(position)?.alt)
            startActivity(intent)

        }

        initSlideShapeTheme()

        setHeadData()
        loadList()
    }

    private fun loadList() {
        mViewModel?.loadMovieDetail(mMovieResponse?.id!!)?.observe(this@MovieDetailActivity, Observer { data ->
            setHeadData(data)
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

    private fun setHeadData(detailData: MovieDetailResponse?) {

        vTvMovieHeadRatePeople.text = getString(R.string.movie_rate_count, detailData?.ratings_count)
        vTvMovieHeadDate.text = getString(R.string.movie_date, detailData?.year)
        vTvMovieHeadCity.text = getString(R.string.movie_city, CommonUtil.formatGenres(detailData?.countries))

        vTvAlias.text = detailData?.summary

        //合并 导演 演员
        for (cast in mMovieResponse?.casts!!) {
            cast.type = getString(R.string.movie_cast)
        }

        for (cast in mMovieResponse?.directors!!) {
            cast.type = getString(R.string.movie_director)
        }

        mAdapter!!.addData(mMovieResponse?.directors!!)
        mAdapter!!.addData(mMovieResponse?.casts!!)
    }

    private fun setHeadData() {
        if (mMovieResponse != null) {
            GlideImageLoader.displayImageWithBlur(this, mMovieResponse?.images?.medium!!, vIvMovieHeadBg)
            GlideImageLoader.displayImageWithBlur(this, mMovieResponse?.images?.medium!!, vIvTitleBg)

            GlideImageLoader.displayImage(this, mMovieResponse?.images?.large, vIvMovieHeadPhoto)
            vToolBar.title = mMovieResponse?.title
            vTvMovieHeadRate.text = getString(R.string.movie_rate, mMovieResponse?.rating?.average)
            vTvMovieHeadDirectors.text = getString(R.string.movie_directors, CommonUtil.formatName(mMovieResponse?.directors))
            vTvMovieHeadCasts.text = CommonUtil.formatName(mMovieResponse?.casts)
            vTvMovieHeadGenres.text = getString(R.string.movie_genres, CommonUtil.formatGenres(mMovieResponse?.genres))
        }
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

        val imgItemBgparams = vIvMovieHeadBg.layoutParams
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


    inner class ListAdapter : BaseQuickAdapter<HotMovieResponse.SubjectsBean.PersonBean, BaseViewHolder>(R.layout.item_movie_detail_preson) {

        override fun convert(helper: BaseViewHolder, item: HotMovieResponse.SubjectsBean.PersonBean) {
            GlideImageLoader.displayImage(this@MovieDetailActivity, item.avatars?.large, helper.getView(R.id.iv_avatar))
            helper.setText(R.id.tv_name, item.name)
            helper.setText(R.id.tv_type, item.type)
        }
    }

}
