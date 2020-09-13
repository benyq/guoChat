/*
 * Copyright (c) 2020. vipyinzhiwei <vipyinzhiwei@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benyq.guochat.ui.openeye

import android.app.Activity
import android.content.Context
import com.benyq.guochat.R
import com.benyq.guochat.ui.common.WebViewActivity
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.loge
import java.net.URLDecoder

/**
 * actionUrl事件处理工具类。通过截取actionUrl相关信息，并进行相应事件处理。
 *
 * @author vipyinzhiwei
 * @since  2020/6/14
 */
object ActionUrl {

    private const val TAG = "eyepetizer://tag/"

    private const val DETAIL = "eyepetizer://detail/"

    private const val RANKLIST = "eyepetizer://ranklist/"

    private const val WEBVIEW = "eyepetizer://webview/?title="

    private const val REPLIES_HOT = "eyepetizer://replies/hot?"

    private const val TOPIC_DETAIL = "eyepetizer://topic/detail?"

    private const val COMMON_TITLE = "eyepetizer://common/?title"

    private const val LT_DETAIL = "eyepetizer://lightTopic/detail/"

    private const val CM_TOPIC_SQUARE = "eyepetizer://community/topicSquare"

    private const val HP_NOTIFI_TAB_ZERO = "eyepetizer://homepage/notification?tabIndex=0"

    private const val CM_TAGSQUARE_TAB_ZERO = "eyepetizer://community/tagSquare?tabIndex=0"

    private const val CM_TOPIC_SQUARE_TAB_ZERO = "eyepetizer://community/tagSquare?tabIndex=0"

    private const val HP_SEL_TAB_TWO_NEWTAB_MINUS_THREE = "eyepetizer://homepage/selected?tabIndex=2&newTabIndex=-3"
    
    /**
     * 处理ActionUrl事件。
     *
     * @param activity 上下文环境
     * @param actionUrl 待处理的url
     * @param toastTitle toast提示标题 or 没有匹配的事件需要处理，给出的提示标题。
     */
    fun process(activity: Context, actionUrl: String?, toastTitle: String = "") {
        if (actionUrl == null) return
        val decodeUrl = URLDecoder.decode(actionUrl, "UTF-8")
        when {
            decodeUrl.startsWith(WEBVIEW) -> {
                WebViewActivity.gotoWeb(activity, decodeUrl.getWebViewInfo().last(), decodeUrl.getWebViewInfo().first())
            }
            decodeUrl == RANKLIST -> {
                Toasts.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(TAG) -> {
                Toasts.show(R.string.currently_not_supported)
            }
            decodeUrl == HP_SEL_TAB_TWO_NEWTAB_MINUS_THREE -> {
//                EventBus.getDefault().post(SwitchPagesEvent(DailyFragment::class.java))
            }
            decodeUrl == CM_TAGSQUARE_TAB_ZERO -> {
                Toasts.show(R.string.currently_not_supported)
            }
            decodeUrl == CM_TOPIC_SQUARE -> {
                Toasts.show(R.string.currently_not_supported)
            }
            decodeUrl == CM_TOPIC_SQUARE_TAB_ZERO -> {
                Toasts.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(COMMON_TITLE) -> {
                Toasts.show(R.string.currently_not_supported)
            }
            actionUrl == HP_NOTIFI_TAB_ZERO -> {

            }
            actionUrl.startsWith(TOPIC_DETAIL) -> {
                Toasts.show(R.string.currently_not_supported)
            }
            actionUrl.startsWith(DETAIL) -> {
//                getConversionVideoId(actionUrl)?.run { NewDetailActivity.start(activity, this) }
            }
            else -> {
                Toasts.show(R.string.currently_not_supported)
            }
        }
    }

    /**
     * 截取标题与url信息。
     *
     * @return first=标题 last=url
     */
    private fun String.getWebViewInfo(): Array<String> {
        val title = this.split("title=").last().split("&url").first()
        val url = this.split("url=").last()
        return arrayOf(title, url)
    }

    /**
     *  截取视频id。
     *
     *  @param actionUrl 解码后的actionUrl
     *  @return 视频id
     */
    private fun getConversionVideoId(actionUrl: String?): Long? {
        return try {
            val list = actionUrl?.split(DETAIL)
            list!![1].toLong()
        } catch (e: Exception) {
            null
        }
    }

}