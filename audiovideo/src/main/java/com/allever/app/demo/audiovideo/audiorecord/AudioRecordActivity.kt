package com.allever.app.demo.audiovideo.audiorecord

import allever.android.lib.compent.common.util.App
import allever.android.lib.compent.common.util.log
import allever.android.lib.compent.common.util.toast
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.allever.app.demo.audiovideo.R
import kotlinx.android.synthetic.main.audio_video_activity_audio_record.*
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

class AudioRecordActivity : AppCompatActivity() {

    private var mAudioRecord: AudioRecord? = null
    private var mBufferSize = 0
    private val SAMPLE_RATE_IN_HZ = 44100 //Hz，采样频率
    private var isRecord = false
    private var bufferByte: ByteArray? = null

    private var mDir = App.context.externalCacheDir
    private var mPcmFileName: String? = null
    private var mWavFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_video_activity_audio_record)

        initAudioRecord()

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

    }

    override fun onDestroy() {
        super.onDestroy()
        mAudioRecord?.release()
        mAudioRecord = null
    }

    private fun startRecord() {
        if (isRecord) {
            return
        }
        mAudioRecord?.startRecording()
        isRecord = true
        mPcmFileName = "${System.currentTimeMillis()}.pcm"
        thread {
            while (isRecord) {
                log("正在录制音频")
                var fileOutputStream: FileOutputStream? = null
                try {
                    fileOutputStream = FileOutputStream(getPcmFilePath())
                    val read = mAudioRecord?.read(bufferByte!!, 0, mBufferSize)
                        ?: AudioRecord.ERROR_INVALID_OPERATION
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        fileOutputStream.write(bufferByte)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    fileOutputStream?.close()
                }
            }
            log("停止录制音频")
        }
    }

    private fun stopRecord() {
        mAudioRecord?.stop()
        isRecord = false
        val filePath = getPcmFilePath()
        tvPcmPath.text = "PCM: $filePath"
    }

    private fun covert2Wav() {
        val wavFilePath = getWavFilePath()
        thread {
            AudioRecordManager.pcmToWav(
                getPcmFilePath(),
                wavFilePath,
                SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_MONO,
                mBufferSize
            )
            runOnUiThread {
                tvWavPath.text = "WAV: $wavFilePath"
            }
        }
    }

    private fun playRecord() {
        toast("播放wav")
    }

    private fun initAudioRecord() {
        mBufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        mAudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT, mBufferSize
        )
        bufferByte = ByteArray(mBufferSize)
    }

    private fun getPcmFilePath(): String = "$mDir${File.separator}$mPcmFileName"

    private fun getWavFilePath(): String =
        "$mDir${File.separator}${(File(getPcmFilePath()).name).split(".")[0]}.wav"
}