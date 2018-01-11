package com.felix.lessplus.model.bean

import com.felix.lessplus.http.BaseServerResponse

/**
 * Created by liuhaiyang on 2017/12/8.
 * banner bean
 */
class GankIoResponse : BaseServerResponse() {

    var error: Boolean? = false
    var results: List<ResultBean>? = null

    inner class ResultBean {

        var _id: String? = null
        var createdAt: String? = null
        var desc: String? = null
        var publishedAt: String? = null
        var url: String? = null
        var who: String? = null
        var images: List<String>? = null
    }

}