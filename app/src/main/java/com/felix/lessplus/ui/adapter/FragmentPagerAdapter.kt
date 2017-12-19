package com.felix.lessplus.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.ViewGroup

/**
 * Created by liuhaiyang on 2017/12/7.
 */

class FragmentPagerAdapter : android.support.v4.app.FragmentPagerAdapter {

    private var mFragment: MutableList<*>? = null
    private var mTitleList: List<String>? = null

    constructor(fm: FragmentManager, mFragment: MutableList<*>) : super(fm) {
        this.mFragment = mFragment
    }

    constructor(fm: FragmentManager, mFragment: MutableList<*>, mTitleList: List<String>) : super(fm) {
        this.mFragment = mFragment
        this.mTitleList = mTitleList
    }

    override fun getItem(position: Int): Fragment {
        return mFragment!![position] as Fragment
    }

    override fun getCount(): Int {
        return mFragment!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (mTitleList != null) {
            mTitleList!![position]
        } else {
            ""
        }
    }

    fun addFragmentList(fragment: MutableList<*>) {
        this.mFragment!!.clear()
        this.mFragment = null
        this.mFragment = fragment
        notifyDataSetChanged()
    }

}
