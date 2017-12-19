package com.felix.lessplus.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.felix.lessplus.http.BaseServerResponse
import com.felix.lessplus.http.RestBaseCallBack
import com.felix.lessplus.http.RestService
import com.felix.lessplus.http.UrlConfig
import com.felix.lessplus.model.bean.BannerResponse
import com.felix.lessplus.model.bean.MusicResponse
import retrofit2.Call
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

//        mCall?.enqueue(object : retrofit2.Callback<BannerResponse> {
//
//            override fun onResponse(call: Call<BannerResponse>?, response: Response<BannerResponse>?) {
//                mutableLiveData.value = response?.body()
//            }
//
//            override fun onFailure(call: Call<BannerResponse>?, t: Throwable?) {
//            }
//        })

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
}