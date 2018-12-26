package com.felix.lessplus.di.component

import com.felix.lessplus.viewmodel.GankViewModel
import com.felix.lessplus.viewmodel.MovieViewModel
import com.felix.lessplus.viewmodel.MusicViewModel
import com.felix.lessplus.di.module.AppModule
import com.felix.lessplus.di.module.RemoteModule
import com.felix.lessplus.ui.activity.HttpTestActivity
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
    fun inject(httpTestActivity: HttpTestActivity)
}