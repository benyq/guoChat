package com.benyq.guochat

import android.app.Activity
import android.content.Context
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
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

fun EditText.textTrim(): String {
    return this.text.toString().trim()
}

inline fun <reified T> singleton(crossinline initializer: (Context)->T) : (Context)->T {
    var instance : T? = null
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


