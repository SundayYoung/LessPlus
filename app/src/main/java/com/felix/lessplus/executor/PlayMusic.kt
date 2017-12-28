package com.felix.lessplus.executor

import android.app.Activity

import com.felix.lessplus.model.bean.Music

/**
 * Created by Felix on 2017/1/20.
 */
abstract class PlayMusic(private val mActivity: Activity, private val mTotalStep: Int) : IExecutor<Music> {
    protected var mMusic: Music? = null
    protected var mCounter = 0

    override fun execute() {
        getPlayInfoWrapper()
    }

    private fun getPlayInfoWrapper() {
        onPrepare()
        getPlayInfo()
    }

    protected abstract fun getPlayInfo()

    protected fun checkCounter() {
        mCounter++
        if (mCounter == mTotalStep) {
            onExecuteSuccess(mMusic)
        }
    }
}
