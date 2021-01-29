package com.allever.app.demo.audiovideo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.allever.app.demo.audiovideo.drawimage.DrawImageActivity
import allever.android.lib.compent.common.util.ActivityCollector
import kotlinx.android.synthetic.main.audio_video_activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_video_activity_main)

        btnDrawImage.setOnClickListener {
            ActivityCollector.startActivity(this, DrawImageActivity::class.java)
        }
    }
}