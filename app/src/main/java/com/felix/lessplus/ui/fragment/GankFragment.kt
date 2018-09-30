package com.felix.lessplus.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.felix.lessplus.R
import com.felix.lessplus.model.bean.GankIoResponse
import com.felix.lessplus.ui.activity.WebViewActivity
import com.felix.lessplus.utils.CommonUtil
import com.felix.lessplus.utils.GlideImageLoader
import com.felix.lessplus.viewmodel.GankViewModel
import kotlinx.android.synthetic.main.fragment_gank.*
import java.util.ArrayList

/**
 * Created by liuhaiyang on 2018/1/10.
 */
class GankFragment : BaseFragment() {

    private var mViewModel: GankViewModel? = null
    private var mAdapter: ListAdapter? = null

    private var mUserHintFirst = true
    private var mPage: Int = 1
    private var mResponseList: MutableList<GankIoResponse.ResultBean>? = null

    override fun setContentLayout(): Int = R.layout.fragment_gank

    override fun initData(bundle: Bundle?) {
        mViewModel = ViewModelProviders.of(this).get(GankViewModel::class.java)
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
        vRvGank.layoutManager = LinearLayoutManager(mContext)
        vRvGank.adapter = mAdapter
        mAdapter!!.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        //item click
        mAdapter!!.setOnItemClickListener { _, _, position ->

            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(CommonUtil.KEY_URL, mAdapter!!.getItem(position)?.url)
            startActivity(intent)

        }
        //load more
        mAdapter?.setOnLoadMoreListener({
            mPage++
            loadData()
        }, vRvGank)

        mResponseList = ArrayList()
        //refresh
        vSRLayout.setColorSchemeColors(ContextCompat.getColor(mContext!!, R.color.colorPrimaryDark))
        vSRLayout.setOnRefreshListener({
            (mResponseList as ArrayList<GankIoResponse.ResultBean>).clear()
            mPage = 1
            loadData()
        })
    }

    /**
     * gank data
     */
    private fun loadData() {
        mViewModel?.loadGankAndoid(mPage)?.observe(this, Observer { data ->

            vSRLayout.isRefreshing = false
            mResponseList?.addAll(data?.results!!)

            mResponseList?.let { mAdapter!!.setNewData(it) }
            if (data?.results!!.size < 20) {
                mAdapter!!.loadMoreEnd(false)
            } else {
                mAdapter!!.loadMoreComplete()
            }
            if (vPbLoad.visibility == View.VISIBLE) {
                vPbLoad.visibility = View.GONE
            }
        })

    }

    inner class ListAdapter : BaseQuickAdapter<GankIoResponse.ResultBean, BaseViewHolder>(R.layout.item_gank_list) {

        override fun convert(helper: BaseViewHolder, item: GankIoResponse.ResultBean) {
            val vImage: ImageView = helper.getView(R.id.iv_android_pic)
            if (item.images != null && item.images!!.isNotEmpty()) {
                vImage.visibility = View.VISIBLE
                GlideImageLoader.displayImageAsBitmap(mContext, item.images!![0], vImage)
            } else {
                vImage.visibility = View.GONE
            }

            helper.setText(R.id.tv_android_des, item.desc)
            helper.setText(R.id.tv_android_who, if (TextUtils.isEmpty(item.who)) getString(R.string.gank_default_name) else item.who)

        }
    }
}