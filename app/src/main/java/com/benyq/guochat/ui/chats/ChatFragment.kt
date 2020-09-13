package com.benyq.guochat.ui.chats

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.app.SharedViewModel
import com.benyq.guochat.getViewModel
import com.benyq.guochat.model.bean.ChatListBean
import com.benyq.guochat.model.vm.ChatViewModel
import com.benyq.guochat.ui.base.LifecycleFragment
import com.benyq.guochat.ui.common.openeye.WaterDropHeader
import com.benyq.mvvm.ext.goToActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * @author benyq
 * @time 2020/4/20
 * @e-mail 1520063035@qq.com
 * @note 聊天列表
 */
@AndroidEntryPoint
class ChatFragment : LifecycleFragment<ChatViewModel>() {

    private val mChatAdapter = ChatAdapter()

    override fun initVM(): ChatViewModel = getViewModel()

    override fun getLayoutId() = R.layout.fragment_chat

    override fun initView() {
        rvChats.layoutManager = LinearLayoutManager(mContext)
        rvChats.adapter = mChatAdapter
        mChatAdapter.setDiffCallback(object : DiffUtil.ItemCallback<ChatListBean>() {
            override fun areItemsTheSame(oldItem: ChatListBean, newItem: ChatListBean): Boolean {
                return oldItem.fromToId == newItem.fromToId
            }

            override fun areContentsTheSame(oldItem: ChatListBean, newItem: ChatListBean): Boolean {
                return oldItem.latestTime == newItem.latestTime
            }

        })
        mChatAdapter.setOnItemClickListener { adapter, view, position ->
            goToActivity<ChatDetailActivity>(IntentExtra.fromToId to mChatAdapter.data[position])
        }

        mChatAdapter.setOnItemLongClickListener { adapter, view, position ->
            mChatAdapter.removeAt(position)
            true
        }
        refreshLayout.setRefreshHeader(WaterDropHeader(mContext))

        getAppViewModelProvider().get(SharedViewModel::class.java).chatChange.observe(
            viewLifecycleOwner,
            Observer {
                initData()
            })
    }

    override fun initListener() {
        refreshLayout.setOnRefreshListener {
            initData()
            refreshLayout.finishRefresh(2000)
        }
    }

    override fun initData() {
        mViewModel.getChatContracts()
    }

    override fun dataObserver() {
        with(mViewModel) {
            mChatListData.observe(viewLifecycleOwner, Observer {
                mChatAdapter.setDiffNewData(it.toMutableList())
            })
        }
    }
}