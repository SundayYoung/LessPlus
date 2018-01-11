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
class GankRemoteData @Inject constructor(private val restService: RestService) {

    fun loadGankAndroid(page: Int): LiveData<GankIoResponse> {
        val mutableLiveData = MutableLiveData<GankIoResponse>()
        val mCall: Call<GankIoResponse>? = restService.loadGankIoData("Android", page, 20)
        mCall?.enqueue(object : Callback<GankIoResponse> {

            override fun onResponse(call: Call<GankIoResponse>?, response: Response<GankIoResponse>?) {
                mutableLiveData.value = response?.body()
            }

            override fun onFailure(call: Call<GankIoResponse>?, t: Throwable?) {
            }
        })
        return mutableLiveData
    }
}