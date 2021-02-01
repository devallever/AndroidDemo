package com.allever.app.demo.audiovideo.audiorecord

import allever.android.lib.compent.common.util.App
import allever.android.lib.compent.common.util.toast
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.allever.app.demo.audiovideo.R
import kotlinx.android.synthetic.main.audio_video_activity_audio_record.*
import java.io.File
import kotlin.concurrent.thread

class AudioRecordActivity : AppCompatActivity() {
    private var mDir = App.context.externalCacheDir
    private var mPcmFileName: String? = null
    private var mWavFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_video_activity_audio_record)

        btnStartRecord.setOnClickListener {
            startRecord()
        }

        btnStopRecord.setOnClickListener {
            stopRecord()
        }

        btnCovertWav.setOnClickListener {
            covert2Wav()
        }

        btnPlayRecord.setOnClickListener {
            playRecord()
        }

        btnStopPlayRecord.setOnClickListener {
            stopPlayRecord()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        AudioRecordManager.getIns().destroy()
    }

    private fun startRecord() {
        mPcmFileName = "${System.currentTimeMillis()}.pcm"
        AudioRecordManager.getIns().startRecord(getPcmFilePath())
    }

    private fun stopRecord() {
        AudioRecordManager.getIns().stopRecord()
        val filePath = getPcmFilePath()
        tvPcmPath.text = "PCM: $filePath"
    }

    private fun covert2Wav() {
        val wavFilePath = getWavFilePath()
        thread {
            AudioRecordManager.getIns().pcmToWav(getPcmFilePath(), wavFilePath)
            runOnUiThread {
                tvWavPath.text = "WAV: $wavFilePath"
            }
        }
    }

    private fun playRecord() {
        toast("播放pcm")
        AudioRecordManager.getIns().play(getPcmFilePath())
    }

    private fun stopPlayRecord() {
        toast("停止播放")
        AudioRecordManager.getIns().stopPlay()
    }

    private fun getPcmFilePath(): String = "$mDir${File.separator}$mPcmFileName"

    private fun getWavFilePath(): String =
        "$mDir${File.separator}${(File(getPcmFilePath()).name).split(".")[0]}.wav"
}