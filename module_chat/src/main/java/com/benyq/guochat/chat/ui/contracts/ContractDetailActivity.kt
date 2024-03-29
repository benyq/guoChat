package com.benyq.guochat.chat.ui.contracts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.app.GENDER_FEMALE
import com.benyq.guochat.chat.app.GENDER_MALE
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.ActivityContractDetailBinding
import com.benyq.guochat.chat.loadAvatar
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.model.vm.ContractViewModel
import com.benyq.guochat.chat.ui.chats.ChatDetailActivity
import com.benyq.guochat.chat.ui.common.CommonBottomDialog
import com.benyq.module_base.SmartJump
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ui.base.BaseActivity
import com.benyq.module_base.ui.base.LifecycleActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/5/24
 * @e-mail 1520063035@qq.com
 * @note 联系人详情
 */
@AndroidEntryPoint
class ContractDetailActivity : LifecycleActivity<ContractViewModel, ActivityContractDetailBinding>() {

    override fun provideViewBinding() = ActivityContractDetailBinding.inflate(layoutInflater)
    override fun initVM(): ContractViewModel = getViewModel()

    private var mBottomDialog: CommonBottomDialog? = null
    private lateinit var mContractBean: ContractBean

    @SuppressLint("SetTextI18n")
    override fun initView() {
        //根据传过来的联系人信息初始化页面
        mContractBean = intent.getParcelableExtra(IntentExtra.contractData)!!
        binding.ivAvatar.loadAvatar(mContractBean.avatar)
        binding.tvNickName.text = mContractBean.nick
        binding.tvChatNo.text = "果聊号: ${mContractBean.chatNo}"
        if (mContractBean.gender == GENDER_FEMALE) {
            binding.ivGender.setImageResource(R.drawable.ic_gender_female)
        } else if (mContractBean.gender == GENDER_MALE) {
            binding.ivGender.setImageResource(R.drawable.ic_gender_male)
        }
    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }
        binding.headerView.setMenuAction {
            showBottomDialog()
        }

        binding.ifContractPermission.setOnClickListener {

        }
        binding.ifGuoChatCircle.setOnClickListener {

        }
        binding.llSendMessage.setOnClickListener {
            val conversation =
                ChatObjectBox.getConversationId(ChatLocalStorage.uid, mContractBean.contractId)
            goToActivity<ChatDetailActivity>(
                IntentExtra.conversationId to conversation.id
            )
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

    private fun editContractRemarks() {
        SmartJump.from(this@ContractDetailActivity).startForResult(
            Intent(
                this@ContractDetailActivity,
                ContractNickActivity::class.java
            ).apply {
                putExtra(IntentExtra.contractData, mContractBean)
            }, { code, data ->
                if (code == Activity.RESULT_OK && data != null) {

                }
            })
    }

    override fun dataObserver() {
    }

}
