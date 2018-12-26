package com.felix.lessplus.http;


import com.felix.lessplus.model.bean.BannerResponse;
import com.felix.lessplus.model.bean.GankIoResponse;
import com.felix.lessplus.model.bean.HotMovieResponse;
import com.felix.lessplus.model.bean.MovieDetailResponse;
import com.felix.lessplus.model.bean.MusicDownLoadInfo;
import com.felix.lessplus.model.bean.MusicListResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by liuhaiyang on 2016/9/6.
 */
public interface RestService {

    @GET
    Call<BaseServerResponse> loadData(@Url String url, @QueryMap Map<String, Object> map);

    @GET
    Call<BaseServerResponse> loadData(@Url String url);

    @POST
    Call<BaseServerResponse> postData(@Url String url, @Body Object body);

    @POST
    @Multipart
    Call<BaseServerResponse> uploadFile(@Url String url, @PartMap Map<String, RequestBody> params);

    @POST
    @Multipart
    Call<BaseServerResponse> uploadFile(@Url String url, @PartMap Map<String, RequestBody> params, @Header("token") String token);

    @POST
    @Multipart
    Call<BaseServerResponse> uploadFile(@Url String url, @Part() List<MultipartBody.Part> parts);


    @GET
    Call<ResponseBody> downLoadData(@Url String url);

    @GET
    Call<BannerResponse> loadBanner(@Url String url);

    @GET
    Call<MusicListResponse> loadOffSetMusic(@Url String url, @Query("type")int type, @Query("size")int size, @Query("offset")int offset);

    @GET
    Call<MusicDownLoadInfo> loadMusicDownLoadInfo(@Url String url, @Query("songid")String songId);


    @GET("http://gank.io/api/data/{type}/{pre_page}/{page}")
    Call<GankIoResponse> loadGankIoData(@Path("type")String type, @Path("page")int page, @Path("pre_page")int pre_page);

    @GET
    Call<HotMovieResponse> loadHotMovie(@Url String url);

    @GET("http://api.douban.com/v2/movie/subject/{id}")
    Call<MovieDetailResponse> loadMovieDetail(@Path("id")String id);
}
