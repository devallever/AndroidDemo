package com.allever.app.demo.audiovideo.camera

import allever.android.lib.compent.common.util.log
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.allever.app.demo.audiovideo.R
import kotlinx.android.synthetic.main.audio_video_activity_camera.*

class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback, CameraListener {

    private lateinit var cameraHelper: CameraHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_video_activity_camera)

        btnOpenPreview.setOnClickListener {
            startPreview()
        }
        btnStopPreview.setOnClickListener {
            stopPreview()
        }

        cameraHelper = CameraHelper.newIns(surfaceViewCamera.holder, this)

        surfaceViewCamera.holder.addCallback(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraHelper.destroy()
    }

    private fun startPreview() {
        cameraHelper.startPreview()
    }

    private fun stopPreview() {
        cameraHelper.stopPreview()
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        log("surfaceCreated")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        log("surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        log("surfaceDestroyed")
        cameraHelper.stopPreview()
    }

    override fun onPreview(data: ByteArray) {
        log("onPreview")
    }
}