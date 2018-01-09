package com.felix.lessplus.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.felix.lessplus.R
import com.felix.lessplus.ui.activity.MusicListActivity
import com.felix.lessplus.ui.adapter.MusicMultiAdapter
import com.felix.lessplus.utils.CommonUtil
import com.felix.lessplus.utils.GlideImageLoader
import com.felix.lessplus.viewmodel.MusicViewModel
import com.youth.banner.Banner
import kotlinx.android.synthetic.main.fragment_music.*

/**
 * Created by liuhaiyang on 2017/12/7.
 */
class MusicFragment : BaseFragment(), MusicMultiAdapter.OnItemImgClickListener {

    private var mMusicViewModel: MusicViewModel? = null
    private var mAdapter: MusicMultiAdapter? = null

    private var mBanner: Banner? = null

    override fun setContentLayout(): Int = R.layout.fragment_music

    override fun initData(bundle: Bundle?) {
        mMusicViewModel = ViewModelProviders.of(this).get(MusicViewModel::class.java)

        initRv()
        loadBanner()
        loadMusicAlbum()
    }

    private fun initRv() {
        mAdapter = MusicMultiAdapter(mContext!!)
        mAdapter!!.addListener(this)
        val mHeadView = View.inflate(mContext, R.layout.view_head_banner, null)
        mBanner = mHeadView?.findViewById(R.id.vBanner)
        mAdapter!!.setHeaderView(mHeadView!!)
        vRvList.layoutManager = LinearLayoutManager(mContext)
        vRvList.adapter = mAdapter
    }

    /**
     * banner data
     */
    private fun loadBanner() {
        mMusicViewModel?.loadBanner()?.observe(this, Observer { bannerData ->
            val mImages = ArrayList<String>()
            if (bannerData?.result != null && bannerData.result?.focus != null && bannerData.result?.focus?.result != null) {
                for (res in bannerData.result!!.focus!!.result!!) {
                    res.randpic?.let { mImages.add(it) }
                }
            }

            mBanner!!.setImages(mImages).setImageLoader(GlideImageLoader()).start()
        })
    }

    /**
     * music album data
     */
    private fun loadMusicAlbum() {
        mMusicViewModel?.loadMusicAlbum()?.observe(this, Observer { albumData ->
            mAdapter!!.setAdapterData(albumData!!)
            progressBar.visibility = View.GONE
        })

    }

    /**
     * item click
     */
    override fun onImgClick(type: Int) {
        val intent = Intent(context, MusicListActivity::class.java)
        intent.putExtra(CommonUtil.KEY_TYPE, type)
        startActivity(intent)
    }
}