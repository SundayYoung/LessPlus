package com.felix.lessplus.http

/**
 * Created by liuhaiyang on 2017/12/8.
 */
class UrlConfig {

    companion object {
        var MUSIC_BASE: String = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList"
        var MUSIC_PLAY: String = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.play&songid="
        var BANNER: String = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14"
        var MUSIC_ALBUM: String = "http://5a2a624532152c0012fb9328.mockapi.io/api/homeMusic"
    }
}