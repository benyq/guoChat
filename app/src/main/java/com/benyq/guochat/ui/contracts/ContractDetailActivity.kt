package com.benyq.guochat.ui.contracts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.benyq.guochat.R
import com.benyq.guochat.app.GENDER_FEMALE
import com.benyq.guochat.app.GENDER_MALE
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.loadImage
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.guochat.local.ChatObjectBox
import com.benyq.guochat.local.entity.ContractEntity
import com.benyq.mvvm.ui.base.BaseActivity
import com.benyq.guochat.ui.chats.ChatDetailActivity
import com.benyq.guochat.ui.common.CommonBottomDialog
import com.benyq.mvvm.SmartJump
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.goToActivity
import kotlinx.android.synthetic.main.activity_contract_detail.*

/**
 * @author benyq
 * @time 2020/5/24
 * @e-mail 1520063035@qq.com
 * @note 联系人详情
 */
class ContractDetailActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_contract_detail

    private var mBottomDialog: CommonBottomDialog? = null
    private lateinit var mContractEntity: ContractEntity

    @SuppressLint("SetTextI18n")
    override fun initView() {
        //根据传过来的联系人信息初始化页面
        mContractEntity = intent.getParcelableExtra(IntentExtra.contractData)!!
        ivAvatar.loadImage(mContractEntity.avatarUrl)
        tvNickName.text = mContractEntity.nick
        tvChatNo.text = "果聊号: ${mContractEntity.contractId}"
        if (mContractEntity.gender == GENDER_FEMALE) {
            ivGender.setImageResource(R.drawable.ic_gender_female)
        }else if (mContractEntity.gender == GENDER_MALE) {
            ivGender.setImageResource(R.drawable.ic_gender_male)
        }
    }

    override fun initListener() {
        headerView.setBackAction { finish() }
        headerView.setMenuAction {
            showBottomDialog()
        }

        ifContractPermission.setOnClickListener {

        }
        ifGuoChatCircle.setOnClickListener {

        }
        llSendMessage.setOnClickListener {
            val user = ChatLocalStorage.userAccount
            val bean = ChatObjectBox.findFromToByIds(user.chatId, mContractEntity.id)
            goToActivity<ChatDetailActivity>(IntentExtra.fromToId to bean)
        }
    }

    private fun showBottomDialog() {
        mBottomDialog =
            mBottomDialog ?: CommonBottomDialog.newInstance(arrayOf("设置备注和标签", "朋友权限", "删除"))
                .apply {
                    setOnMenuAction { _, index ->
                        when (index) {
                            0 -> {
                                editContractRemarks()
                            }
                            1 -> {
                                Toasts.show("朋友权限")
                            }
                            2 -> {
                                Toasts.show("删除")
                            }
                        }
                    }
                }
        mBottomDialog?.show(supportFragmentManager)
    }

    private fun editContractRemarks(){
        SmartJump.from(this@ContractDetailActivity).startForResult(Intent(this@ContractDetailActivity, ContractNickActivity::class.java).apply {
            putExtra(IntentExtra.contractData, mContractEntity)
        }, { code, data->
            if (code == Activity.RESULT_OK && data != null) {

            }
        })
    }

}
