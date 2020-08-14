package com.benyq.guochat.ui.me

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.getViewModel
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.model.vm.PersonalInfoViewModel
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.mvvm.annotation.BindViewModel
import com.benyq.mvvm.ext.textTrim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_personal_info_edit.*

/**
 * @author benyq
 * @time 2020/5/22
 * @e-mail 1520063035@qq.com
 * @note  修改信息，目前只有昵称
 */
@AndroidEntryPoint
class PersonalInfoEditActivity : LifecycleActivity<PersonalInfoViewModel>() {

    override fun initVM(): PersonalInfoViewModel = getViewModel()

    private var oldValue = "更改名字"

    override fun getLayoutId() = R.layout.activity_personal_info_edit

    override fun initView() {
        headerView.setToolbarTitle("更改名字")
        headerView.setMenuBtnEnable(false)
        etContent.setText(oldValue)
    }

    override fun initListener() {
        headerView.setBackAction { finish() }
        headerView.setMenuBtnAction {
            val value = etContent.textTrim()
            viewModelGet().editUserNick(value)
        }

        etContent.addTextChangedListener {
            if (it?.toString() == oldValue) {
                headerView.setMenuBtnEnable(false)
            }else {
                headerView.setMenuBtnEnable(true)
            }
        }
    }

    override fun dataObserver() {
        viewModelGet().editNickLiveData.observe(this, Observer {
            LocalStorage.updateUserAccount {
                nickName = it
            }
            finish()
        })
    }
}
