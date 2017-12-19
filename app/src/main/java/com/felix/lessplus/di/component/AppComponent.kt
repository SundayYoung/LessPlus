package com.weiyankeji.zhongmei.di.component

import com.felix.lessplus.viewmodel.MusicViewModel
import com.weiyankeji.zhongmei.di.module.AppModule
import com.weiyankeji.zhongmei.di.module.RemoteModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/2.
 */
@Component(modules = arrayOf(AppModule::class, RemoteModule::class))
@Singleton
interface AppComponent {
    fun inject(currencyViewModel: MusicViewModel)
}