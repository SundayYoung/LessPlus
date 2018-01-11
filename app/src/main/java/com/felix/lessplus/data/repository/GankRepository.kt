package com.felix.lessplus.data.repository

import android.arch.lifecycle.LiveData
import com.felix.lessplus.data.remote.GankRemoteData
import com.felix.lessplus.model.bean.GankIoResponse
import javax.inject.Inject


/**
 * Created by liuhaiyang on 2017/12/8.
 */
class GankRepository @Inject constructor(private val remoteData: GankRemoteData) {

    fun getGankAndroid(page: Int): LiveData<GankIoResponse> {
        return remoteData.loadGankAndroid(page)
    }

}