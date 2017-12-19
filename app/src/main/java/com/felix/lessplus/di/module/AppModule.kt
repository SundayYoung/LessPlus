package com.weiyankeji.zhongmei.di.module

import android.content.Context
import com.felix.lessplus.ui.LessPlusApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/2.
 */
@Module
class AppModule(private val mApplication: LessPlusApplication) {
    @Provides
    @Singleton
    fun provideContext(): Context = mApplication
}