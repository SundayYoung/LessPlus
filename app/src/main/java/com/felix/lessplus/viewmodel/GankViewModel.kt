package com.felix.lessplus.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.felix.lessplus.data.repository.GankRepository
import com.felix.lessplus.data.repository.MusicRepository
import com.felix.lessplus.model.bean.*
import com.felix.lessplus.ui.LessPlusApplication
import javax.inject.Inject

/**
 * Created by liuhaiyang on 2018/1/10.
 */
class GankViewModel : ViewModel() {

    @Inject lateinit var mRepository: GankRepository

    private var mGankAndroidData: LiveData<GankIoResponse>? = null

    init {
        LessPlusApplication.mAppComponent.inject(this)
    }


    fun loadGankAndoid(page: Int): LiveData<GankIoResponse>? {
        mGankAndroidData = null
        mGankAndroidData = MutableLiveData<GankIoResponse>()
        mGankAndroidData = mRepository.getGankAndroid(page)
        return mGankAndroidData
    }

}