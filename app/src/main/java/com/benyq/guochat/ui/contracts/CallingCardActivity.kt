package com.benyq.guochat.ui.contracts

import com.benyq.guochat.R
import com.benyq.guochat.dip2px
import com.benyq.guochat.function.zxing.encode.CodeEncodingCreator
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.model.bean.UserBean
import com.benyq.guochat.ui.base.BaseActivity
import com.benyq.guochat.ui.base.LifecycleActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.activity_calling_card.*
import kotlinx.coroutines.*

/**
 * @author benyq
 * @time 2020/5/8
 * @e-mail 1520063035@qq.com
 * @note 二维码名片
 *  预想的是根据传入的果聊号生成名片信息
 */
class CallingCardActivity : BaseActivity() {

    private val mLoadIconJob: Job = Job()

    override fun getLayoutId() = R.layout.activity_calling_card

    private lateinit var mUserBean: UserBean

    override fun initView() {
        mUserBean = LocalStorage.userAccount
        Glide.with(this).load(mUserBean.avatarUrl)
            .transform(RoundedCorners(dip2px(this, 10).toInt()))
            .into(ivAvatar)

        tvContractName.text = mUserBean.nickName
        createCallingCardQr("1234")

    }

    override fun initListener() {
        headerView.setBackAction { finish() }
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
                    .transform(RoundedCorners(dip2px(this@CallingCardActivity, 5).toInt()))
                    .submit(150, 150)
                    .get()
            }
            ivCallingQr.setImageBitmap(CodeEncodingCreator.createQRCode(content, 300, 300, bitmap))
        }

    }
}
