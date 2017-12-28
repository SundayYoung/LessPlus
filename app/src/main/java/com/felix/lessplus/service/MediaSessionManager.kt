package com.felix.lessplus.service

import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

import com.felix.lessplus.model.bean.Music

/**
 * Created by felix on 2017/8/8.
 */
class MediaSessionManager(private val mPlayService: PlayService) {
    private var mMediaSession: MediaSessionCompat? = null

    private val callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            mPlayService.playPause()
        }

        override fun onPause() {
            mPlayService.playPause()
        }

        override fun onSkipToNext() {
            mPlayService.next()
        }

        override fun onSkipToPrevious() {
            mPlayService.prev()
        }

        override fun onStop() {
            mPlayService.stop()
        }

        override fun onSeekTo(pos: Long) {
            mPlayService.seekTo(pos.toInt())
        }
    }

    init {
        setupMediaSession()
    }

    private fun setupMediaSession() {
        mMediaSession = MediaSessionCompat(mPlayService, TAG)
        mMediaSession!!.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS)
        mMediaSession!!.setCallback(callback)
        mMediaSession!!.isActive = true
    }

    fun updatePlaybackState() {
        val state = if (mPlayService.isPlaying || mPlayService.isPreparing) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
        mMediaSession!!.setPlaybackState(
                PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, mPlayService.currentPosition, 1f)
                        .build())
    }

    fun updateMetaData(music: Music?) {
        if (music == null) {
            mMediaSession!!.setMetadata(null)
            return
        }

        val metaData = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.album)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, music.artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, music.duration)
        //                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, CoverLoader.getInstance().loadThumbnail(music));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, AppCache.getMusicList().size());
        }

        mMediaSession!!.setMetadata(metaData.build())
    }

    fun release() {
        mMediaSession!!.setCallback(null)
        mMediaSession!!.isActive = false
        mMediaSession!!.release()
    }

    companion object {
        private val TAG = "MediaSessionManager"
        private val MEDIA_SESSION_ACTIONS = (PlaybackStateCompat.ACTION_PLAY
                or PlaybackStateCompat.ACTION_PAUSE
                or PlaybackStateCompat.ACTION_PLAY_PAUSE
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                or PlaybackStateCompat.ACTION_STOP
                or PlaybackStateCompat.ACTION_SEEK_TO)
    }
}
