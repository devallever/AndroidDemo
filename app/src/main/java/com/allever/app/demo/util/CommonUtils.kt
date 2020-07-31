package com.allever.app.demo.util

import android.content.Context
import android.util.Log

private val TAG = "ILogger"

/**
 * 获取状态栏高度
 *
 * @param context
 * @return
 */
fun getStatusBarHeight(context: Context): Int {
    var statusBarHeight = 0
    val resourceId: Int =
        context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
    }

    return statusBarHeight
}

fun log(content: String) {
    Log.d(TAG, content)
}