package com.allever.app.demo.alienscreen

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.allever.app.demo.R
import allever.android.lib.compent.common.util.getStatusBarHeight
import allever.android.lib.compent.common.util.log
import kotlinx.android.synthetic.main.activity_alien_screen_main.*

class AlienScreenMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alien_screen_main)

        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
        }

        val statusBarHeight = getStatusBarHeight(this)
        log("状态栏高度 = $statusBarHeight")
        rlRoot.setPadding(
            rlRoot.paddingLeft,
            rlRoot.paddingTop + statusBarHeight,
            rlRoot.paddingRight,
            rlRoot.paddingBottom
        )
    }
}