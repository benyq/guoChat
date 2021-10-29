package com.benyq.guochat.chat.ui.login

import android.app.Activity
import android.content.Intent
import androidx.navigation.fragment.NavHostFragment
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.FragmentRegisterBinding
import com.benyq.guochat.chat.model.bean.RegisterBean
import com.benyq.guochat.chat.model.vm.LoginViewModel
import com.benyq.guochat.chat.ui.MainActivity
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ext.textTrim
import com.benyq.module_base.ui.base.BaseFragment
import com.benyq.module_base.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @author benyq
 * @date 2021/10/26
 * @email 1520063035@qq.com
 *
 */
@AndroidEntryPoint
class RegisterFragment : LifecycleFragment<LoginViewModel, FragmentRegisterBinding>() {

    override fun provideViewBinding() = FragmentRegisterBinding.inflate(layoutInflater)
    override fun initVM(): LoginViewModel = getViewModel()


    override fun initView() {
    }

    override fun initListener() {
        binding.headerView.run {
            setBackAction {
                NavHostFragment.findNavController(this@RegisterFragment).navigateUp()
            }
        }

        binding.btnRegister.setOnClickListener {
            val userName = binding.etUserName.textTrim()
            val pwd = binding.etPassword.textTrim()
            mViewModel.register(userName, pwd)
        }
    }

    override fun dataObserver() {
        mViewModel.mRegisterResult.observe(viewLifecycleOwner){
            //注册成功直接跳转到MainActivity
            goToActivity<MainActivity>(exitAnim = R.anim.normal_out)
            requireActivity().finish()
        }
        mViewModel.loadingType.observe(viewLifecycleOwner){
            loge("loadingType: it")
            if (it.isLoading) {
                showLoading(it.isSuccess)
            } else {
                hideLoading()
            }
        }
    }
}