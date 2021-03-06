package com.benyq.guochat.chat.ui.me

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.benyq.guochat.chat.databinding.ActivityPersonalInfoEditBinding
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.vm.PersonalInfoViewModel
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.textTrim
import com.benyq.module_base.ui.base.LifecycleActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/5/22
 * @e-mail 1520063035@qq.com
 * @note  修改信息，目前只有昵称
 */
@AndroidEntryPoint
class PersonalInfoEditActivity : LifecycleActivity<PersonalInfoViewModel, ActivityPersonalInfoEditBinding>() {

    override fun initVM(): PersonalInfoViewModel = getViewModel()

    private var oldValue = "更改名字"

    override fun provideViewBinding() = ActivityPersonalInfoEditBinding.inflate(layoutInflater)

    override fun initView() {
        binding.headerView.setToolbarTitle("更改名字")
        binding.headerView.setMenuBtnEnable(false)
        binding.etContent.setText(oldValue)
    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }
        binding.headerView.setMenuBtnAction {
            val value = binding.etContent.textTrim()
            viewModelGet().editUserNick(value)
        }

        binding.etContent.addTextChangedListener {
            if (it?.toString() == oldValue) {
                binding.headerView.setMenuBtnEnable(false)
            } else {
                binding.headerView.setMenuBtnEnable(true)
            }
        }
    }

    override fun dataObserver() {
        viewModelGet().editNickLiveData.observe(this, Observer {
            ChatLocalStorage.updateUserAccount {
                nickName = it
            }
            finish()
        })
    }
}
