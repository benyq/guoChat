package com.benyq.guochat.function.other

import com.benyq.mvvm.ext.loge
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * @author benyq
 * @time 2020/5/2
 * @e-mail 1520063035@qq.com
 * @note 时间转换
 */

/**
 * 毫秒级时间戳转换为时间
 */
object DateFormatUtil {

    private const val HOUR_OF_DAY = 24
    private const val DAY_OF_YESTERDAY = 2L
    private const val DAY_OF_WEEK = 8L
    private const val TIME_UNIT = 60

    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE)
    val simpleDateFormatNotHour = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)
    var weekArray = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")

    fun dateLongToString(timeMillis: Long) : String{
        val calendar = Calendar.getInstance()

        val curTime = calendar.timeInMillis

        calendar.timeInMillis = timeMillis
        val date = Date(timeMillis)

        //将MISC 转换成 sec
        //将MISC 转换成 sec
        val difSec: Long = abs((curTime - timeMillis) / 1000)
        val difMin = difSec / TIME_UNIT
        val difHour = difMin / TIME_UNIT
        val difDate = difHour / HOUR_OF_DAY
        val oldHour = calendar[Calendar.HOUR]
        val hs = simpleDateFormat.format(date).substringAfter(" ")
        return when {
            difDate == 0L -> {
                //今天
                hs
            }
            difDate < DAY_OF_YESTERDAY -> {
                "昨天 $hs"
            }
            difDate < DAY_OF_WEEK -> {
                "${weekArray[calendar.get(Calendar.DAY_OF_WEEK) - 1]} $hs"
            }
            else -> simpleDateFormatNotHour.format(date)
        }
    }

    fun dateToString(pattern: String, date: Date) : String{
        val format: DateFormat =
            SimpleDateFormat(pattern, Locale.CHINA)
        return format.format(date)
    }
}