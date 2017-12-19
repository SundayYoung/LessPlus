package com.felix.lessplus.http

import java.io.Serializable

/**
 * Created by liuhaiyang on 2016/9/6.
 */
open class BaseServerResponse : Serializable {
    var msg: String? = null
    var data: Any? = null
}
