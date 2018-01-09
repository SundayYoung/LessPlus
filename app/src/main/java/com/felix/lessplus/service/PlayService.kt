package com.felix.lessplus.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.felix.lessplus.extendmusic.Actions

import com.felix.lessplus.model.bean.Music
import com.felix.lessplus.receiver.NoisyAudioStreamReceiver

import java.io.IOException

/**
 * 音乐播放后台服务
 * Created by liuhaiyang on 2017/12/27.
 */
class PlayService : Service(), MediaPlayer.OnCompletionListener {

    private val mNoisyReceiver = NoisyAudioStreamReceiver()
    private val mNoisyFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private val mHandler = Handler()
    private var mPlayer: MediaPlayer? = MediaPlayer()
    private var mAudioFocusManager: AudioFocusManager? = null
    private var mMediaSessionManager: MediaSessionManager? = null
    var onPlayEventListener: OnPlayerEventListener? = null
    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    var playingMusic: Music? = null
        private set
    /**
     * 获取正在播放的本地歌曲的序号
     */
    var playingPosition = -1
        private set
    private var mPlayState = STATE_IDLE

    private val mPreparedListener = MediaPlayer.OnPreparedListener {
        if (isPreparing) {
            start()
        }
    }

    private val mBufferingUpdateListener = MediaPlayer.OnBufferingUpdateListener { mp, percent ->
        if (onPlayEventListener != null) {
            onPlayEventListener!!.onBufferingUpdate(percent)
        }
    }

    val isPlaying: Boolean
        get() = mPlayState == STATE_PLAYING

    val isPausing: Boolean
        get() = mPlayState == STATE_PAUSE

    val isPreparing: Boolean
        get() = mPlayState == STATE_PREPARING

    val isIdle: Boolean
        get() = mPlayState == STATE_IDLE

    val audioSessionId: Int
        get() = mPlayer!!.audioSessionId

    val currentPosition: Long
        get() = if (isPlaying || isPausing) {
            mPlayer!!.currentPosition.toLong()
        } else {
            0
        }

    private val mPublishRunnable = object : Runnable {
        override fun run() {
            if (isPlaying && onPlayEventListener != null) {
                onPlayEventListener!!.onPublish(mPlayer!!.currentPosition)
            }
            mHandler.postDelayed(this, TIME_UPDATE)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate: " + javaClass.simpleName)
        mAudioFocusManager = AudioFocusManager(this)
        mMediaSessionManager = MediaSessionManager(this)
        mPlayer!!.setOnCompletionListener(this)
//        Notifier.init(this)
//        QuitTimer.instance.init(this, mHandler, EventCallback<Long> { aLong ->
//            if (onPlayEventListener != null) {
//                onPlayEventListener!!.onTimer(aLong!!)
//            }
//        })
    }

    override fun onBind(intent: Intent): IBinder? {
        return PlayBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.action != null) {
            when (intent.action) {
                Actions.ACTION_MEDIA_PLAY_PAUSE -> playPause()
                Actions.ACTION_MEDIA_NEXT -> next()
                Actions.ACTION_MEDIA_PREVIOUS -> prev()
            }
        }
        return Service.START_NOT_STICKY
    }

    /**
     * 扫描音乐
     */
//    fun updateMusicList(callback: EventCallback<Void>?) {
//        object : AsyncTask<Void, Void, List<Music>>() {
//            override fun doInBackground(vararg params: Void): List<Music> {
//                return MusicUtils.scanMusic(this@PlayService)
//            }
//
//            override fun onPostExecute(musicList: List<Music>) {
//                AppCache.getMusicList().clear()
//                AppCache.getMusicList().addAll(musicList)
//
//                if (!AppCache.getMusicList().isEmpty()) {
//                    updatePlayingPosition()
//                    playingMusic = AppCache.getMusicList().get(playingPosition)
//                }
//
//                if (onPlayEventListener != null) {
//                    onPlayEventListener!!.onMusicListUpdate()
//                }
//
//                callback?.onEvent(null)
//            }
//        }.execute()
//    }

    override fun onCompletion(mp: MediaPlayer) {
        next()
    }

    fun play(position: Int) {
//        var position = position
//        if (AppCache.getMusicList().isEmpty()) {
//            return
//        }
//
//        if (position < 0) {
//            position = AppCache.getMusicList().size() - 1
//        } else if (position >= AppCache.getMusicList().size()) {
//            position = 0
//        }
//
//        playingPosition = position
//        val music = AppCache.getMusicList().get(playingPosition)
//        PreferencesUtil.saveCurrentSongId(music.id)
//        play(music)
    }

