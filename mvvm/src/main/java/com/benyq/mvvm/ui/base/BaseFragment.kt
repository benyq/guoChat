package com.benyq.mvvm.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note 所有fragment的基类
 */
abstract class BaseFragment : Fragment(), IFragment{

    lateinit var mContext : Context
    private lateinit var appViewModelProvider: ViewModelProvider

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBefore()
        initView()
        initListener()
    }

    open fun getAppViewModelProvider(): ViewModelProvider {
        if (!this::appViewModelProvider.isInitialized) {
            appViewModelProvider = ViewModelProvider(requireActivity().applicationContext as BaseApplication, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
        }
        return appViewModelProvider
    }

    open fun initData() {}
}