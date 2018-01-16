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

        var GANK_BASE: String = "http://gank.io/api/"
        var HOT_MOVIE: String = "http://api.douban.com/v2/movie/in_theaters"

        var SPLASH_BASE: String = "http://cn.bing.com"
        var SPLASH_URL: String = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1"
    }
}