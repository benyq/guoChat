package com.benyq.guochat.ui.contracts

import com.benyq.guochat.R
import com.benyq.guochat.databinding.ActivityCallingCardBinding
import com.benyq.guochat.function.zxing.encode.CodeEncodingCreator
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.guochat.model.bean.UserBean
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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

        binding.tvContractName.text = mUserBean.nickName
        createCallingCardQr("1234")

    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }
    }

    private fun createCallingCardQr(chatNo: String) {
        val launcher = CoroutineScope(mLoadIconJob)
        val content = "chatNo:$chatNo"
        launcher.launch(Dispatchers.Main) {
            val bitmap = withContext(Dispatchers.IO) {
                Glide.with(this@CallingCardActivity)
                    .asBitmap() //必须
                    .load(mUserBean.avatarUrl)
                    .centerCrop()
                    .transform(RoundedCorners(dip2px(5).toInt()))
                    .submit(150, 150)
                    .get()
            }
            binding.ivCallingQr.setImageBitmap(CodeEncodingCreator.createQRCode(content, 300, 300, bitmap))
        }

    }
}
