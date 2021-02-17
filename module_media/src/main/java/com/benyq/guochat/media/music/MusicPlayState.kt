package com.benyq.guochat.media.music

/**
 * @author benyq
 * @time 2020/5/5
 * @e-mail 1520063035@qq.com
 * @note
 */
enum class MusicPlayState(value: String) {
    PREPARE("MediaPlayer--准备完毕"),
    COMPLETE("MediaPlayer--播放结束"),
    ERROR("MediaPlayer--播放错误"),
    EXCEPTION("MediaPlayer--播放异常"),
    START("MediaPlayer--播放开始"),
    PROGRESS("MediaPlayer--播放进度回调"),
}