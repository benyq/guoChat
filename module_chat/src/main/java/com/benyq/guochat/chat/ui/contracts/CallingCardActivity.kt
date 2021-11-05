package com.benyq.guochat.chat.ui.contracts

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.benyq.guochat.chat.databinding.ActivityCallingCardBinding
import com.benyq.guochat.chat.function.barcode.CodeEncodingCreator
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.bean.UserBean
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.*

/**
 * @author benyq
 * @time 2020/5/8
 * @e-mail 1520063035@qq.com
 * @note 二维码名片
 *  预想的是根据传入的果聊号生成名片信息
 */
class CallingCardActivity : BaseActivity<ActivityCallingCardBinding>() {

    private val mLoadIconJob: Job = Job()

    override fun provideViewBinding() = ActivityCallingCardBinding.inflate(layoutInflater)

    private lateinit var mUserBean: UserBean

    override fun initView() {
        mUserBean = ChatLocalStorage.userAccount
        Glide.with(this).load(mUserBean.avatarUrl)
            .transform(RoundedCorners(dip2px(10).toInt()))
            .into(binding.ivAvatar)

        binding.tvContractName.text = mUserBean.nick
        createCallingCardQr(ChatLocalStorage.uid)

    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }
    }

    private fun createCallingCardQr(chatId: String) {
        val launcher = CoroutineScope(mLoadIconJob)
        val content = "chat-$chatId"
        launcher.launch(Dispatchers.Main) {
            Glide.with(this@CallingCardActivity)
                .asBitmap()
                .load(mUserBean.avatarUrl)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        loge("Glide onResourceReady")
                        binding.ivCallingQr.setImageBitmap(CodeEncodingCreator.createQRCode(content, 300, 300, resource))
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        binding.ivCallingQr.setImageBitmap(CodeEncodingCreator.createQRCode(content, 300, 300, null))
                    }
                })
        }

    }
}
