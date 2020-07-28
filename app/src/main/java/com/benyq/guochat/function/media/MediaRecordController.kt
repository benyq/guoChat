package com.benyq.guochat.function.media

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import com.benyq.guochat.app.App
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ext.tryCatch
import java.io.File


/**
 * @author benyq
 * @time 2020/4/28
 * @e-mail 1520063035@qq.com
 * @note 录音管理,包括录音、播放
 * 20200512 新增  这个类还要包括视频的录制
 */
object MediaRecordController {

    private var recordFilePath: String =
        App.sInstance.getExternalFilesDir("soundRecord")!!.absolutePath + "/"
    private var startTime = 0L

    private var mVoiceFilePath: String = ""

    private lateinit var mRecord: MediaRecorder

    private var mRecordId = 0L

    private var mCompleteAction: ((Long) -> Unit)? = null

    private val mPlayer: MediaPlayer = MediaPlayer().apply {
        setOnPreparedListener {
            it.start()
        }
        setOnCompletionListener {
            mCompleteAction?.invoke(mRecordId)
        }
        setOnErrorListener { mp, what, extra ->
            mRecordId = 0L
            true
        }
    }

    fun startVoiceRecord() {
        mVoiceFilePath =
            getFileName()
        mRecord = MediaRecorder().apply {
            //必须按照这个顺序，否则会报错
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(mVoiceFilePath)
            setAudioSamplingRate(44100)
            setAudioEncodingBitRate(192000)
        }

        loge("mVoiceFilePath $mVoiceFilePath")

        tryCatch({
            mRecord.prepare()
            startTime = System.currentTimeMillis()
            mRecord.start()
        }, {
            loge("MediaRecorder prepare() failed")
        })
    }

    /**
     * isCancel true 放弃本次录音
     */
    fun stopVideoRecord(isCancel: Boolean = false): VoiceBean? {
        mRecord.stop()
        val duration: Long = System.currentTimeMillis() - startTime
        //小于 1秒 的 录音视为无效
        if (duration < 1000 || isCancel) {
            deleteVoiceFile()
        }
        mRecord.reset()
        mRecord.release()
        return if (isCancel) {
            null
        } else {
            VoiceBean(mVoiceFilePath, duration)
        }
    }

    /**
     * @param recordId 数据库聊天记录Id
     */
    fun playVideoRecord(context: Context, voicePath: String, recordId: Long) {
        mRecordId = recordId
        val amanager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0)
        mPlayer.setAudioAttributes(
            AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_ALARM).build()
        )
        mPlayer.reset()
        mPlayer.setDataSource(
            voicePath
        )
        mPlayer.setVolume(1.0f, 1.0f);
        mPlayer.prepareAsync()
    }

    fun reset() {
        mCompleteAction = null
        mPlayer.release()
    }

    fun setCompleteAction(action: (Long)->Unit) {
        mCompleteAction = action
    }

    private fun getFileName(): String {
        val soundDir = File(recordFilePath)
        if (!soundDir.exists()) {
            soundDir.mkdir()
        }
        return recordFilePath + System.currentTimeMillis() + ".amr";
    }

    private fun deleteVoiceFile() {
        val file = File(mVoiceFilePath)
        if (file.exists()) {
            file.delete()
            mVoiceFilePath = ""
        }
    }

}