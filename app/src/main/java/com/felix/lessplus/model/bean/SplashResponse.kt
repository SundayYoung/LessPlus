package com.felix.lessplus.model.bean

/**
 * Created by liuhaiyang on 2018/1/15.
 */
class SplashResponse {

    var images: List<Image>? = null

    inner class Image{
        var url: String? = null
    }
}