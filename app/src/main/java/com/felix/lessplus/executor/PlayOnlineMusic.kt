package com.felix.lessplus.executor

import android.app.Activity
import android.text.TextUtils
import com.felix.lessplus.data.remote.MusicRemoteData
import com.felix.lessplus.di.component.DaggerPlayComponent

import com.felix.lessplus.model.bean.Music
import com.felix.lessplus.model.bean.MusicDownLoadInfo
import com.felix.lessplus.model.bean.OnLineMusic
import com.felix.lessplus.utils.FileUtils
import com.felix.lessplus.di.module.RemoteModule
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.File
import javax.inject.Inject

/**
 * 播放在线音乐
 * Created by liuhaiyang on 2017/12/26.
 */
abstract class PlayOnlineMusic(mActivity: Activity, private val mOnlineMusic: OnLineMusic) : PlayMusic(mActivity, 3) {

    @Inject lateinit var mRemoteData: MusicRemoteData

    //TODO this leak 内存泄漏
    init {
        DaggerPlayComponent.builder()
                .remoteModule(RemoteModule()).build()
                .inject(this)
    }

    override fun getPlayInfo() {
        val artist = mOnlineMusic.artist_name
        val title = mOnlineMusic.title

        mMusic = Music()
        mMusic?.type = Music.Type.ONLINE
        mMusic?.title = title
        mMusic?.artist = artist
        mMusic?.album = mOnlineMusic.album_title

        // 下载歌词
        val lrcFileName = FileUtils.getLrcFileName(artist, title)
        val lrcFile = File(FileUtils.lrcDir + lrcFileName)
        if (!lrcFile.exists() && !TextUtils.isEmpty(mOnlineMusic.lrclink)) {
            downloadLrc(mOnlineMusic.lrclink, lrcFileName)
        } else {
            mCounter++
        }

        // 下载封面
        val albumFileName = FileUtils.getAlbumFileName(artist, title)
        val albumFile = File(FileUtils.albumDir, albumFileName)
        var picUrl = mOnlineMusic.album_500_500
        if (TextUtils.isEmpty(picUrl)) {
            picUrl = mOnlineMusic.pic_big
        }
        if (!albumFile.exists() && !TextUtils.isEmpty(picUrl)) {
            downloadAlbum(picUrl, albumFileName)
        } else {
            mCounter++
        }
        mMusic?.coverPath = albumFile.path

        // 获取歌曲播放链接
//        doAsync {
//            val data = HttpUtil.loadMusicDownLoadInfo(mOnlineMusic.song_id)
//            uiThread {
//                if (data == null) {
//                    return@uiThread
//                }
//
//                mMusic?.path = data.bitrate?.file_link
//                mMusic?.duration = (data.bitrate?.file_duration!! * 1000).toLong()
//                checkCounter()
//            }
//        }

        mRemoteData.loadMusicDownLoadInfo(mOnlineMusic.song_id, object : Callback<MusicDownLoadInfo> {

            override fun onResponse(call: Call<MusicDownLoadInfo>?, response: Response<MusicDownLoadInfo>?) {
                val data = response?.body()
                if (data?.bitrate == null) {
                    return
                }
                mMusic?.path = data.bitrate?.file_link
                mMusic?.duration = (data.bitrate?.file_duration!! * 1000).toLong()
                checkCounter()
            }

            override fun onFailure(call: Call<MusicDownLoadInfo>?, t: Throwable?) {
            }
        })

    }

    private fun downloadLrc(url: String?, fileName: String) {
        mRemoteData.downLoadInfo(url, object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response != null) {
                    FileUtils.saveFile(response.body()!!,FileUtils.lrcDir, fileName)
                }
                checkCounter()
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            }
        })
    }

    private fun downloadAlbum(picUrl: String?, fileName: String) {
        mRemoteData.downLoadInfo(picUrl, object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response != null) {
                    FileUtils.saveFile(response.body()!!,FileUtils.albumDir, fileName)
                }
                checkCounter()
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            }
        })
    }
}
