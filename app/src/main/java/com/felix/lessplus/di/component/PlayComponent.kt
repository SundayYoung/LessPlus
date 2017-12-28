package com.weiyankeji.zhongmei.di.component

import com.felix.lessplus.executor.PlayOnlineMusic
import com.weiyankeji.zhongmei.di.module.RemoteModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/2.
 */
@Component(modules = arrayOf(RemoteModule::class))
@Singleton
interface PlayComponent {
    fun inject(playOnlineMusic: PlayOnlineMusic)
}