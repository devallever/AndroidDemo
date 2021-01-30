package allever.android.lib.compent.common.util

import android.content.Context
import android.util.Log
import android.widget.Toast


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

fun toast(msg: String?) {
    val s = msg ?: ""
    Toast.makeText(App.context, s, Toast.LENGTH_SHORT).show()
}