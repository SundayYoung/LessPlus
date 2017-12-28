package com.felix.lessplus.model.bean

/**
 * Created by liuhaiyang on 2017/12/20.
 */
class MusicListResponse {

    var billboard: BillBoardData? = null
    var song_list: List<OnLineMusic>? = null

    inner class BillBoardData {
        var billboard_type: String? = null
        var update_date: String? = null
        var name: String? = null
        var comment: String? = null
        var pic_s192: String? = null
        var pic_s260: String? = null
        var pic_s640: String? = null
    }
}