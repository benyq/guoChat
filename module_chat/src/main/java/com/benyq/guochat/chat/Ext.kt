package com.benyq.guochat.chat

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.benyq.module_base.http.JSON
import com.benyq.module_base.ext.fromQ
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

    val contentUri = Uri.fromFile(File(targetPath + imgName))
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri)
    context.sendBroadcast(mediaScanIntent)

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
    ) ?: run {
        return false
    }
    val result = runCatching {
        context.contentResolver.openOutputStream(saveUri)?.use {
            val sink = it.sink().buffer()
            val source = file.source().buffer()
            sink.writeAll(source)
            sink.close()
            source.close()
        }
        values.put(MediaStore.Video.Media.IS_PENDING, 0)
        context.contentResolver.update(saveUri, values, null, null)
    }
    return result.isSuccess
}

