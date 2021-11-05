package com.benyq.guochat.chat.ui.login

import androidx.navigation.fragment.NavHostFragment
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.FragmentLoginBinding
import com.benyq.guochat.chat.model.vm.LoginViewModel
import com.benyq.guochat.chat.ui.MainActivity
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.textTrim
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
class LoginFragment : LifecycleFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun provideViewBinding() = FragmentLoginBinding.inflate(layoutInflater)
    override fun initVM(): LoginViewModel = getViewModel()

    override fun initListener() {
        super.initListener()
        binding.btnLogin.setOnClickListener {
            val username = binding.etUserName.textTrim()
            val password = binding.etPassword.textTrim()
            if (username.isEmpty()) {
                Toasts.show(R.string.username_empty)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toasts.show(R.string.password_empty)
                return@setOnClickListener
            }
            mViewModel.login(username, password)
        }

        binding.btnRegister.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_enter_register)
        }
    }

    override fun dataObserver() {
        with(mViewModel) {
            mLoginResult.observe(viewLifecycleOwner) {
                goToActivity<MainActivity>(exitAnim = R.anim.normal_out)
                requireActivity().finish()
            }
            loadingType.observe(viewLifecycleOwner) {
                if (it.isLoading) {
                    showLoading(it.isSuccess)
                } else {
                    hideLoading()
                }
            }
        }
    }

}