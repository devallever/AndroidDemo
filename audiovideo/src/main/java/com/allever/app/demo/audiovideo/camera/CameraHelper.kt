package com.allever.app.demo.audiovideo.camera

import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder

/**
 *
 */
class CameraHelper(private val holder: SurfaceHolder, private var cameraListener: CameraListener?) {

    private var camera: Camera? = null
    private var isPreviewing = false

    private val previewCallback = Camera.PreviewCallback { data, camera ->
        log("Camera预览回调")
        cameraListener?.onPreview(data)
    }

    private fun initCamera() {
        camera = Camera.open()
        camera?.setPreviewCallback(previewCallback)
    }

    fun startPreview() {
        if (isPreviewing) {
            return
        }

        if (camera == null) {
            initCamera()
        }
        camera?.setPreviewDisplay(holder)
        camera?.startPreview()
        isPreviewing = true
    }

    fun stopPreview() {
        camera?.stopPreview()
        isPreviewing = false
    }

    fun destroy() {
        camera?.release()
        camera = null
        isPreviewing = false
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }

    companion object {
        private val TAG: String = CameraHelper::class.java.simpleName
        fun newIns(holder: SurfaceHolder, cameraListener: CameraListener?) =
            CameraHelper(holder, cameraListener)
    }

}