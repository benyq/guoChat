package com.benyq.guochat.chat.app

import android.content.Context
import java.io.File


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

const val CIRCLE__TYPE_TEXT = 1
const val CIRCLE__TYPE_IMG = 2
const val CIRCLE__TYPE_VIDEO = 3


fun Context.chatImgPath() : String {
    return getExternalFilesDir(IMG_PATH)!!.absolutePath + File.separator
}

fun Context.chatVideoPath(): String {
    return getExternalFilesDir(VIDEO_PATH)!!.absolutePath + File.separator
}


const val baseUrl = "http://81.69.26.237/"

const val chatIdPrefix = "chat-"