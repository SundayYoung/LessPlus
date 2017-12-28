package com.felix.lessplus.model.bean

import java.io.Serializable

/**
 * Created by liuhaiyang on 2017/12/26.
 */
class MusicDownLoadInfo : Serializable {

    var bitrate: Bitrate? = null

    inner class Bitrate {
        var file_duration: Int = 0
        var file_link: String? = null
    }
}