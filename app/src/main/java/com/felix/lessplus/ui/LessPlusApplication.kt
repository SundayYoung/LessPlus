package com.felix.lessplus.ui

import android.app.Application
import com.weiyankeji.zhongmei.di.component.AppComponent
import com.weiyankeji.zhongmei.di.component.DaggerAppComponent
import com.weiyankeji.zhongmei.di.module.AppModule
import com.weiyankeji.zhongmei.di.module.RemoteModule

/**
 * Created by liuhaiyang on 2017/12/8.
 */
class LessPlusApplication : Application() {

    companion object {
        lateinit var mAppComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        mAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
//                .roomModule(RoomModule())
                .remoteModule(RemoteModule()).build()
    }
}