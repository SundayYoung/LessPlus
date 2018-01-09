package com.felix.lessplus.ui.activity

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.felix.lessplus.service.PlayCache
import com.felix.lessplus.service.PlayService
import com.felix.lessplus.utils.PermissionsUtil
import org.jetbrains.anko.toast

/**
 * Created by liuhaiyang on 2017/12/7.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun setContentLayout(): Int
    protected abstract fun initData(bundle: Bundle?)

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setContentLayout())
        initData(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionsUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }


    fun getPlayService(): PlayService {
        return PlayCache.instance.mPlayService ?: throw NullPointerException("play service is null")
    }

    private fun setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}