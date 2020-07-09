package com.benyq.guochat.ui.chats

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.local.ObjectBox
import com.benyq.guochat.model.vm.ChatViewModel
import com.benyq.guochat.ui.base.LifecycleFragment
import com.benyq.mvvm.annotation.BindViewModel
import com.benyq.mvvm.ext.startActivity
import com.scwang.smartrefresh.header.WaterDropHeader
import kotlinx.android.synthetic.main.fragment_chat.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author benyq
 * @time 2020/4/20
 * @e-mail 1520063035@qq.com
 * @note 聊天列表
 */
class ChatFragment : LifecycleFragment<ChatViewModel>() {

    private val mChatAdapter = ChatAdapter()

    override fun initVM(): ChatViewModel = getViewModel()

    override fun getLayoutId() = R.layout.fragment_chat

    override fun initView() {
        rvChats.layoutManager = LinearLayoutManager(mContext)
        rvChats.adapter = mChatAdapter
        mChatAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity<ChatDetailActivity>(IntentExtra.fromToId to mChatAdapter.data[position])
        }

        mChatAdapter.setOnItemLongClickListener { adapter, view, position ->

            true
        }
        refreshLayout.setRefreshHeader(WaterDropHeader(mContext))

    }

    override fun initListener() {
        refreshLayout.setOnRefreshListener {
            mChatAdapter.setNewInstance(ObjectBox.getChatContracts().toMutableList())
            refreshLayout.finishRefresh(2000)
        }
    }

    override fun initData() {
        mViewModel.getChatContracts()
    }

    override fun dataObserver() {
        with(mViewModel) {
            mChatListData.observe(viewLifecycleOwner, Observer {
                mChatAdapter.setNewInstance(it.toMutableList())
            })
        }
    }
}