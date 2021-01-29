package com.allever.app.demo.audiovideo.drawimage

import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.allever.app.demo.audiovideo.R
import kotlinx.android.synthetic.main.audio_video_activity_draw_image.*

class DrawImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_video_activity_draw_image)

        surfaceViewDrawImage.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    val canvas = holder.lockCanvas()
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
                    canvas.drawBitmap(bitmap, 0f, 0f, Paint())
                    holder.unlockCanvasAndPost(canvas)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
}