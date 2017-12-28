package com.felix.lessplus.service

/**
 * Created by liuhaiyang on 2017/12/27.
 */
class PlayCache private constructor() {

    var mPlayService: PlayService? = null

    companion object {
        val instance = PlayCache()
    }
}