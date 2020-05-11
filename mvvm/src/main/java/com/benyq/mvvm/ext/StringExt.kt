package com.benyq.mvvm.ext

import android.text.TextUtils
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

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
