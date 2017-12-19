package com.felix.lessplus.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by liuhaiyang on 2017/12/13.
 */
class CommonUtil {

    companion object {


        val KEY_URL: String = "keyType"


        /**
         * 获得屏幕高度
         *
         * @param context
         * @return
         */
        fun getScreenWidth(context: Context): Int {
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.widthPixels
        }


        /**
         * dp转px
         *
         * @param context
         * @return
         */
        fun dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}