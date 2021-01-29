package com.allever.app.demo.alienscreen

import android.annotation.TargetApi
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.allever.app.demo.R
import allever.android.lib.compent.common.util.getStatusBarHeight
import allever.android.lib.compent.common.util.log
import kotlinx.android.synthetic.main.activity_alien_full_screen.*


class AlienFullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alien_full_screen)

//        // 全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }


        //窗口布局参数属性layoutInDisplayCutoutMode, 有三种模式
        //LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT：


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val layoutParams = window.attributes

            //1.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            //默认的布局模式，仅当刘海区域完全包含在状态栏之中时，才允许窗口延伸到刘海区域显示.
            //也就是说，如果没有设置为全屏显示模式，就允许窗口延伸到刘海区域，否则不允许。
            //全屏模式 + 这种模式 不会延伸到刘海区域
            //layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT

            //2.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER：永远不允许窗口延伸到刘海区域。
            //layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER

            //3.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES:
            //始终允许窗口延伸到屏幕短边上的刘海区域，窗口永远不会延伸到屏幕长边上的刘海区域。
            //注意：需要在代码设置全屏
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = layoutParams
        }

        val statusBarView = View(this)
        statusBarView.id = statusBarView.hashCode()
        statusBarView.setBackgroundResource(R.drawable.top_bar_bg)
        val statusBarHeight = getStatusBarHeight(this)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
        rootLayout.addView(statusBarView, lp)

        val btnLp = btnTest.layoutParams as? RelativeLayout.LayoutParams
        btnLp?.addRule(RelativeLayout.BELOW, statusBarView.id)

        getNotchParams()
    }

    /**
     * 获得刘海区域信息
     */
    @TargetApi(28)
    fun getNotchParams() {
        val decorView: View = window.decorView
        decorView.post(Runnable {
            val windowInsets: WindowInsets = decorView.rootWindowInsets
            // 当全屏顶部显示黑边时，getDisplayCutout()返回为null
            val displayCutout = windowInsets.displayCutout
            if (displayCutout != null) {
                log("安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.safeInsetLeft)
                log("安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.safeInsetRight)
                log("安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.safeInsetTop)
                log("安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.safeInsetBottom)
            }
            // 获得刘海区域
            val rectList: List<Rect>? = displayCutout?.boundingRects
            if (rectList == null || rectList.isEmpty()) {
                log("不是刘海屏")
            } else {
                log("刘海屏数量:" + rectList.size)
                for (rect in rectList) {
                    log("刘海屏区域：$rect")
                }
            }
        })
    }
}