    fun play(music: Music) {
        playingMusic = music
        try {
            mPlayer!!.reset()
            mPlayer!!.setDataSource(music.path)
            mPlayer!!.prepareAsync()
            mPlayState = STATE_PREPARING
            mPlayer!!.setOnPreparedListener(mPreparedListener)
            mPlayer!!.setOnBufferingUpdateListener(mBufferingUpdateListener)
            if (onPlayEventListener != null) {
                onPlayEventListener!!.onChange(music)
            }
            mMediaSessionManager!!.updateMetaData(playingMusic)
            mMediaSessionManager!!.updatePlaybackState()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun playPause() {
        if (isPreparing) {
            stop()
        } else if (isPlaying) {
            pause()
        } else if (isPausing) {
            start()
        } else {
            play(playingPosition)
        }
    }

    internal fun start() {
        if (!isPreparing && !isPausing) {
            return
        }

        if (mAudioFocusManager!!.requestAudioFocus()) {
            mPlayer!!.start()
            mPlayState = STATE_PLAYING
            mHandler.post(mPublishRunnable)
            mMediaSessionManager!!.updatePlaybackState()
            registerReceiver(mNoisyReceiver, mNoisyFilter)

            if (onPlayEventListener != null) {
                onPlayEventListener!!.onPlayerStart()
            }
        }
    }

    internal fun pause() {
        if (!isPlaying) {
            return
        }

        mPlayer!!.pause()
        mPlayState = STATE_PAUSE
        mHandler.removeCallbacks(mPublishRunnable)
        mMediaSessionManager!!.updatePlaybackState()
        unregisterReceiver(mNoisyReceiver)

        if (onPlayEventListener != null) {
            onPlayEventListener!!.onPlayerPause()
        }
    }

    fun stop() {
        if (isIdle) {
            return
        }

        pause()
        mPlayer!!.reset()
        mPlayState = STATE_IDLE
    }

    operator fun next() {
//        if (AppCache.getMusicList().isEmpty()) {
//            return
//        }
//
//        val mode = PlayModeEnum.valueOf(PreferencesUtil.playMode)
//        when (mode) {
//            PlayModeEnum.SHUFFLE -> {
//                playingPosition = Random().nextInt(AppCache.getMusicList().size())
//                play(playingPosition)
//            }
//            PlayModeEnum.SINGLE -> play(playingPosition)
//            PlayModeEnum.LOOP -> play(playingPosition + 1)
//            else -> play(playingPosition + 1)
//        }
    }

    fun prev() {
//        if (AppCache.getMusicList().isEmpty()) {
//            return
//        }
//
//        val mode = PlayModeEnum.valueOf(PreferencesUtil.playMode)
//        when (mode) {
//            PlayModeEnum.SHUFFLE -> {
//                playingPosition = Random().nextInt(AppCache.getMusicList().size())
//                play(playingPosition)
//            }
//            PlayModeEnum.SINGLE -> play(playingPosition)
//            PlayModeEnum.LOOP -> play(playingPosition - 1)
//            else -> play(playingPosition - 1)
//        }
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    fun seekTo(msec: Int) {
        if (isPlaying || isPausing) {
            mPlayer!!.seekTo(msec)
            mMediaSessionManager!!.updatePlaybackState()
            if (onPlayEventListener != null) {
                onPlayEventListener!!.onPublish(msec)
            }
        }
    }

    /**
     * 删除或下载歌曲后刷新正在播放的本地歌曲的序号
     */
//    fun updatePlayingPosition() {
//        var position = 0
//        val id = PreferencesUtil.currentSongId
//        for (i in 0 until AppCache.getMusicList().size()) {
//            if (AppCache.getMusicList().get(i).getId() === id) {
//                position = i
//                break
//            }
//        }
//        playingPosition = position
//        PreferencesUtil.saveCurrentSongId(AppCache.getMusicList().get(playingPosition).getId())
//    }

    override fun onDestroy() {
        mPlayer!!.reset()
        mPlayer!!.release()
        mPlayer = null
        mAudioFocusManager!!.abandonAudioFocus()
        mMediaSessionManager!!.release()
        PlayCache.instance.mPlayService = null
        super.onDestroy()
        Log.i(TAG, "llllonDestroy: " + javaClass.simpleName)
    }

    fun quit() {
        stop()
        QuitTimer.instance.stop()
        stopSelf()
    }

    inner class PlayBinder : Binder() {
        val service: PlayService
            get() = this@PlayService
    }

    companion object {
        private val TAG = "Service"
        private val TIME_UPDATE = 300L

        private val STATE_IDLE = 0
        private val STATE_PREPARING = 1
        private val STATE_PLAYING = 2
        private val STATE_PAUSE = 3

        fun startCommand(context: Context, action: String) {
            val intent = Intent(context, PlayService::class.java)
            intent.action = action
            context.startService(intent)
        }
    }
}
