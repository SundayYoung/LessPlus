package com.felix.lessplus.model.bean

/**
 * Created by liuhaiyang on 2017/12/13.
 */
class MusicResponse {

    var title: String? = null
    var albumList: List<MusicAlbum>? = null

    class MusicAlbum {
        var url: String? = null
        var content: String? = null
        var type: Int = 0
    }
}