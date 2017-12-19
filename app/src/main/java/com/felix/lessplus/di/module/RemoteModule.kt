package com.weiyankeji.zhongmei.di.module

import com.felix.lessplus.http.RestService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/4.
 */
@Module
class RemoteModule {
    @Provides @Singleton fun provideGson(): Gson =
            GsonBuilder()
                    .setLenient()
                    .create()

    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()


    @Provides @Singleton fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl("https://www.gethub.com")
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()


    @Provides @Singleton fun provideRestService(retrofit: Retrofit): RestService =
            retrofit.create(RestService::class.java)
}