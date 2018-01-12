package com.felix.lessplus.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.felix.lessplus.data.repository.MovieRepository
import com.felix.lessplus.model.bean.*
import com.felix.lessplus.ui.LessPlusApplication
import javax.inject.Inject

/**
 * Created by liuhaiyang on 2018/1/10.
 */
class MovieViewModel : ViewModel() {

    @Inject lateinit var mRepository: MovieRepository

    private var mMovieList: LiveData<HotMovieResponse>? = null
    private var mMovieDetail: LiveData<MovieDetailResponse>? = null

    init {
        LessPlusApplication.mAppComponent.inject(this)
    }


    fun loadHotMovie(): LiveData<HotMovieResponse>? {
        mMovieList = null
        mMovieList = MutableLiveData<HotMovieResponse>()
        mMovieList = mRepository.getHotMovie()
        return mMovieList
    }

    fun loadMovieDetail(id : String): LiveData<MovieDetailResponse>? {
        mMovieDetail = null
        mMovieDetail = MutableLiveData<MovieDetailResponse>()
        mMovieDetail = mRepository.getMovieDetail(id)
        return mMovieDetail
    }

}