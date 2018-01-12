package com.felix.lessplus.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.felix.lessplus.R
import com.felix.lessplus.model.bean.HotMovieResponse
import com.felix.lessplus.ui.activity.MovieDetailActivity
import com.felix.lessplus.utils.CommonUtil
import com.felix.lessplus.utils.GlideImageLoader
import com.felix.lessplus.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import android.support.v4.app.ActivityOptionsCompat
import android.widget.ImageView


/**
 * Created by liuhaiyang on 2018/1/11.
 */
class MovieFragment : BaseFragment() {

    private var mViewModel: MovieViewModel? = null
    private var mAdapter: ListAdapter? = null

    private var mUserHintFirst = true

    override fun setContentLayout(): Int = R.layout.fragment_movie

    override fun initData(bundle: Bundle?) {
        mViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && mUserHintFirst) {
            initView()
            loadData()
            mUserHintFirst = false
        }
    }

    private fun initView() {
        mAdapter = ListAdapter()
        vRvMovie.layoutManager = LinearLayoutManager(mContext)
        vRvMovie.adapter = mAdapter
        mAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
        //item click
        mAdapter!!.setOnItemClickListener { _, _, position ->

            val response : HotMovieResponse.SubjectsBean = mAdapter!!.getItem(position)!!
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(CommonUtil.KEY_DATA, response)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, mAdapter?.getViewByPosition(vRvMovie, position, R.id.vIvPhoto)!! as ImageView, getString(R.string.share_img))

            startActivity(intent, options.toBundle())

        }
    }

    /**
     * data
     */
    private fun loadData() {
        mViewModel?.loadHotMovie()?.observe(this, Observer { data ->

            if (data?.subjects != null) {
                mAdapter!!.setNewData(data.subjects)
            }
            if (vPbLoad.visibility == View.VISIBLE) {
                vPbLoad.visibility = View.GONE
            }
        })

    }

    inner class ListAdapter : BaseQuickAdapter<HotMovieResponse.SubjectsBean, BaseViewHolder>(R.layout.item_movie_list) {

        override fun convert(helper: BaseViewHolder, item: HotMovieResponse.SubjectsBean) {

            helper.setText(R.id.vTvTitle, item.title)
            GlideImageLoader.displayImage(mContext, item.images?.large, helper.getView(R.id.vIvPhoto))

            helper.setText(R.id.vTvDirectors, getString(R.string.movie_directors, CommonUtil.formatName(item.directors)))
            helper.setText(R.id.vTvCasts, getString(R.string.movie_casts, CommonUtil.formatName(item.casts)))
            helper.setText(R.id.vTvGenres, getString(R.string.movie_genres, CommonUtil.formatGenres(item.genres)))
            helper.setText(R.id.vTvRate, getString(R.string.movie_rate, item.rating?.average))

        }
    }
}