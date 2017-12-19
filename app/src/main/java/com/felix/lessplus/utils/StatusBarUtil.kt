package com.felix.lessplus.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.felix.lessplus.ui.customview.StatusBarView

/**
 * Created by liuhaiyang on 2017/12/18.
 */
class StatusBarUtil {

    companion object {
        /**
         * 获取状态栏高度
         *
         * @param context context
         * @return 状态栏高度
         */
        fun getStatusBarHeight(context: Context): Int {
            // 获得状态栏高度
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        }


        fun setTranslucentImageHeader(activity: Activity, alpha: Int, needOffsetView: View?) {
            setFullScreen(activity)
            //获取windowphone下的decorView
            val decorView = activity.window.decorView as ViewGroup
            val count = decorView.childCount
            //判断是否已经添加了statusBarView
            if (count > 0 && decorView.getChildAt(count - 1) is StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundColor(Color.argb(alpha, 0, 0, 0))
            } else {
                //新建一个和状态栏高宽的view
                val statusView = createTranslucentStatusBarView(activity, alpha)
                decorView.addView(statusView)
            }

            if (needOffsetView != null) {
                val layoutParams = needOffsetView.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0)
            }

        }


        fun setFullScreen(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 设置透明状态栏,这样才能让 ContentView 向上
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }

        /**
         * 创建半透明矩形 View
         *
         * @param alpha 透明值
         * @return 半透明 View
         */
        private fun createTranslucentStatusBarView(activity: Activity, alpha: Int): StatusBarView {
            // 绘制一个和状态栏一样高的矩形
            val statusBarView = StatusBarView(activity)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity))
            statusBarView.layoutParams = params
            statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
            return statusBarView
        }
    }

}