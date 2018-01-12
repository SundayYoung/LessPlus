package com.felix.lessplus.data.repository

import android.arch.lifecycle.LiveData
import com.felix.lessplus.data.remote.MovieRemoteData
import com.felix.lessplus.model.bean.HotMovieResponse
import com.felix.lessplus.model.bean.MovieDetailResponse
import javax.inject.Inject


/**
 * Created by liuhaiyang on 2017/12/8.
 */
class MovieRepository @Inject constructor(private val remoteData: MovieRemoteData) {

    fun getHotMovie(): LiveData<HotMovieResponse> {
        return remoteData.loadHotMovie()
    }

    fun getMovieDetail(id: String): LiveData<MovieDetailResponse> {
        return remoteData.loadMovieDetail(id)
    }

}