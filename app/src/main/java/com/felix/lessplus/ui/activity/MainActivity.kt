package com.felix.lessplus.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
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
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.graphics.Palette
import android.text.format.DateUtils
import android.widget.SeekBar
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.felix.lessplus.model.bean.Music
import com.felix.lessplus.service.OnPlayerEventListener
import com.felix.lessplus.utils.CommonUtil
import com.felix.lessplus.utils.GlideImageLoader
import com.felix.lessplus.utils.StatusBarUtil
import kotlinx.android.synthetic.main.include_play_controller.*
import org.jetbrains.anko.toast


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, OnPlayerEventListener {

    private var mTitleList: ArrayList<String>? = null
    private var mFragments: ArrayList<Fragment>? = null

    private var mSlideOffSet: Float = 0f
    private var mStatusBarColor: Int = 0
    private var mPlayColor: Int = R.color.colorPrimary

    private var mLastProgress: Int = 0


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
        setClickListener()

        getPlayService().onPlayEventListener = this

    }


    /**
     * add fragment
     */
    private fun initFragment() {
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


    /**
     * Bottom play bar
     */
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

                vClBottomBar.visibility = if (slideOffset == 1f) View.GONE else View.VISIBLE

            }

            override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {
                // || mSlideOffSet == 1.0f
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    StatusBarUtil.setStatusBarColor(this@MainActivity, mStatusBarColor, false)
                } else {
                    StatusBarUtil.setStatusBarColor(this@MainActivity, ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark), false)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        })

        vTvPlayMusicTitle.isSelected = true

        vSbProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                    vTvCurrentTime.text = CommonUtil.formatTime(progress.toLong())
                    mLastProgress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    private fun setClickListener() {
        vIvPlay.setOnClickListener( {
            getPlayService().playPause()
        })
        vIvFramePlay.setOnClickListener({
            getPlayService().playPause()
        })
        vIvPrev.setOnClickListener({
            toast("上一首")
        })
        vIvNext.setOnClickListener({
            toast("下一首")
        })
    }
    
    private fun setPaletteStatusBarColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette: Palette ->
            val vDark: Palette.Swatch? = palette.darkVibrantSwatch
            mStatusBarColor = vDark?.rgb ?: 0

            val vLight: Palette.Swatch? = palette.lightVibrantSwatch
            mPlayColor = vLight?.rgb ?: ContextCompat.getColor(this, R.color.colorPrimary)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (vSlidingLayout != null && (vSlidingLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED || vSlidingLayout.panelState == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            vSlidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else {
            moveTaskToBack(true)
//            super.onBackPressed()
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
        val service = getPlayService()
        service.onPlayEventListener = null
        super.onDestroy()
    }



    /**
     * Music status
     */
    override fun onChange(music: Music) {
        if (vSlidingLayout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN) {
            vSlidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
        //play bar
        GlideImageLoader.displayImage(this, music.coverPath, vIvPlayBarCover)
        vTvPlayBarTitle.text = music.title
        vTvPlayBarArtist.text = music.artist

        //frame play
        GlideImageLoader.callBackBitmap(this, music.coverPath).into(object : SimpleTarget<Bitmap>() {

            override fun onResourceReady(bitmap: Bitmap?, transition: Transition<in Bitmap>?) {
                if (bitmap != null) {
                    vIvBg.setImageBitmap(bitmap)
                    setPaletteStatusBarColor(bitmap)
                }
            }
        })
        vTvPlayMusicTitle.text = music.title
        vTvPlayMusicAuthor.text = getString(R.string.music_artist, music.artist)

        vSbProgress.max = music.duration.toInt()
        vTvTotalTime.text = CommonUtil.formatTime(music.duration)
        setPlayDrawableColor()


    }

    @SuppressLint("ResourceAsColor")
    private fun setPlayDrawableColor() {
        val mDrawable = GradientDrawable()
        mDrawable.shape = GradientDrawable.OVAL
        mDrawable.useLevel = false
        mDrawable.setColor(mStatusBarColor)

        vIvFramePlayBg.setColor(mStatusBarColor)
        vIvPrev.background = mDrawable
        vIvNext.background = mDrawable

    }

    override fun onPlayerStart() {
        vIvPlay.isSelected = true
        vIvFramePlay.isSelected = true
    }

    override fun onPlayerPause() {
        vIvPlay.isSelected = false
        vIvFramePlay.isSelected = false
    }

    override fun onPublish(progress: Int) {
        vSbProgress.progress = progress
    }

    override fun onBufferingUpdate(percent: Int) {
        vSbProgress.secondaryProgress = vSbProgress.max * 100 / percent
    }
}
