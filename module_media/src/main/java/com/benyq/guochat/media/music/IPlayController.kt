package com.benyq.guochat.media.music

/**
 * @author benyq
 * @time 2020/4/12
 * @e-mail 1520063035@qq.com
 * @note 播放控制
 */
interface IPlayController {

    /**
     * 手动点击与自动都调这个方法
     */
    fun playNext()

    fun playPrevious()

    fun playAgain()

    fun playPause(): Boolean

    fun start(): Boolean

    /**
     * 设置进度
     */
    fun setSeek(progress: Int)

    /**
     * 获取时间 00:12
     */
    fun getTrackTime(): String

    /**
     * 修改播放模式，顺序、随机、循环
     */
    fun changeMode()

    /**
     * 继续播放
     */
    fun play(): Boolean
}