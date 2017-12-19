package com.felix.lessplus.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.felix.lessplus.data.repository.MusicRepository
import com.felix.lessplus.model.bean.BannerResponse
import com.felix.lessplus.model.bean.MusicResponse
import com.felix.lessplus.ui.LessPlusApplication
import javax.inject.Inject

/**
 * Created by liuhaiyang on 2017/12/19.
 */
class MusicViewModel : ViewModel() {

    @Inject lateinit var mMusicRepository: MusicRepository

    private var mBannerData: LiveData<List<BannerResponse>>? = null
    private var mMusicListData: LiveData<List<MusicResponse>>? = null

    init {
        LessPlusApplication.mAppComponent.inject(this)
    }


    fun loadBanner(): LiveData<List<BannerResponse>>? {
        mBannerData = null
        mBannerData = MutableLiveData<List<BannerResponse>>()
        mBannerData = mMusicRepository.getBanner()
        return mBannerData
    }

    fun loadMusicAlbum(): LiveData<List<MusicResponse>>? {
        mMusicListData = null
        mMusicListData = MutableLiveData<List<MusicResponse>>()
        mMusicListData = mMusicRepository.getMusicAlbum()
        return mMusicListData
    }

}