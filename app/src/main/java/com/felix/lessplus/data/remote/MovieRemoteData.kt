package com.felix.lessplus.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.felix.lessplus.http.BaseServerResponse
import com.felix.lessplus.http.RestBaseCallBack
import com.felix.lessplus.http.RestService
import com.felix.lessplus.http.UrlConfig
import com.felix.lessplus.model.bean.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by liuhaiyang on 2017/12/8.
 */
class MovieRemoteData @Inject constructor(private val restService: RestService) {

    fun loadHotMovie(): LiveData<HotMovieResponse> {
        val mutableLiveData = MutableLiveData<HotMovieResponse>()
        val mCall: Call<HotMovieResponse>? = restService.loadHotMovie(UrlConfig.HOT_MOVIE)
        mCall?.enqueue(object : Callback<HotMovieResponse> {

            override fun onResponse(call: Call<HotMovieResponse>?, response: Response<HotMovieResponse>?) {
                mutableLiveData.value = response?.body()
            }

            override fun onFailure(call: Call<HotMovieResponse>?, t: Throwable?) {
            }
        })
        return mutableLiveData
    }

    fun loadMovieDetail(id: String): LiveData<MovieDetailResponse> {
        val mutableLiveData = MutableLiveData<MovieDetailResponse>()
        val mCall: Call<MovieDetailResponse>? = restService.loadMovieDetail(id)
        mCall?.enqueue(object : Callback<MovieDetailResponse> {

            override fun onResponse(call: Call<MovieDetailResponse>?, response: Response<MovieDetailResponse>?) {
                mutableLiveData.value = response?.body()
            }

            override fun onFailure(call: Call<MovieDetailResponse>?, t: Throwable?) {
            }
        })
        return mutableLiveData
    }
}