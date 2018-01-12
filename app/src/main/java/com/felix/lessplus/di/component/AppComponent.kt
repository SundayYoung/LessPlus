package com.weiyankeji.zhongmei.di.component

import com.felix.lessplus.viewmodel.GankViewModel
import com.felix.lessplus.viewmodel.MovieViewModel
import com.felix.lessplus.viewmodel.MusicViewModel
import com.weiyankeji.zhongmei.di.module.AppModule
import com.weiyankeji.zhongmei.di.module.RemoteModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/2.
 */
@Component(modules = [(AppModule::class), (RemoteModule::class)])
@Singleton
interface AppComponent {
    fun inject(currencyViewModel: MusicViewModel)
    fun inject(currencyViewModel: GankViewModel)
    fun inject(currencyViewModel: MovieViewModel)
}