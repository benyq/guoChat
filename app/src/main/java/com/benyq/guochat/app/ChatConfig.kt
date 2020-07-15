package com.benyq.guochat.app

import okhttp3.MediaType.Companion.toMediaTypeOrNull

/**
 * @author benyq
 * @time 2020/5/2
 * @e-mail 1520063035@qq.com
 * @note
 */

/**
 * 聊天类型， 联系人
 */
const val CHAT_TYPE_CONTRACT = 1
/**
 * 聊天类型， 群聊
 */
const val CHAT_TYPE_GROUP = 2

const val IMG_PATH = "img"

const val VIDEO_PATH = "video"

const val GENDER_FEMALE = 0
const val GENDER_MALE = 1
const val GENDER_UNKNOWN = 2

val TEXT = "text/plain; charset=utf-8".toMediaTypeOrNull()
val STREAM = "application/octet-stream".toMediaTypeOrNull()
val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
val FormUrlEncoded = "application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull()

const val CIRCLE__TYPE_TEXT = 1
const val CIRCLE__TYPE_IMG = 2
const val CIRCLE__TYPE_VIDEO = 3