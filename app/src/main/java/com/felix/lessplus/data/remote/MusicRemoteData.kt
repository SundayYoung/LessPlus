package com.felix.lessplus.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.felix.lessplus.http.BaseServerResponse
import com.felix.lessplus.http.RestBaseCallBack
import com.felix.lessplus.http.RestService
import com.felix.lessplus.http.UrlConfig
import com.felix.lessplus.model.bean.BannerResponse
import com.felix.lessplus.model.bean.MusicDownLoadInfo
import com.felix.lessplus.model.bean.MusicListResponse
import com.felix.lessplus.model.bean.MusicResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by liuhaiyang on 2017/12/8.
 */
class MusicRemoteData @Inject constructor(private val restService: RestService) {

    fun loadBanner(): LiveData<List<BannerResponse>> {
        val mutableLiveData = MutableLiveData<List<BannerResponse>>()
        val mCall: Call<BaseServerResponse>? = restService.loadData(UrlConfig.BANNER)
        mCall?.enqueue(object : RestBaseCallBack<List<BannerResponse>>() {
            override fun onResponse(data: List<BannerResponse>?) {
                mutableLiveData.value = data
            }

            override fun onFailure(error: Throwable?, code: Int, msg: String) {

            }
        })
        return mutableLiveData
    }

    fun loadMusicAlbum(): LiveData<List<MusicResponse>> {
        val mutableLiveData = MutableLiveData<List<MusicResponse>>()
        val mCall: Call<BaseServerResponse>? = restService.loadData(UrlConfig.MUSIC_ALBUM)
        mCall?.enqueue(object : RestBaseCallBack<List<MusicResponse>>() {
            override fun onResponse(data: List<MusicResponse>?) {
                mutableLiveData.value = data
            }

            override fun onFailure(error: Throwable?, code: Int, msg: String) {

            }
        })

        return mutableLiveData
    }

    fun loadMusicList(type: Int, offset: Int): LiveData<MusicListResponse> {
        val mutableLiveData = MutableLiveData<MusicListResponse>()
        val mCall: Call<MusicListResponse>? = restService.loadOffSetMusic(UrlConfig.MUSIC_BASE, type, 40, offset)
        mCall?.enqueue(object : Callback<MusicListResponse> {

            override fun onResponse(call: Call<MusicListResponse>?, response: Response<MusicListResponse>?) {
                mutableLiveData.value = response?.body()
            }

            override fun onFailure(call: Call<MusicListResponse>?, t: Throwable?) {
            }
        })

        return mutableLiveData
    }

    fun loadMusicDownLoadInfo(songId: String?): LiveData<MusicDownLoadInfo> {
        val mutableLiveData = MutableLiveData<MusicDownLoadInfo>()
        val mCall: Call<MusicDownLoadInfo>? = restService.loadMusicDownLoadInfo(UrlConfig.MUSIC_PLAY, songId)
        mCall?.enqueue(object : Callback<MusicDownLoadInfo> {

            override fun onResponse(call: Call<MusicDownLoadInfo>?, response: Response<MusicDownLoadInfo>?) {
                mutableLiveData.value = response?.body()
            }

            override fun onFailure(call: Call<MusicDownLoadInfo>?, t: Throwable?) {
            }
        })

        return mutableLiveData
    }

    fun loadMusicDownLoadInfo(songId: String?, callback: Callback<MusicDownLoadInfo>) {
        val mCall: Call<MusicDownLoadInfo>? = restService.loadMusicDownLoadInfo(UrlConfig.MUSIC_PLAY, songId)
        mCall?.enqueue(callback)
    }


    fun downLoadInfo(url: String?, callback: Callback<ResponseBody>) {
        val mCall: Call<ResponseBody>? = restService.downLoadData(url!!)
        mCall?.enqueue(callback)
    }
}