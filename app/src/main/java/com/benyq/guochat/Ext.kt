package com.benyq.guochat

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.benyq.module_base.http.JSON
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.fromQ
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ext.setTextAppearanceCustomer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.tabs.TabLayout
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

fun ImageView.loadImage(
    url: String,
    round: Int = 10,
    isCircle: Boolean = false,
    placeHolder: Int = R.drawable.shape_album_loading_bg
) {
    Glide.with(context).load(url)
        .apply {
            if (isCircle) {
                transform(CircleCrop())
            } else if (round > 0) {
                transform(RoundedCorners(context.dip2px(round).toInt()))
                    .placeholder(placeHolder)
            }
        }
        .into(this)
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


fun TabLayout.setTextStyleSelectState(position: Int, @StyleRes style: Int) {
    val title =
        ((getChildAt(0) as LinearLayout).getChildAt(position) as LinearLayout).getChildAt(
            1
        ) as TextView
    title.setTextAppearanceCustomer(context, style)
}

fun ViewPager2.overScrollNever() {
    val child: View = getChildAt(0)
    (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}

/**
 * 占位隐藏view，带有渐隐动画效果。
 *
 * @param duration 毫秒，动画持续时长，默认500毫秒。
 */
fun View?.invisibleAlphaAnimation(duration: Long = 500L) {
    this?.visibility = View.INVISIBLE
    this?.startAnimation(AlphaAnimation(1f, 0f).apply {
        this.duration = duration
        fillAfter = true
    })
}

/**
 * 显示view，带有渐显动画效果。
 *
 * @param duration 毫秒，动画持续时长，默认500毫秒。
 */
fun View?.visibleAlphaAnimation(duration: Long = 500L) {
    this?.visibility = View.VISIBLE
    this?.startAnimation(AlphaAnimation(0f, 1f).apply {
        this.duration = duration
        fillAfter = true
    })
}

