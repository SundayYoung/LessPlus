package com.felix.lessplus.http


import com.felix.lessplus.model.bean.BannerResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Created by liuhaiyang on 2017/12/6.
 */
interface RestService {

    @GET
    fun loadData(@Url url: String): Call<BaseServerResponse>

    @GET
    fun loadData(@Url url: String, @QueryMap map: Map<String, Any>): Call<BaseServerResponse>

    @POST
    fun postData(@Url url: String, @Body body: Any): Call<BaseServerResponse>

    /**
     * Get Banner Data
     */
    @GET
    fun getBannerData(@Url url: String): Call<BannerResponse>


}
