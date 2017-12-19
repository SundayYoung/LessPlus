package com.felix.lessplus.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by liuhaiyang on 2017/12/7.
 */
abstract class BaseFragment : Fragment() {

    var mContext: Context? = null

    protected abstract fun setContentLayout(): Int
    protected abstract fun initData(bundle: Bundle?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = activity
        return inflater.inflate(setContentLayout(), container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
    }
}