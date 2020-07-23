package com.benyq.guochat.ui.common.stateful_loading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import java.util.*

/**
 * @author benyq
 * @time 2020/7/17
 * @e-mail 1520063035@qq.com
 * @note
 */
class LoadingHelper(contentView: View) {

    lateinit var mAdapter: Adapter<ViewHolder>
    private var mViewHolders: EnumMap<ViewType, ViewHolder> = EnumMap(ViewType::class.java)
    private var currentViewHolder: ViewHolder? = null
    private val contentViewHolder = ViewHolder(contentView)
    private var parent: ViewGroup? = null
    var onRetryAction: ((View)->Unit) ? = null


    init {
        mViewHolders[ViewType.CONTENT] = contentViewHolder
        parent = contentView.parent as ViewGroup
        showContent()
    }

    /**
     * 这个应该是原本页面应该显示的内容  contentView
     */
    fun showContent() = showView(ViewType.CONTENT)

    fun showLoading() = showView(ViewType.LOADING)

    fun showError() = showView(ViewType.ERROR)

    private fun showView(viewType: ViewType) {
        if (currentViewHolder == null) {
            addView(viewType)
            return
        }
        val holder = getViewHolder(viewType)
        if (holder != currentViewHolder) {
            addView(viewType)
        }
    }

    private fun addView(viewType: ViewType) {
        parent?.run {
            val viewHolder = getViewHolder(viewType)
            val rootView = viewHolder.rootView
            if (indexOfChild(rootView) != -1) {
                removeView(rootView)
            }
            currentViewHolder?.rootView?.let {
                if (indexOfChild(it) != -1) {
                    removeView(it)
                }
            }
            addView(rootView, 0)
            currentViewHolder = viewHolder
        }
    }


    private fun getViewHolder(viewType: ViewType): ViewHolder {
        return mViewHolders[viewType] ?: let {
            val vh = mAdapter.onCreateViewHolder(
                parent!!, viewType
            )
            vh.viewType = viewType
            mViewHolders[viewType] = vh
            mAdapter.onBindViewHolder(vh)
            if (viewType == ViewType.ERROR) {
                vh.onReloadListener = onRetryAction
            }
            vh
        }
    }
}

abstract class Adapter<VH : ViewHolder> {
    abstract fun onCreateViewHolder(parent: ViewGroup, type: ViewType): VH
    abstract fun onBindViewHolder(holder: VH)
}

open class ViewHolder(val rootView: View) {

    internal var viewType: ViewType? = null

    var onReloadListener: ((View)->Unit)? = null
        internal set

    fun <T : View> getView(@IdRes viewId: Int): T {
        val view = rootView.findViewById<T>(viewId)
        checkNotNull(view) { "No view found with id $viewId" }
        return view
    }

}

enum class ViewType {
    LOADING, CONTENT, ERROR, EMPTY
}