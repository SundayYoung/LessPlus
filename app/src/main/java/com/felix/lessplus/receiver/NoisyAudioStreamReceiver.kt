package com.felix.lessplus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.felix.lessplus.service.PlayService
import com.felix.lessplus.extendmusic.Actions

/**
 * 来电/耳机拔出时暂停播放
 * Created by liuhaiyang on 2017/12/27.
 */
class NoisyAudioStreamReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        PlayService.startCommand(context, Actions.ACTION_MEDIA_PLAY_PAUSE)
    }
}
