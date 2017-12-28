package com.felix.lessplus.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.felix.lessplus.R


@SuppressLint("StaticFieldLeak")
/**
 * SharedPreferences工具类
 * Created by liuhaiyang on 2017/12/28.
 */
object PreferencesUtil {
    private val MUSIC_ID = "music_id"
    private val PLAY_MODE = "play_mode"
    private val SPLASH_URL = "splash_url"
    private val NIGHT_MODE = "night_mode"

    private var sContext: Context? = null

    val currentSongId: Long
        get() = getLong(MUSIC_ID, -1)

    val playMode: Int
        get() = getInt(PLAY_MODE, 0)

    val splashUrl: String
        get() = getString(SPLASH_URL, "")

    val isNightMode: Boolean
        get() = getBoolean(NIGHT_MODE, false)

    val filterSize: String
        get() = getString(sContext!!.getString(R.string.setting_key_filter_size), "0")

    val filterTime: String
        get() = getString(sContext!!.getString(R.string.setting_key_filter_time), "0")

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(sContext)

    fun init(context: Context) {
        sContext = context.applicationContext
    }

    fun saveCurrentSongId(id: Long) {
        saveLong(MUSIC_ID, id)
    }

    fun savePlayMode(mode: Int) {
        saveInt(PLAY_MODE, mode)
    }

    fun saveSplashUrl(url: String) {
        saveString(SPLASH_URL, url)
    }

    fun enableMobileNetworkPlay(): Boolean {
        return getBoolean(sContext!!.getString(R.string.setting_key_mobile_network_play), false)
    }

    fun saveMobileNetworkPlay(enable: Boolean) {
        saveBoolean(sContext!!.getString(R.string.setting_key_mobile_network_play), enable)
    }

    fun enableMobileNetworkDownload(): Boolean {
        return getBoolean(sContext!!.getString(R.string.setting_key_mobile_network_download), false)
    }

    fun saveNightMode(on: Boolean) {
        saveBoolean(NIGHT_MODE, on)
    }

    fun saveFilterSize(value: String) {
        saveString(sContext!!.getString(R.string.setting_key_filter_size), value)
    }

    fun saveFilterTime(value: String) {
        saveString(sContext!!.getString(R.string.setting_key_filter_time), value)
    }

    private fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    private fun saveBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    private fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    private fun saveInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    private fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    private fun saveLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    private fun getString(key: String, defValue: String?): String {
        return preferences.getString(key, defValue)
    }

    private fun saveString(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }
}
