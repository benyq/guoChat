package com.benyq.mvvm.ui.base

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * @author benyq
 * @time 2020/4/27
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class BaseDialogFragment : DialogFragment(){

    protected lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawableResource(R.color.transparent)
        }
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    open fun show(fragmentManager: FragmentManager) {
        if (!isAdded) {
//            val ft: FragmentTransaction = fragmentManager.beginTransaction()
//            ft.add(this, javaClass.simpleName)
//            ft.commitAllowingStateLoss()
            show(fragmentManager, javaClass.simpleName)
        }
    }
}