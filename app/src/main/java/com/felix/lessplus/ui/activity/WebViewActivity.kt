package com.felix.lessplus.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.felix.lessplus.R
import com.felix.lessplus.utils.CommonUtil
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity : BaseActivity() {

    private var mUrl: String? = null

    override fun setContentLayout(): Int = R.layout.activity_webview

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData(bundle: Bundle?) {

        mUrl = intent.getStringExtra(CommonUtil.KEY_URL)
        setTitleBar()

        val webSettings = vWb.settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.javaScriptEnabled = true
        webSettings.blockNetworkImage = false

        //load url
        if (Patterns.WEB_URL.matcher(mUrl).matches()) run { vWb.loadUrl(mUrl) }

        vWb.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, progress: Int) {
                if (vPbLoad != null) {
                    vPbLoad.progress = progress
                    if (progress == 100) {
                        vPbLoad.visibility = View.GONE
                    }
                }
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                if (!TextUtils.isEmpty(title)) {
                    vToolBar.title = title
                }

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vWb.stopLoading()
        vWb.webChromeClient = null
        vWb.destroy()
    }


    private fun setTitleBar() {
        setSupportActionBar(vToolBar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        vToolBar.setNavigationOnClickListener { onBackPressed() }
    }

}
