package com.felix.lessplus.model.bean

import com.felix.lessplus.http.BaseServerResponse

/**
 * Created by liuhaiyang on 2017/12/8.
 * banner bean
 */
class BannerResponse : BaseServerResponse() {

    var result: ResultBean? = null

    inner class ResultBean {

        var focus: FocusBean? = null

        inner class FocusBean {
            var error_code: Int = 0
            var result: List<ResultBeanX>? = null

            inner class ResultBeanX {
                var randpic: String? = null
                var code: String? = null
                var type: Int = 0
            }
        }
    }

}