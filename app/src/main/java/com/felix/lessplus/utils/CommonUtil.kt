package com.felix.lessplus.utils

import android.content.Context
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import com.felix.lessplus.model.bean.HotMovieResponse
import java.util.*

/**
 * Created by liuhaiyang on 2017/12/13.
 */
class CommonUtil {

    companion object {


        val KEY_TYPE: String = "keyType"
        val KEY_URL: String = "keyUrl"
        val KEY_DATA: String = "keyData"

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

        fun formatTime(time: Long): String {
            return formatTime("mm:ss", time)
        }

        private fun formatTime(pattern: String, milli: Long): String {
            val m = (milli / DateUtils.MINUTE_IN_MILLIS).toInt()
            val s = (milli / DateUtils.SECOND_IN_MILLIS % 60).toInt()
            val mm = String.format(Locale.getDefault(), "%02d", m)
            val ss = String.format(Locale.getDefault(), "%02d", s)
            return pattern.replace("mm", mm).replace("ss", ss)
        }

        /**
         * 格式化导演、主演名字
         */
        fun formatName(casts: List<HotMovieResponse.SubjectsBean.PersonBean>?): String {
            return if (casts != null && casts.isNotEmpty()) {
                val stringBuilder = StringBuilder()
                for (i in casts.indices) {
                    if (i < casts.size - 1) {
                        stringBuilder.append(casts[i].name).append(" / ")
                    } else {
                        stringBuilder.append(casts[i].name)
                    }
                }
                stringBuilder.toString()

            } else {
                "佚名"
            }
        }


        /**
         * 格式化电影类型
         */
        fun formatGenres(genres: List<String>?): String {
            return if (genres != null && genres.isNotEmpty()) {
                val stringBuilder = StringBuilder()
                for (i in genres.indices) {
                    if (i < genres.size - 1) {
                        stringBuilder.append(genres[i]).append(" / ")
                    } else {
                        stringBuilder.append(genres[i])
                    }
                }
                stringBuilder.toString()

            } else {
                "不知名类型"
            }
        }
    }
}