package com.felix.lessplus.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.felix.lessplus.http.UrlConfig
import com.felix.lessplus.model.bean.MusicDownLoadInfo
import com.google.gson.Gson
import java.net.URL

/**
 * Created by liuhaiyang on 2017/12/13.
 */
class HttpUtil {

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
            val data = Gson().fromJson(forecastJsonStr, MusicDownLoadInfo::class.java)
            return if (data.bitrate != null) data else null
        }

    }
}