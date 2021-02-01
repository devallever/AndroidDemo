package com.allever.app.demo.audiovideo.audiorecord;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

import androidx.fragment.app.FragmentActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import allever.android.lib.compent.common.util.CommonUtilsKt;
import kotlin.math.UMathKt;

/**
 * @author allever
 */
public class AudioRecordManager {

    /**
     * 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
     */
    private static final int SAMPLE_RATE_INHZ = 44100;

    /**
     * 声道数。CHANNEL_IN_MONO and CHANNEL_IN_STEREO. 其中CHANNEL_IN_MONO是可以保证在所有设备能够使用的。
     */
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;

    /**
     * 返回的音频数据的格式。 ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, and ENCODING_PCM_FLOAT.
     */
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord mAudioRecord;
    private boolean mIsRecording = false;
    private int mMinBufferSize = 0;
    private byte[] mBytes;
    private ExecutorService mExecutor = Executors.newCachedThreadPool();
    private AudioTrack mAudioTrack;
    private boolean mIsPlaying = false;

    private AudioRecordManager() {
        mMinBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        mBytes = new byte[mMinBufferSize];
    }

    private static class Holder {
        private static final AudioRecordManager INS = new AudioRecordManager();
    }

    public static AudioRecordManager getIns() {
        return Holder.INS;
    }

    public void startRecord(final String pcmPath) {
        if (mIsRecording) {
            return;
        }
        if (mAudioRecord == null) {
            mAudioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE_INHZ,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT,
                    mMinBufferSize);
        }

        if (mBytes == null) {
            mBytes = new byte[mMinBufferSize];
        }

        mIsRecording = true;
        mAudioRecord.startRecording();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(pcmPath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (null != fileOutputStream) {
                    while (mIsRecording) {
                        int read = mAudioRecord.read(mBytes, 0, mMinBufferSize);
                        log("read = " + read);
                        // 如果读取音频数据没有出现错误，就将数据写入到文件
                        if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                            try {
                                fileOutputStream.write(mBytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    log("停止录音");
                    try {
                        log("run: close file output stream !");
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void stopRecord() {
        mIsRecording = false;
    }

    public void play(final String path) {
        if (mAudioTrack == null) {
            mAudioTrack = new AudioTrack(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build(),
                    new AudioFormat.Builder().setSampleRate(SAMPLE_RATE_INHZ)
                            .setEncoding(AUDIO_FORMAT)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build(),
                    mMinBufferSize,
                    AudioTrack.MODE_STREAM,
                    AudioManager.AUDIO_SESSION_ID_GENERATE);
        }

        if (mIsPlaying) {
            stopPlay();
            return;
        }

        mAudioTrack.play();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(path);
                    byte[] tempBuffer = new byte[mMinBufferSize];
                    mIsPlaying = true;
                    while (fileInputStream.available() > 0 && mIsPlaying) {
                        int readCount = fileInputStream.read(tempBuffer);
                        if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                                readCount == AudioTrack.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (readCount != 0 && readCount != -1) {
                            mAudioTrack.write(tempBuffer, 0, readCount);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mIsPlaying = false;
                }
            }
        });
    }

    public void stopPlay() {
        if (mAudioTrack != null) {
            mAudioTrack.stop();
        }
        mIsPlaying = false;
    }

    public void destroy() {
        if (mAudioRecord != null) {
            mAudioRecord.release();
            mAudioRecord = null;
        }
        mBytes = null;
        mIsRecording = false;

        if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    public void pcmToWav(String inFilename, String outFilename) {
        pcmToWav(inFilename, outFilename, SAMPLE_RATE_INHZ, CHANNEL_CONFIG);
    }


    /**
     * pcm文件转wav文件
     *
     * @param inFilename  源文件路径
     * @param outFilename 目标文件路径
     * @param sampleRate  sample rate、采样率
     * @param channel     channel、声道
     */
    public void pcmToWav(String inFilename, String outFilename, int sampleRate, int channel) {
        FileInputStream in;
        FileOutputStream out;
        long totalAudioLen;
        long totalDataLen;
        int channels = channel == AudioFormat.CHANNEL_IN_MONO ? 1 : 2;
        long byteRate = 16 * sampleRate * channels / 8;
        byte[] data = new byte[mMinBufferSize];
        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            writeWaveFileHeader(out, totalAudioLen, totalDataLen,
                    sampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 加入wav文件头
     */
    private void writeWaveFileHeader(FileOutputStream out,
                                     long totalAudioLen,
                                     long totalDataLen,
                                     long longSampleRate,
                                     int channels,
                                     long byteRate) throws IOException {
        byte[] header = new byte[44];
        // RIFF/WAVE header
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        //WAVE
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        // 'fmt ' chunk
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        // 4 bytes: size of 'fmt ' chunk
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        // format = 1
        header[20] = 1;
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // block align
        header[32] = (byte) (2 * 16 / 8);
        header[33] = 0;
        // bits per sample
        header[34] = 16;
        header[35] = 0;
        //data
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

    private void log(String msg) {
        CommonUtilsKt.log(msg);
    }
}
