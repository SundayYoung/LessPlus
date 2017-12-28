package com.felix.lessplus.extendmusic

/**
 * 播放模式
 * Created by liuhaiyang on 2017/12/26.
 */
enum class PlayModeEnum private constructor(private val value: Int) {
    LOOP(0),
    SHUFFLE(1),
    SINGLE(2);

    fun value(): Int {
        return value
    }

    companion object {

        fun valueOf(value: Int): PlayModeEnum {
            when (value) {
                1 -> return SHUFFLE
                2 -> return SINGLE
                0 -> return LOOP
                else -> return LOOP
            }
        }
    }
}
