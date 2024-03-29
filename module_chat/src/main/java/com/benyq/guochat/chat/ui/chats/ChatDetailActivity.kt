package com.benyq.guochat.chat.ui.chats

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.app.SharedViewModel
import com.benyq.guochat.chat.databinding.ActivityChatDetailBinding
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.model.vm.ChatDetailViewModel
import com.benyq.guochat.chat.model.vm.StateEvent
import com.benyq.guochat.chat.ui.chats.video.PictureVideoActivity
import com.benyq.guochat.database.entity.chat.ChatRecordEntity
import com.benyq.guochat.media.voice.VoiceRecordController
import com.benyq.imageviewer.ImagePreview
import com.benyq.imageviewer.PreviewPhoto
import com.benyq.imageviewer.PreviewTypeEnum
import com.benyq.module_base.SmartJump
import com.benyq.module_base.ext.*
import com.benyq.module_base.glide.GlideEngine
import com.benyq.module_base.ui.base.LifecycleActivity
import com.benyq.module_base.ui.waterdrop.WaterDropHeader
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates


/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note 与联系人的聊天界面
 */
@AndroidEntryPoint
class ChatDetailActivity : LifecycleActivity<ChatDetailViewModel, ActivityChatDetailBinding>(),
    View.OnClickListener {

    private val TYPE_TEXT = 0
    private val TYPE_VOICE = 1

    private lateinit var mAdapter: ChatRecordAdapter
    private val mEmojiAdapter by lazy { EmojiAdapter() }
    private val mVoiceRecordDialog by lazy {
        VoiceRecordDialog().apply {
            setConfirmAction {
                val voiceBean = VoiceRecordController.stopVideoRecord()
                //发送
                voiceBean?.run {
                    val chatBean = ChatRecordEntity(
                        filePath = voicePath,
                        duration = voiceDuration,
                        fromUid = ChatLocalStorage.uid,
                        toUid = mContractBean.contractId,
                        chatType = ChatRecordEntity.TYPE_VOICE
                    )
                    addChatData(chatBean)
                }
            }
            setCancelAction {
                VoiceRecordController.stopVideoRecord(true)
            }
        }
    }

    private val mAppVideModel: SharedViewModel by lazy {
        getAppViewModelProvider().get(SharedViewModel::class.java).apply {
            conversationId = mConversationId
        }
    }

    private var mInputType by Delegates.observable(TYPE_TEXT, { prop, old, new ->
        if (new == TYPE_VOICE) {

            XXPermissions.with(this)
                .permission(Manifest.permission.RECORD_AUDIO)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            binding.ivTextVoice.setImageResource(R.drawable.ic_voice)
                            binding.etContent.gone()
                            binding.tvPressVoice.visible()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        Toasts.show("权限不足")
                    }
                })
        } else if (new == TYPE_TEXT) {
            binding.ivTextVoice.setImageResource(R.drawable.ic_keyboard)
            binding.etContent.visible()
            binding.etContent.requestFocus()
            binding.etContent.textAndSelection(binding.etContent.textTrim())
            binding.tvPressVoice.gone()
        }
    })

    private var mConversationId: Long = 0

    private lateinit var mContractBean: ContractBean

    override fun initVM(): ChatDetailViewModel = getViewModel()

    override fun provideViewBinding() = ActivityChatDetailBinding.inflate(layoutInflater)

    override fun initView() {
        mConversationId = intent.getLongExtra(IntentExtra.conversationId, 0)
        val tempContractBean = ChatObjectBox.getContractByConversation(mConversationId)

        if (mConversationId == 0L || tempContractBean == null) {
            Toasts.show("聊天信息出错!!!!")
            onBackPressed()
            return
        }
        mContractBean = tempContractBean
        mAdapter = ChatRecordAdapter(ChatLocalStorage.userAccount, mContractBean)
        binding.rootLayout.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (oldBottom != -1 && oldBottom > bottom) {
                if (mAdapter.data.isNotEmpty()) {
                    binding.rvChatRecord.scrollToPosition(mAdapter.data.size - 1)
                }
            }
        }
        binding.rvChatRecord.layoutManager = LinearLayoutManager(this)
        binding.rvChatRecord.adapter = mAdapter
        binding.rvChatRecord.itemAnimator?.run {
            addDuration = 0
            changeDuration = 0
            moveDuration = 0
            removeDuration = 0
        }

        binding.rvEmoji.apply {
            layoutManager = GridLayoutManager(this@ChatDetailActivity, 8)
            adapter = mEmojiAdapter
            mEmojiAdapter.setOnItemClickListener { _, _, position ->
                binding.etContent.append(mEmojiAdapter.data[position])
            }
        }

        binding.headerView.setBackAction {
            onBackPressed()
        }
        binding.headerView.setMenuAction {

        }
        binding.headerView.setToolbarTitle(mContractBean.nick)

        binding.refreshLayout.setRefreshHeader(WaterDropHeader(this))

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mAppVideModel.reset()
                finish()
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent == null) {
            Toasts.show("聊天信息出错!!!!")
            onBackPressed()
            return
        }
        mConversationId = intent.getLongExtra(IntentExtra.conversationId, 0)
        mAppVideModel.conversationId = mConversationId
        val tempContractBean = ChatObjectBox.getContractByConversation(mConversationId)

        if (mConversationId == 0L || tempContractBean == null) {
            Toasts.show("聊天信息出错!!!!")
            onBackPressed()
            return
        }
        mContractBean = tempContractBean
        mAdapter.setNewInstance(null)
        mAdapter.setContract(mContractBean)
        binding.headerView.setToolbarTitle(mContractBean.nick)
        initData()
    }

    override fun initImmersionBar() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.darkgrey)
            statusBarDarkFont(
                true,
                0.2f
            ) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
            keyboardEnable(true)
        }
    }

    override fun isSupportSwipeBack() = true

    override fun initListener() {
        VoiceRecordController.setCompleteAction {
            mAdapter.setVoiceStop(it)
        }
        binding.etContent.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                hideFunctionMenu(10)
            }
        }
        binding.etContent.addTextChangedListener {
            binding.tvSend.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        binding.ivTextVoice.setOnClickListener(this)
        binding.rootLayout.setOnClickListener(this)
        binding.etContent.setOnClickListener(this)
        binding.tvSend.setOnClickListener(this)
        binding.ivMoreFunction.setOnClickListener(this)
        binding.ivEmoji.setOnClickListener(this)

        binding.llAlbum.setOnClickListener(this)
        binding.llCapture.setOnClickListener(this)
        binding.llContracts.setOnClickListener(this)

        binding.refreshLayout.setOnRefreshListener {
            viewModelGet().requestMoreRecord(mConversationId, mAdapter.data.first().sendTime)
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val data = mAdapter.data[position]
            if (data.chatType == ChatRecordEntity.TYPE_VOICE) {
                mAdapter.setVoiceStop()
                VoiceRecordController.playVideoRecord(this, data.filePath, data.id)
                mAdapter.setVoicePlay(data)
            }
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.ivContent, R.id.flVideo -> {
                    //妈的，也挺麻烦的
                    var currentData: PreviewPhoto? = null

                    val tmpData = mutableListOf<PreviewPhoto>()
                    val tmpView = mutableListOf<View?>()
                    val visiblePosition =
                        (binding.rvChatRecord.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    mAdapter.data.forEachIndexed { index, entity ->
                        val realIndex = index - visiblePosition
                        if (entity.chatType == ChatRecordEntity.TYPE_IMG) {
                            tmpView.add(
                                binding.rvChatRecord.getChildAt(realIndex)
                                    ?.findViewById(R.id.ivContent)
                            )
                            tmpData.add(PreviewPhoto(entity.filePath, PreviewTypeEnum.IMAGE))
                        }
                        if (entity.chatType == ChatRecordEntity.TYPE_VIDEO) {
                            tmpView.add(
                                binding.rvChatRecord.getChildAt(realIndex)
                                    ?.findViewById(R.id.ivVideo)
                            )
                            tmpData.add(PreviewPhoto(entity.filePath, PreviewTypeEnum.VIDEO))
                        }
                        if (index == position) {
                            currentData = tmpData.last()
                        }
                    }
                    ImagePreview.setCacheView(tmpView.toList())
                        .setData(tmpData)
                        .setCurPosition(tmpData.indexOf(currentData))
                        .show(this)
                }
            }
        }
        binding.tvPressVoice.setOnLongClickListener {
            val location = Rect()
            it.getGlobalVisibleRect(location)
            val y = (location.top + location.bottom) / 2
            mVoiceRecordDialog.setTouchY(y)
            mVoiceRecordDialog.show(supportFragmentManager)
            VoiceRecordController.startVoiceRecord(this)
            true
        }
    }

    override fun initData() {
        viewModelGet().getChatRecord(mConversationId)
    }

    override fun dataObserver() {
        with(viewModelGet()) {
            mChatRecordData.observe(this@ChatDetailActivity){
                binding.rvChatRecord.alpha = 0f
                //根据item总高度显示区分 stackFromEnd 会出现问题，所以目前先靠估算吧
                val layoutManager = binding.rvChatRecord.layoutManager as LinearLayoutManager
                mAdapter.setNewInstance(it.toMutableList())
                binding.rvChatRecord.postDelayed(50) {
                    layoutManager.stackFromEnd = isFullScreen(layoutManager)
                    binding.rvChatRecord.alpha = 1f
                }
            }
            mSendMessageData.observe(this@ChatDetailActivity) {

            }
            mChatLatestRecordData.observe(this@ChatDetailActivity) {
                mAdapter.addData(it)
                binding.rvChatRecord.scrollToPosition(mAdapter.data.size - 1)
            }
            mChatMoreRecordData.observe(this@ChatDetailActivity) {
                mAdapter.addData(0, it)
                if (it.isEmpty()) {
                    binding.refreshLayout.setEnableRefresh(false)
                }
            }
            loadingType.observe(this@ChatDetailActivity) {
                if (!it.isLoading) {
                    binding.refreshLayout.finishRefresh()
                }
            }
        }
        mAppVideModel.chatChange.observe(this) {
            if (it == mConversationId && mAdapter.data.isNotEmpty()) {
                viewModelGet().requestLatestRecord(mConversationId, mAdapter.data.last().sendTime)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (mVoiceRecordDialog.dialog?.isShowing == true) {
            mVoiceRecordDialog.dispatchTouchEvent(ev!!)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun showFunctionMenu(duration: Long = 200) {
        binding.llBottom.animate().translationY(-binding.flBottom.height.toFloat())
            .setDuration(duration).start()
        binding.flBottom.animate().withStartAction {
            binding.flBottom.visible()
        }.translationY(-binding.flBottom.height.toFloat()).setDuration(duration).start()
    }

    private fun hideFunctionMenu(duration: Long = 200) {
        binding.llBottom.animate().translationY(0f).setDuration(duration).start()

        binding.flBottom.animate().withEndAction {
            binding.flBottom.gone()
        }.translationY(0f).setDuration(duration).start()

    }

    private fun showChatEmoji(duration: Long = 200) {
        val emojiHeight = dip2px(200)
        binding.llBottom.animate().translationY(-emojiHeight)
            .setDuration(duration).start()
        binding.rvEmoji.animate().withStartAction {
            binding.rvEmoji.visible()
        }.translationY(-emojiHeight).setDuration(duration).start()
    }

    private fun hideChatEmoji(duration: Long = 200L) {
        binding.llBottom.animate().translationY(0f).setDuration(duration).start()

        binding.rvEmoji.animate().withEndAction {
            binding.rvEmoji.gone()
        }.translationY(0f).setDuration(duration).start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.etContent -> hideFunctionMenu(10)
            R.id.ivTextVoice -> {
                mInputType = (mInputType + 1) % 2
            }
            R.id.tvSend -> {
                hideFunctionMenu()
                val content = binding.etContent.textTrim()
                addChatData(
                    ChatRecordEntity(
                        content = content,
                        fromUid = ChatLocalStorage.uid,
                        toUid = mContractBean.contractId
                    )
                )
                binding.etContent.setText("")
            }
            R.id.ivMoreFunction -> {
                showFunctionMenu()
            }
            R.id.ivEmoji -> {
                mInputType = TYPE_TEXT
                showChatEmoji()
            }
            R.id.llAlbum -> {
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofAll())
                    .imageEngine(GlideEngine)
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: List<LocalMedia>) {
                            hideFunctionMenu()
                            val res = result[0]
                            addChatData(
                                ChatRecordEntity(
                                    fromUid = ChatLocalStorage.uid,
                                    toUid = mContractBean.contractId,
                                    chatType = ChatRecordEntity.TYPE_IMG,
                                    filePath = res.realPath
                                )
                            )
                        }

                        override fun onCancel() {}
                    })
                hideFunctionMenu()
            }
            R.id.llCapture -> {
                XXPermissions.with(this)
                    .permission(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO
                        )
                    )
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                            if (all) {
                                gotoCameraCapture()
                            } else {
                                Toasts.show("请授予相应权限")
                            }
                        }

                        override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                            Toasts.show("请授予相应权限")
                        }
                    })
                hideFunctionMenu()
            }
            R.id.llContracts -> {

            }
        }
    }

    //v 永远都是EditText
    override fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        //两种情况 etContent 和 tvSend 不隐藏
        val et = checkView(binding.etContent, event)
        val tv = checkView(binding.tvSend, event) && binding.tvSend.visibility == View.VISIBLE
        return !et && !tv
    }

    override fun hideView(ev: MotionEvent) {
        loge("currentFocus $currentFocus")
        //两种情况 etContent 和 tvSend 不隐藏
        val iv = checkView(binding.ivMoreFunction, ev)
        val fl = checkView(binding.flBottom, ev)
        if (!iv && !fl && binding.flBottom.isVisible) {
            hideFunctionMenu()
        }

        val ivEmoji = checkView(binding.ivEmoji, ev)
        val rvEmoji = checkView(binding.rvEmoji, ev)
        if (!ivEmoji && !rvEmoji && binding.rvEmoji.isVisible) {
            hideChatEmoji()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        VoiceRecordController.reset()
    }

    private fun checkView(v: View, event: MotionEvent): Boolean {
        val leftTop = intArrayOf(0, 0)
        //获取输入框当前的location位置
        v.getLocationInWindow(leftTop)
        val left = leftTop[0]
        val top = leftTop[1]
        val bottom = top + v.height
        val right = left + v.width
        return (event.x > left && event.x < right
                && event.y > top && event.y < bottom)
    }

    private fun isFullScreen(llm: LinearLayoutManager): Boolean {
        if (mAdapter.itemCount == 0) {
            return false
        }
        return (llm.findLastCompletelyVisibleItemPosition() + 1) != mAdapter.itemCount ||
                llm.findFirstCompletelyVisibleItemPosition() != 0
    }

    private fun addChatData(data: ChatRecordEntity) {
        data.conversationId = mConversationId
        mAdapter.addData(data)
        binding.rvChatRecord.smoothScrollToPosition(mAdapter.data.size - 1)
        runOnUiThread(200) {
            val mLayoutManager = binding.rvChatRecord.layoutManager as LinearLayoutManager
            mLayoutManager.scrollToPositionWithOffset(mAdapter.data.size - 1, 0)
        }
        viewModelGet().sendChatMessage(data)
    }

    private fun gotoCameraCapture() {
        SmartJump.from(this).startForResult(PictureVideoActivity::class.java, { resultCode, data ->
            hideFunctionMenu()
            if (resultCode == Activity.RESULT_OK && data != null) {
                val state = data.getIntExtra(IntentExtra.stateEvent, StateEvent.STATE_FINISH_IMG)
                if (state == StateEvent.STATE_FINISH_VIDEO) {
                    addChatData(
                        ChatRecordEntity(
                            fromUid = ChatLocalStorage.uid,
                            toUid = mContractBean.contractId,
                            chatType = ChatRecordEntity.TYPE_VIDEO,
                            filePath = data.getStringExtra(IntentExtra.videoImgPath)!!,
                            duration = data.getIntExtra(IntentExtra.videoDuration, 0).toLong()
                        )
                    )
                } else {
                    addChatData(
                        ChatRecordEntity(
                            fromUid = ChatLocalStorage.uid,
                            toUid = mContractBean.contractId,
                            chatType = ChatRecordEntity.TYPE_IMG,
                            filePath = data.getStringExtra(IntentExtra.videoImgPath)!!
                        )
                    )
                }
            }
        })
    }

}
