package com.benyq.guochat.ui.me

import androidx.core.widget.addTextChangedListener
import com.benyq.guochat.R
import com.benyq.guochat.textTrim
import com.benyq.guochat.ui.base.LifecycleActivity
import kotlinx.android.synthetic.main.activity_personal_info_edit.*

/**
 * @author benyq
 * @time 2020/5/22
 * @e-mail 1520063035@qq.com
 * @note  修改信息，目前只有昵称
 */
class PersonalInfoEditActivity : LifecycleActivity() {

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

        }

        etContent.addTextChangedListener {
            if (it?.toString() == oldValue) {
                headerView.setMenuBtnEnable(false)
            }else {
                headerView.setMenuBtnEnable(true)
            }
        }
    }
}
