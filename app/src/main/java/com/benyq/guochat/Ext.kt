package com.benyq.guochat

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.benyq.guochat.app.JSON
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.fromQ
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.*
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * @author benyq
 * @time 2020/4/20
 * @e-mail 1520063035@qq.com
 * @note
 */

fun <T> GlobalScope.asyncWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {

    val deferred = GlobalScope.async(context, start) {

        block()
    }

    lifecycleOwner.lifecycle.addObserver(LifecycleCoroutineListener(deferred))

    return deferred
}


infix fun <T> Deferred<T>.then(block: (T) -> Unit): Job {

    return GlobalScope.launch(context = Dispatchers.Main) {

        block(await())
    }
}

class LifecycleCoroutineListener(private val job: Job) : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (!job.isCancelled) {
            job.cancel()
        }
    }
}

/**
 * 将dip或dp值转换为px值，保证尺寸大小不变
 *
 * @param dipValue
 * （DisplayMetrics类中属性density）
 * @return
 */
fun dip2px(context: Context, dipValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f)
}

fun dip2px(context: Context, dipValue: Int): Float {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f)
}

inline fun <reified T> singleton(crossinline initializer: (Context) -> T): (Context) -> T {
    var instance: T? = null
    return { context ->
        instance ?: synchronized(T::class) {
            instance ?: initializer(context).also { instance = it }
        }
    }
}

class Singleton(context: Context) {
    companion object {
        val instance = singleton {
            Singleton(it)
        }
    }

    init {
    }

    fun test() = "foo"
}

//计算时间
fun calculateTime(time: Int): String {
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

fun loadAvatar(iv: ImageView, url: String, round: Int = 10) {
    Glide.with(iv.context).load(url)
        .apply {
            if (round > 0) {
                transform(RoundedCorners(dip2px(iv.context, round).toInt()))
            }
        }
        .into(iv)
}

fun <K, V> mapOfToBodyJson(vararg pairs: Pair<K, V>): RequestBody {
    return mapOf(*pairs).toString().toRequestBody(JSON)
}


fun saveImg(context: Context, file: File, targetPath: String, imgName: String): Boolean {
    return if (fromQ()) {
        saveImgVersionQ(context, file, targetPath, imgName)
    } else {
        saveImgLegacy(context, file, targetPath, imgName)
    }
}

private fun saveImgLegacy(
    context: Context,
    file: File,
    targetPath: String,
    imgName: String
): Boolean {
    val parentFile = File(targetPath)
    if (!parentFile.exists()) {
        parentFile.mkdirs()
    }
    if (!file.exists()) {
        return false
    }
    val source = file.source().buffer()
    val sink = File(targetPath + imgName).sink().buffer()
    sink.writeAll(source)
    sink.close()
    source.close()

    MediaScannerConnection.scanFile(
        context,
        arrayOf(targetPath + imgName),
        arrayOf("image/jpeg"),
        null
    )
    return true
}

@TargetApi(Build.VERSION_CODES.Q)
private fun saveImgVersionQ(
    context: Context,
    file: File,
    targetPath: String,
    imgName: String
): Boolean {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis().toString())
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }
    val saveUri = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        values
    ) ?: kotlin.run {
        Toasts.show("存储失败")
        return false
    }
    context.contentResolver.openOutputStream(saveUri)?.use {
        val sink = it.sink().buffer()
        val source = file.source().buffer()
        sink.writeAll(source)
        sink.close()
        source.close()
        Toasts.show("存储成功")
    }
    values.put(MediaStore.Video.Media.IS_PENDING, 0)
    context.contentResolver.update(saveUri, values, null, null)
    return true
}