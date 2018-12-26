package com.felix.lessplus.ui.activity

import android.os.Bundle
import com.felix.lessplus.R
import com.felix.lessplus.http.BaseServerResponse
import com.felix.lessplus.http.RestBaseCallBack
import com.felix.lessplus.http.UrlConfig
import com.felix.lessplus.model.bean.MusicListResponse
import com.felix.lessplus.model.bean.MusicResponse
import com.felix.lessplus.model.bean.TestRequest
import com.felix.lessplus.ui.LessPlusApplication
import com.felix.lessplus.utils.HttpUtil
import javax.inject.Inject

class HttpTestActivity : BaseActivity() {

    @Inject
    lateinit var mHttpUtil: HttpUtil

    init {
        LessPlusApplication.mAppComponent.inject(this)
    }

    override fun setContentLayout(): Int = R.layout.activity_splash

    override fun initData(bundle: Bundle?) {

        val request = TestRequest()
        val map = HashMap<String, Any>()
        map["type"] = 2
        map["size"] = 40
        map["offset"] = 0
        mHttpUtil.getData(UrlConfig.MUSIC_ALBUM, request, object : RestBaseCallBack<List<MusicResponse>>(){
            override fun onResponse(data: List<MusicResponse>?) {

            }

            override fun onFailure(error: Throwable?, code: Int, msg: String) {
            }

        })

    }
}