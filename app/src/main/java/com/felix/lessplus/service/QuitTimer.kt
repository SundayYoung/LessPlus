package com.felix.lessplus.service

import android.os.Handler
import android.text.format.DateUtils


/**
 * Created by felix on 2017/12/8.
 */
class QuitTimer private constructor() {
    private var mPlayService: PlayService? = null
    private var mTimerCallback: EventCallback<Long>? = null
    private var mHandler: Handler? = null
    private var mTimerRemain: Long = 0

    private val mQuitRunnable = object : Runnable {
        override fun run() {
            mTimerRemain -= DateUtils.SECOND_IN_MILLIS
            if (mTimerRemain > 0) {
                mTimerCallback!!.onEvent(mTimerRemain)
                mHandler!!.postDelayed(this, DateUtils.SECOND_IN_MILLIS)
            } else {
                //AppCache.clearStack();
                mPlayService!!.quit()
            }
        }
    }

    private object SingletonHolder {
        val sInstance = QuitTimer()
    }

    fun init(playService: PlayService, handler: Handler, timerCallback: EventCallback<Long>) {
        mPlayService = playService
        mHandler = handler
        mTimerCallback = timerCallback
    }

    fun start(milli: Long) {
        stop()
        if (milli > 0) {
            mTimerRemain = milli + DateUtils.SECOND_IN_MILLIS
            mHandler!!.post(mQuitRunnable)
        } else {
            mTimerRemain = 0
            mTimerCallback!!.onEvent(mTimerRemain)
        }
    }

    fun stop() {
        mHandler!!.removeCallbacks(mQuitRunnable)
    }

    companion object {

        val instance: QuitTimer
            get() = SingletonHolder.sInstance
    }
}
