package com.benyq.module_base.http

import android.text.TextUtils
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.IOException

/**
 * @author benyq
 * @time 2020/4/9
 * @e-mail 1520063035@qq.com
 * @note
 */
object ParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request.newBuilder()

        if (canInjectIntoBody(request)) {
            val formBodyBuilder = FormBody.Builder()
            formBodyBuilder.add("token", "token")
            formBodyBuilder.add("marketId", "uid")

            val formBody: RequestBody = formBodyBuilder.build()
            var postBodyString = bodyToString(request.body)
            postBodyString += (if (postBodyString.isNotEmpty()) "&" else "") + bodyToString(formBody)
            requestBuilder.post(postBodyString.toRequestBody(FormUrlEncoded))
            request = requestBuilder.build()
        } else {
            request = requestBuilder.url(request.url.newBuilder().run {
                addQueryParameter("token", "token")
                addQueryParameter("marketId", "marketId")
            }.build()).build()

        }

        return chain.proceed(request)
    }


    /**
     * 确认是否是 post 请求
     * @param request 发出的请求
     * @return true 需要注入公共参数
     */
    private fun canInjectIntoBody(request: Request?): Boolean {
        if (request == null) {
            return false
        }
        if (!TextUtils.equals(request.method, "POST")) {
            return false
        }
        val body = request.body ?: return false
        val mediaType = body.contentType() ?: return false
        return TextUtils.equals(mediaType.subtype, "x-www-form-urlencoded")
    }

    private fun bodyToString(request: RequestBody?): String {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
}