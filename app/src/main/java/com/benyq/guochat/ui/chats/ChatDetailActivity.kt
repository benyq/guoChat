package com.benyq.guochat.ui.chats

import android.Manifest
import android.app.Activity
import android.graphics.Rect
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.core.view.postDelayed
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.app.SharedViewModel
import com.benyq.guochat.function.media.MediaRecordController
import com.benyq.guochat.function.other.GlideEngine
import com.benyq.guochat.function.permissionX.PermissionX
import com.benyq.guochat.local.entity.ChatRecordEntity
import com.benyq.guochat.model.bean.ChatListBean
import com.benyq.guochat.model.vm.ChatDetailViewModel
import com.benyq.guochat.model.vm.StateEvent
import com.benyq.mvvm.ui.base.LifecycleActivity
import com.benyq.guochat.ui.chats.video.PictureVideoActivity
import com.benyq.mvvm.SmartJump
import com.benyq.mvvm.ext.*
import com.gyf.immersionbar.ktx.immersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlin.properties.Delegates


/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note 与联系人的聊天界面
 */
@AndroidEntryPoint
class ChatDetailActivity : LifecycleActivity<ChatDetailViewModel>(), View.OnClickListener {

    private val TYPE_TEXT = 0
    private val TYPE_VOICE = 1

    private val mAdapter = ChatRecordAdapter(1)
    private val mVoiceRecordDialog by lazy {
        VoiceRecordDialog().apply {
            setConfirmAction {
                val voiceBean = MediaRecordController.stopVideoRecord()
                //发送
                voiceBean?.run {
                    val chatBean = ChatRecordEntity(
                        voiceRecordPath = voicePath,
                        voiceRecordDuration = voiceDuration,
                        fromUid = 1,
                        toUid = 2,
                        chatType = ChatRecordEntity.TYPE_VOICE
                    )
                    addChatData(chatBean)
                }
            }
            setCancelAction {
                MediaRecordController.stopVideoRecord(true)
            }
        }
    }

    private var mInputType by Delegates.observable(TYPE_TEXT, { prop, old, new ->
        if (new == TYPE_VOICE) {
            PermissionX.request(
                this,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) { granted, permissions ->
                if (granted) {
                    ivTextVoice.setImageResource(R.drawable.ic_voice)
                    etContent.gone()
                    tvPressVoice.visible()
                } else {
                    toast("没权限")
                }
            }

        } else if (new == TYPE_TEXT) {
            ivTextVoice.setImageResource(R.drawable.ic_keyboard)
            etContent.visible()
            tvPressVoice.gone()
        }
    })

    lateinit var mChatListBean: ChatListBean

    override fun initVM(): ChatDetailViewModel = getViewModel()

    override fun getLayoutId() = R.layout.activity_chat_detail

    override fun initView() {
        mChatListBean = intent.getParcelableExtra(IntentExtra.fromToId)!!
        rootLayout.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (oldBottom != -1 && oldBottom > bottom) {
                if (mAdapter.data.isNotEmpty()) {
                    rvChatRecord.scrollToPosition(mAdapter.data.size - 1)
                }
            }
        }
        rvChatRecord.layoutManager = LinearLayoutManager(this)

        rvChatRecord.adapter = mAdapter

        rvChatRecord.itemAnimator?.run {
            addDuration = 0
            changeDuration = 0;
            moveDuration = 0;
            removeDuration = 0;
        }


        headerView.setBackAction {
            finish()
        }
        headerView.setMenuAction {

        }
        headerView.setToolbarTitle(mChatListBean.contractName)
        mAdapter.notifyDataSetChanged()
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

