package com.benyq.guochat.chat.ui.chats

import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.app.SharedViewModel
import com.benyq.guochat.chat.databinding.FragmentChatBinding
import com.benyq.guochat.chat.model.bean.ChatListBean
import com.benyq.guochat.chat.model.vm.ChatViewModel
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.base.LifecycleFragment
import com.benyq.module_base.ui.waterdrop.WaterDropHeader
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/4/20
 * @e-mail 1520063035@qq.com
 * @note 聊天列表
 */
@AndroidEntryPoint
class ChatFragment : LifecycleFragment<ChatViewModel, FragmentChatBinding>() {

    private val mChatAdapter = ChatAdapter()
    private val mAppVideModel: SharedViewModel by lazy {
        getAppViewModelProvider().get(SharedViewModel::class.java)
    }
    override fun initVM(): ChatViewModel = getViewModel()

    override fun provideViewBinding() = FragmentChatBinding.inflate(layoutInflater)

    override fun initView() {
        binding.rvChats.layoutManager = LinearLayoutManager(mContext)
        binding.rvChats.adapter = mChatAdapter
        (binding.rvChats.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mChatAdapter.setDiffCallback(object : DiffUtil.ItemCallback<ChatListBean>() {
            override fun areItemsTheSame(oldItem: ChatListBean, newItem: ChatListBean): Boolean {
                return oldItem.conversationId == newItem.conversationId
            }

            override fun areContentsTheSame(oldItem: ChatListBean, newItem: ChatListBean): Boolean {
                return oldItem.latestTime == newItem.latestTime && oldItem.unreadRecord == newItem.unreadRecord
            }

        })
        mChatAdapter.setOnItemClickListener { adapter, view, position ->
            goToActivity<ChatDetailActivity>(IntentExtra.conversationId to mChatAdapter.data[position].conversationId)
        }

        mChatAdapter.setOnItemLongClickListener { adapter, view, position ->
            mChatAdapter.removeAt(position)
            true
        }
        binding.refreshLayout.setRefreshHeader(WaterDropHeader(mContext))
    }

    override fun initListener() {
        binding.refreshLayout.setOnRefreshListener {
            initData()
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun initData() {
        mViewModel.getChatContracts()
    }

    override fun dataObserver() {
        with(mViewModel) {
            mChatListData.observe(viewLifecycleOwner) {
                mChatAdapter.setDiffNewData(it.toMutableList())
                binding.refreshLayout.finishRefresh()
            }
        }
        mAppVideModel.chatChange.observe(viewLifecycleOwner) {
            initData()
        }
    }
}