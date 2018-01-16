package com.felix.lessplus.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import com.felix.lessplus.R
import com.felix.lessplus.http.UrlConfig
import com.felix.lessplus.service.PlayCache
import com.felix.lessplus.service.PlayService
import com.felix.lessplus.utils.GlideImageLoader
import com.felix.lessplus.utils.HttpUtil
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SplashActivity : AppCompatActivity() {

    private var mPlayServiceConnection: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemClock.sleep(300)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setTheme(R.style.AppTheme)
        loadSplash()
        checkService()
    }

    override fun onDestroy() {
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection)
        }
        super.onDestroy()
    }

    private fun jumpToMainActivity() {
        SystemClock.sleep(400)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out)
        finish()
    }

    private fun checkService() {
        if (PlayCache.instance.mPlayService == null) {
            val intent = Intent(this, PlayService::class.java)
            startService(intent)

            val handler = Handler()
            handler.postDelayed({
                val intent = Intent()
                intent.setClass(this, PlayService::class.java)
                mPlayServiceConnection = PlayServiceConnection()
                bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE)
            }, 1000)
        } else {
            jumpToMainActivity()
            finish()
        }
    }

    private fun loadSplash() {
        // 获取歌曲播放链接
        doAsync {
            val data = HttpUtil.loadSplash()
            uiThread {
                if (data == null) {
                    return@uiThread
                }
                val url: String = UrlConfig.SPLASH_BASE + data.images?.get(0)?.url!!
                GlideImageLoader.displayImage(this@SplashActivity, url, vIvBg)
            }
        }
    }

    /**
     * service connection
     */
    private inner class PlayServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val playService = (service as PlayService.PlayBinder).service
            PlayCache.instance.mPlayService = playService

            jumpToMainActivity()
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }
}
