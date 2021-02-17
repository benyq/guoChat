package com.benyq.guochat.media.video

/**
 * author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 相机参数设置
 */

data class VideoConfig(

    //预览分辨率
    var videoResolution: VideoResolution = VideoResolution.VIDEO_1080,
    //帧率
    var frameRate: VideoFrameRate = VideoFrameRate.FRAME_RATE_FPS_30,
    //默认前置相机
    var frontCamera : Boolean = true

)

enum class VideoResolution(val width: Int, val height: Int) {

    VIDEO_1080(1920, 1080),
    VIDEO_720P(1280, 720),
    VIDEO_480(854, 480),
    VIDEO_360(640, 360)

}

enum class VideoFrameRate(val fps: Int) {
    FRAME_RATE_FPS_20(20),
    FRAME_RATE_FPS_25(25),
    FRAME_RATE_FPS_30(30)
}

