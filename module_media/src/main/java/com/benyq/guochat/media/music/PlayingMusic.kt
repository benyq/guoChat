package com.benyq.guochat.media.music

/**
 * @author benyq
 * @time 2020/4/16
 * @e-mail 1520063035@qq.com
 * @note
 */
data class PlayingMusic(
    var nowTime:String = "00:00",
    var allTime:String = "00:00",
    var duration:Int = 0,
    var playerPosition:Int = 0
)