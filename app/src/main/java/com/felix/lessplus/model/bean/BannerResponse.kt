package com.felix.lessplus.model.bean

import com.felix.lessplus.http.BaseServerResponse

/**
 * Created by liuhaiyang on 2017/12/8.
 * banner bean
 */
class BannerResponse : BaseServerResponse() {
    var randpic: String? = null
    var mo_type: Int = 0
    var type: Int = 0
    var is_publish: String? = null
    var randpic_iphone6: String? = null
    var randpic_desc: String? = null
}