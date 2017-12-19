package com.felix.lessplus.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.felix.lessplus.R
import com.felix.lessplus.ui.adapter.FragmentPagerAdapter
import com.felix.lessplus.ui.fragment.BaicFragment
import com.felix.lessplus.ui.fragment.MusicFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.ArrayList

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mTitleList: ArrayList<String>? = null
    private var mFragments: ArrayList<Fragment>? = null

    override fun setContentLayout(): Int = R.layout.activity_main

    override fun initData(bundle: Bundle?) {

        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        initFragment()

    }


    /**
     * add fragment
     */
    fun initFragment() {
        mTitleList = ArrayList()
        mFragments = ArrayList()

        mTitleList!!.add("Music")
        mTitleList!!.add("Gank")
        mTitleList!!.add("Movie")
        mTitleList!!.add("Books")

        mFragments!!.add(MusicFragment())
        mFragments!!.add(BaicFragment())
        mFragments!!.add(BaicFragment())
        mFragments!!.add(BaicFragment())

        val fAdapter = FragmentPagerAdapter(supportFragmentManager, mFragments!!, mTitleList!!)
        vpHome.adapter = fAdapter
        vpHome.offscreenPageLimit = mFragments!!.size
        fAdapter.notifyDataSetChanged()

        tbHome.setupWithViewPager(vpHome)
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

//        when(id) {
//            R.id.nav_camera
//        }

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
