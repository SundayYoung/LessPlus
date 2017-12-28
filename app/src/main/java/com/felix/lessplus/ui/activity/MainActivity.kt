package com.felix.lessplus.ui.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.felix.lessplus.R
import com.felix.lessplus.ui.adapter.FragmentPagerAdapter
import com.felix.lessplus.ui.fragment.BaicFragment
import com.felix.lessplus.ui.fragment.MusicFragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.include_play_bar.*
import java.util.ArrayList
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Handler
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import com.felix.lessplus.service.PlayCache
import com.felix.lessplus.service.PlayService
import com.felix.lessplus.utils.StatusBarUtil


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mTitleList: ArrayList<String>? = null
    private var mFragments: ArrayList<Fragment>? = null

    private var mSlideOffSet: Float = 0f
    private var mStatusBarColor: Int = 0

    private var mPlayServiceConnection: ServiceConnection? = null

    override fun setContentLayout(): Int = R.layout.activity_main

    override fun initData(bundle: Bundle?) {

        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        initFragment()
        initPlayBar()

        checkService()
    }

    fun checkService() {
        if (PlayCache.instance.mPlayService == null) {
            val intent = Intent(this, PlayService::class.java)
            startService(intent)

            val handler = Handler()
            handler.postDelayed({
                val intent = Intent()
                intent.setClass(this, PlayService::class.java)
                mPlayServiceConnection = PlayServiceConnection()
                bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE)
            },1000)
        }
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


    private fun initPlayBar() {
        vSlidingLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                mSlideOffSet = slideOffset
                val sOff: Float

                if (slideOffset >= 0.5f) {
                    sOff = (mSlideOffSet - 0.5f) * 2
                    vClBottomBar.alpha = 1 - sOff
                    vTvPlayMusicTitle.alpha = sOff
                    vTvPlayMusicAuthor.alpha = sOff
                }

                if (slideOffset == 0f) {
                    vClBottomBar.alpha = 1f
                    vTvPlayMusicTitle.alpha = 0f
                    vTvPlayMusicAuthor.alpha = 0f
                }

            }

            override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {
                // || mSlideOffSet == 1.0f
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    StatusBarUtil.setStatusBarColor(this@MainActivity, mStatusBarColor, false)
                } else {
                    StatusBarUtil.setStatusBarColor(this@MainActivity, ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark), false)
                }
            }
        })

        vIvBg.setBackgroundResource(R.drawable.ic_play_bg)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_play_bg)

        Palette.from(bitmap).generate { palette: Palette ->
            val ab: Palette.Swatch? = palette.darkVibrantSwatch
            mStatusBarColor = ab?.rgb ?: 0
        }
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
//            startActivity(Intent(this, TestActivity::class.java))
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

    override fun onDestroy() {
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection)
        }
        super.onDestroy()
    }

    /**
     * service connection
     */
    private inner class PlayServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val playService = (service as PlayService.PlayBinder).service
            PlayCache.instance.mPlayService = playService

        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }
}
