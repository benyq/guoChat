package com.benyq.module_base.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.NormalProgressDialogManager

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note 所有fragment的基类
 */
abstract class BaseFragment<VB: ViewBinding> : Fragment(), IFragment{

    lateinit var mContext : Context
    private lateinit var appViewModelProvider: ViewModelProvider

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = provideViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBefore()
        initView()
        initListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun getAppViewModelProvider(): ViewModelProvider {
        if (!this::appViewModelProvider.isInitialized) {
            appViewModelProvider = ViewModelProvider(requireActivity().applicationContext as BaseApplication, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
        }
        return appViewModelProvider
    }

    protected fun showLoading(msg: String?) {
        loge("showLoading $msg")
        NormalProgressDialogManager.showLoading(requireActivity(), msg)
    }

    protected fun hideLoading() {
        NormalProgressDialogManager.dismissLoading()
    }

    open fun initData() {}

    abstract fun provideViewBinding(): VB
}