package com.felix.lessplus.ui.activity

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity

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
}