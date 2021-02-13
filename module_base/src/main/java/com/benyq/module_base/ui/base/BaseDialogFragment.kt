package com.benyq.module_base.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding

/**
 * @author benyq
 * @time 2020/4/27
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class BaseDialogFragment<VB: ViewBinding> : DialogFragment(){

    protected lateinit var mContext: Context
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        _binding = provideViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun initView()
    abstract fun provideViewBinding(): VB

    open fun show(fragmentManager: FragmentManager) {
        if (!isAdded) {
            show(fragmentManager, javaClass.simpleName)
        }
    }
}