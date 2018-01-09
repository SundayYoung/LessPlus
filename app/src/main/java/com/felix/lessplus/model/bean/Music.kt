package com.felix.lessplus.model.bean

import java.io.Serializable

/**
 * 单曲信息
 * Created by felix on 2017/12/27.
 */
class Music : Serializable {
    // 歌曲类型:本地/网络
    var type: Type? = null
    // [本地歌曲]歌曲id
    var id: Long = 0
    // 音乐标题
    var title: String? = null
    // 艺术家
    var artist: String? = null
    // 专辑
    var album: String? = null
    // [本地歌曲]专辑ID
    var albumId: Long = 0
    // [在线歌曲]专辑封面路径
    var coverPath: String? = null
    // 持续时间
    var duration: Long = 0
    // 音乐路径
    var path: String? = null
    // 文件名
    var fileName: String? = null
    // 文件大小
    var fileSize: Long = 0
    var albumImg_500_500: String? = null

    enum class Type {
        LOCAL,
        ONLINE
    }

    /**
     * 对比本地歌曲是否相同
     */
    override fun equals(o: Any?): Boolean {
        return o is Music && this.id == o.id
    }
}
