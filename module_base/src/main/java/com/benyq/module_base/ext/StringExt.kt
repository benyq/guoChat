package com.benyq.module_base.ext

import android.text.TextUtils
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2020/1/11
 * description: 字符串拓展方法
 */


/**
 * Extension method to get md5 string.
 */
fun String.md5() = encrypt(this, "MD5")

/**
 * Extension method to get encrypted string.
 */
private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

/**
 * Extension method to convert byteArray to String.
 */
fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}

fun <T> String.toNumberDefault(default: T) : T {
    return try {
        val res: Any = when (default) {
            is Long -> toLong()
            is Int -> toInt()
            is Float -> toFloat()
            is Double -> toDouble()
            else ->  throw NumberFormatException("未找到该类型")
        }
        res as T
    }catch (e: NumberFormatException) {
        default
    }
}


fun stringCheckNull(value: String?, default: String = "--"): String{
    return if (!TextUtils.isEmpty(value)) value!! else default
}

fun String.isJson(): Boolean {
    val jsonParser = JsonParser()
    return try {
        val jsonElement = jsonParser.parse(this)
        jsonElement.isJsonObject
    }catch (e: JsonSyntaxException){
        false
    }
}

fun getCurrentDate(): String {
    val df = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE)
    return df.format(Date())
}

//计算时间
fun calculateTime(time: Int): String {
    loge("video duration $time")
    return if (time > 60) {
        val second = time % 60
        val minute = time / 60
        (if (minute < 10) "0$minute" else "" + minute) + if (second < 10) ":0$second" else ":$second"
    } else {
        if (time < 10) {
            "00:0$time"
        } else {
            "00:$time"
        }
    }
}