    override fun initListener() {
        MediaRecordController.setCompleteAction {
            mAdapter.setVoiceStop(it)
        }
        etContent.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                hideFunctionMenu(10)
            }
        }
        etContent.addTextChangedListener {
            tvSend.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        ivTextVoice.setOnClickListener(this)
        rootLayout.setOnClickListener(this)
        etContent.setOnClickListener(this)
        tvSend.setOnClickListener(this)
        ivMoreFunction.setOnClickListener(this)

        llAlbum.setOnClickListener(this)
        llCapture.setOnClickListener(this)
        llContracts.setOnClickListener(this)

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val data = mAdapter.data[position]
            if (data.chatType == ChatRecordEntity.TYPE_VOICE) {
                mAdapter.setVoiceStop()
                MediaRecordController.playVideoRecord(this, data.voiceRecordPath, data.id)
                mAdapter.setVoicePlay(data)
            }
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val chatRecord = mAdapter.data[position]
            when (view.id) {
                R.id.ivContent -> {
                    goToActivity<ChatImageActivity>(IntentExtra.imgPath to chatRecord.imgUrl, enterAnim = R.anim.alpha_scale_in, exitAnim = R.anim.anim_stay)
                }
                R.id.flVideo -> {
                    goToActivity<ChatVideoActivity>(IntentExtra.videoPath to chatRecord.videoPath, enterAnim = R.anim.alpha_scale_in, exitAnim = R.anim.anim_stay)
                }
            }
        }
        tvPressVoice.setOnLongClickListener {
            val location = Rect()
            it.getGlobalVisibleRect(location)
            val y = (location.top + location.bottom) / 2
            mVoiceRecordDialog.setTouchY(y)
            mVoiceRecordDialog.show(supportFragmentManager)
            MediaRecordController.startVoiceRecord()
            true
        }
    }

    override fun initData() {
        viewModelGet().getChatRecord(mChatListBean.fromToId, 1, 10)
    }

    override fun dataObserver() {
        with(viewModelGet()) {
            mChatRecordData.observe(this@ChatDetailActivity, Observer {
                if (mAdapter.data.size > 0) {
                    mAdapter.addData(it)
                }else {
                    rvChatRecord.invisible()
                    //根据item总高度显示区分 stackFromEnd 会出现问题，所以目前先靠估算吧
                    val layoutManager = rvChatRecord.layoutManager as LinearLayoutManager
                    mAdapter.setNewInstance(it.toMutableList())
                    rvChatRecord.postDelayed(50){
                        layoutManager.stackFromEnd = isFullScreen(layoutManager)
                        rvChatRecord.alpha = 1f
                        rvChatRecord.visible()
                    }
                }
            })
            mSendMessageData.observe(this@ChatDetailActivity, Observer {

            })
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (mVoiceRecordDialog.dialog?.isShowing == true) {
            mVoiceRecordDialog.dispatchTouchEvent(ev!!)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun showFunctionMenu(duration: Long = 300) {
        llBottom.animate().translationY(-flBottom.height.toFloat()).setDuration(duration).start()
        flBottom.animate().withStartAction {
            flBottom.visible()
        }.translationY(-flBottom.height.toFloat()).setDuration(duration).start()

    }

    private fun hideFunctionMenu(duration: Long = 300) {
        llBottom.animate().translationY(0f).setDuration(duration).start()

        flBottom.animate().withEndAction {
            flBottom.gone()
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
                val content = etContent.textTrim()
                addChatData(
                    ChatRecordEntity(
                        content = content,
                        fromUid = 1,
                        toUid = 2
                    )
                )
                etContent.setText("")
            }
            R.id.ivMoreFunction -> {
                showFunctionMenu()
            }
            R.id.llAlbum -> {
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofAll())
                    .loadImageEngine(GlideEngine)
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: List<LocalMedia>) {
                            hideFunctionMenu()
                            val res = result[0]
                            addChatData(
                                ChatRecordEntity(
                                    fromUid = 2,
                                    toUid = 1,
                                    chatType = ChatRecordEntity.TYPE_IMG,
                                    imgUrl = res.path
                                )
                            )
                        }

                        override fun onCancel() {}
                    })
                hideFunctionMenu()
            }
            R.id.llCapture -> {
                PermissionX.request(
                    this,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                ) { grated, denyList ->
                    if (grated) {
                        gotoCameraCapture()
                    } else {
                        Toasts.show("请授予相应权限")
                    }
                }
                hideFunctionMenu()
            }
            R.id.llContracts -> {

            }
        }
    }

    //v 永远都是EditText
    override fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        //两种情况 etContent 和 tvSend 不隐藏
        val et = checkView(etContent, event)
        val tv = checkView(tvSend, event) && tvSend.visibility == View.VISIBLE
        return !et && !tv
    }

    override fun hideView(ev: MotionEvent) {
        //两种情况 etContent 和 tvSend 不隐藏
        val iv = checkView(ivMoreFunction, ev)
        val fl = checkView(flBottom, ev)
        if (!iv && !fl) {
            hideFunctionMenu()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaRecordController.reset()
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
        data.fromToId = mChatListBean.fromToId
        mAdapter.addData(data)
        rvChatRecord.smoothScrollToPosition(mAdapter.data.size - 1)
        Handler().postDelayed({
            val mLayoutManager = rvChatRecord.layoutManager as LinearLayoutManager
            mLayoutManager.scrollToPositionWithOffset(mAdapter.data.size - 1, 0)
        }, 200)
        viewModelGet().sendChatMessage(data)

        getAppViewModelProvider().get(SharedViewModel::class.java).notifyChatChange(true)
    }

    private fun gotoCameraCapture() {
        SmartJump.from(this).startForResult(PictureVideoActivity::class.java, { resultCode, data ->
            hideFunctionMenu()
            if (resultCode == Activity.RESULT_OK && data != null) {
                val state = data.getIntExtra(IntentExtra.stateEvent, StateEvent.STATE_FINISH_IMG)
                if (state == StateEvent.STATE_FINISH_VIDEO) {
                    addChatData(
                        ChatRecordEntity(
                            fromUid = 2,
                            toUid = 1,
                            chatType = ChatRecordEntity.TYPE_VIDEO,
                            videoPath = data.getStringExtra(IntentExtra.videoImgPath)!!,
                            videoDuration = data.getIntExtra(IntentExtra.videoDuration, 0).toLong()
                        )
                    )
                } else {
                    addChatData(
                        ChatRecordEntity(
                            fromUid = 2,
                            toUid = 1,
                            chatType = ChatRecordEntity.TYPE_IMG,
                            imgUrl = data.getStringExtra(IntentExtra.videoImgPath)!!
                        )
                    )
                }
            }
        })
    }

}
