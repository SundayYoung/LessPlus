package com.felix.lessplus.data.repository

import android.arch.lifecycle.LiveData
import com.felix.lessplus.data.remote.MusicRemoteData
import com.felix.lessplus.model.bean.BannerResponse
import com.felix.lessplus.model.bean.MusicListResponse
import com.felix.lessplus.model.bean.MusicResponse
import javax.inject.Inject


/**
 * Created by liuhaiyang on 2017/12/8.
 */
class MusicRepository @Inject constructor(private val remoteData: MusicRemoteData) {

    fun getBanner(): LiveData<List<BannerResponse>> {
        return remoteData.loadBanner()
    }

    fun getMusicAlbum(): LiveData<List<MusicResponse>> {
        return remoteData.loadMusicAlbum()
    }

    fun getMusicList(type: Int, offset: Int): LiveData<MusicListResponse> {
        return remoteData.loadMusicList(type, offset)
    }
}