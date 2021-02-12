package com.benyq.module_base.glide

import android.os.Handler
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * @author benyq
 * @time 2020/7/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class ProgressResponseBody(
    url: String,
    private val handler: Handler,
    private var responseBody: ResponseBody
) : ResponseBody(){

    private var listener: ProgressListener? = ProgressInterceptor.map[url]

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun source(): BufferedSource {
        return ProgressSource(responseBody.source()).buffer()
    }

    inner class ProgressSource(source: Source) : ForwardingSource(source) {

        private var totalBytesRead: Long = 0
        private var currentProgress = 0

        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength = responseBody.contentLength()
            if (bytesRead == -1L) {
                totalBytesRead = fullLength;
            } else {
                totalBytesRead += bytesRead;
            }
            val progress = (100f * totalBytesRead / fullLength).toInt()
            if (progress != currentProgress) {
                handler.post {
                    listener?.onProgress(progress)
                }
            }
            currentProgress = progress;
            return bytesRead
        }
    }
}