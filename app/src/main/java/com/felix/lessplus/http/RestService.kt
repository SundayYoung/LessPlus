package com.felix.lessplus.http


import com.felix.lessplus.model.bean.MusicDownLoadInfo
import com.felix.lessplus.model.bean.MusicListResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by liuhaiyang on 2017/12/6.
 */
interface RestService {

    @GET
    fun loadData(@Url url: String): Call<BaseServerResponse>

    @GET
    fun downLoadData(@Url url: String): Call<ResponseBody>

    @GET
    fun loadOffSetMusic(@Url url: String, @Query("type") type: Int, @Query("size") size: Int, @Query("offset") offset: Int): Call<MusicListResponse>

    @GET
    fun loadMusicDownLoadInfo(@Url url: String, @Query("songid") songId: String?): Call<MusicDownLoadInfo>

    @GET
    fun loadData(@Url url: String, @QueryMap map: Map<String, Any>): Call<BaseServerResponse>

    @POST
    fun postData(@Url url: String, @Body body: Any): Call<BaseServerResponse>

}
