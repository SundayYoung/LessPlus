package com.felix.lessplus.utils

import android.content.Context
import com.felix.lessplus.http.BaseServerResponse
import com.felix.lessplus.http.RestBaseCallBack
import com.felix.lessplus.http.RestService
import com.felix.lessplus.http.UrlConfig
import com.felix.lessplus.model.bean.BannerResponse
import com.felix.lessplus.model.bean.MusicDownLoadInfo
import com.felix.lessplus.model.bean.SplashResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import java.net.URL
import javax.inject.Inject

/**
 * Created by liuhaiyang on 2017/12/13.
 */
class HttpUtil @Inject constructor(private val mService: RestService) {

    fun getData(url: String, request: Any, callBack: RestBaseCallBack<*>) {
        val queryMap = JsonUtil.javaBeanToMap(request)
        val mCall: Call<BaseServerResponse> = mService.loadData(url, queryMap)
        mCall.enqueue(callBack)
    }

    fun getData(url: String, callBack: RestBaseCallBack<*>) {
        val mCall: Call<BaseServerResponse> = mService.loadData(url)
        mCall.enqueue(callBack)
    }

    companion object {

//        fun buildBaseUrl(songId: String): String {
//            return buildUrl("${UrlConfig.MUSIC_PLAY}&songid=$songId")
//        }
        fun loadMusicDownLoadInfo(songId: String?): MusicDownLoadInfo? {
            var forecastJsonStr: String? = null
            try {
                forecastJsonStr = URL(UrlConfig.MUSIC_PLAY + songId).readText()
            } catch (e: Exception) {
                return null
            }
            val data = JsonUtil.decode(forecastJsonStr, MusicDownLoadInfo::class.java)
            return if (data.bitrate != null) data else null
        }

        fun loadSplash(): SplashResponse? {
            var forecastJsonStr: String? = null
            try {
                forecastJsonStr = URL(UrlConfig.SPLASH_URL).readText()
            } catch (e: Exception) {
                return null
            }
            val data = JsonUtil.decode(forecastJsonStr, SplashResponse::class.java)
            return if (data.images != null) data else null
        }
    }
}