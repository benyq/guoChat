package com.benyq.guochat.chat.ui.contracts

import androidx.navigation.fragment.NavHostFragment
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.FragmentAddContractBinding
import com.benyq.guochat.chat.function.zxing.android.CaptureActivity
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ui.base.BaseFragment

/**
 *
 * @author benyq
 * @date 2021/10/28
 * @email 1520063035@qq.com
 *
 */
class AddContractFragment : BaseFragment<FragmentAddContractBinding>(){
    override fun provideViewBinding(): FragmentAddContractBinding = FragmentAddContractBinding.inflate(layoutInflater)


    override fun initListener() {
        binding.llQuery.setOnClickListener {
            //这个可能用到Navigation 当前界面 -> 搜索界面 -> 其他用户界面
            NavHostFragment.findNavController(this).navigate(R.id.action_search_contract)
        }
        binding.llChatIdCode.setOnClickListener {
            //跳转到二维码名片界面
            goToActivity<CallingCardActivity>()
        }
        binding.ilScanCode.setOnClickListener {
            goToActivity<CaptureActivity>()
        }

        binding.headerView.setBackAction {
            requireActivity().finish()
        }
    }

    override fun initView() {
        //从本地缓存中获取User信息
        val userChatId = ChatLocalStorage.userAccount.chatNo
        val contentChatId = "${getString(R.string.my_chat_id)} $userChatId"
        binding.tvGuoChatId.text = contentChatId
    }

}