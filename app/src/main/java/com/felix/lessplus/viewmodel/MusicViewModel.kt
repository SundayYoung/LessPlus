package com.felix.lessplus.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.felix.lessplus.data.repository.MusicRepository
import com.felix.lessplus.model.bean.BannerResponse
import com.felix.lessplus.model.bean.MusicDownLoadInfo
import com.felix.lessplus.model.bean.MusicListResponse
import com.felix.lessplus.model.bean.MusicResponse
import com.felix.lessplus.ui.LessPlusApplication
import javax.inject.Inject

/**
 * Created by liuhaiyang on 2017/12/19.
 */
class MusicViewModel : ViewModel() {

    @Inject lateinit var mMusicRepository: MusicRepository

    private var mBannerData: LiveData<BannerResponse>? = null
    private var mMusicAlbumData: LiveData<List<MusicResponse>>? = null
    private var mMusicListData: LiveData<MusicListResponse>? = null

    private var mMusicDownInfoData: LiveData<MusicDownLoadInfo>? = null

    init {
        LessPlusApplication.mAppComponent.inject(this)
    }


    fun loadBanner(): LiveData<BannerResponse>? {
        mBannerData = null
        mBannerData = MutableLiveData<BannerResponse>()
        mBannerData = mMusicRepository.getBanner()
        return mBannerData
    }

    fun loadMusicAlbum(): LiveData<List<MusicResponse>>? {
        mMusicAlbumData = null
        mMusicAlbumData = MutableLiveData<List<MusicResponse>>()
        mMusicAlbumData = mMusicRepository.getMusicAlbum()
        return mMusicAlbumData
    }

    fun loadMusicList(type: Int, offset: Int): LiveData<MusicListResponse>? {
        mMusicListData = null
        mMusicListData = MutableLiveData<MusicListResponse>()
        mMusicListData = mMusicRepository.getMusicList(type, offset)
        return mMusicListData
    }

    fun loadMusicDownLoadInfo(songId: String?): LiveData<MusicDownLoadInfo>? {
        mMusicDownInfoData = null
        mMusicDownInfoData = MutableLiveData<MusicDownLoadInfo>()
        mMusicDownInfoData = mMusicRepository.getMusicDownLoadInfo(songId)
        return mMusicDownInfoData
    }

}