package com.benyq.guochat.ui.me

import android.os.Environment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.function.other.GlideEngine
import com.benyq.guochat.getViewModel
import com.benyq.guochat.loadImage
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.guochat.model.vm.PersonalInfoViewModel
import com.benyq.guochat.saveImg
import com.benyq.mvvm.ui.base.LifecycleActivity
import com.benyq.guochat.ui.common.CommonBottomDialog
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.loge
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ktx.immersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_avatar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @author benyq
 * @time 2020/5/21
 * @e-mail 1520063035@qq.com
 * @note 头像显示与修改
 */
@AndroidEntryPoint
class AvatarActivity : LifecycleActivity<PersonalInfoViewModel>() {

    override fun initVM(): PersonalInfoViewModel = getViewModel()

    private var mBottomDialog: CommonBottomDialog? = null

    override fun initWidows() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.color_2a2a2a)
        }
    }

    override fun getLayoutId() = R.layout.activity_avatar

    override fun initView() {
        //从本地缓存中取出user信息
        //Glide加载头像
        ChatLocalStorage.userAccount.run {
            ivAvatar.loadImage(avatarUrl, 0)
        }
    }

    override fun initListener() {
        headerView.setBackAction {
            finish()
        }
        headerView.setMenuAction {
            showBottomDialog()
        }
    }

    override fun dataObserver() {
        viewModelGet().uploadAvatarLiveData.observe(this, Observer {
            ChatLocalStorage.updateUserAccount {
                avatarUrl = it
            }
            initView()
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_right_out)
    }

    private fun showBottomDialog() {
        mBottomDialog =
            mBottomDialog ?: CommonBottomDialog.newInstance(
                arrayOf(
                    getString(R.string.select_from_album),
                    getString(R.string.save_to_phone)
                )
            ).apply {
                    setOnMenuAction { _, index ->
                        when (index) {
                            0 -> {
                                PictureSelector.create(this@AvatarActivity)
                                    .openGallery(PictureMimeType.ofAll())
                                    .loadImageEngine(GlideEngine)
                                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                                        override fun onResult(result: List<LocalMedia>) {
                                            val res = result[0]
                                            uploadAvatar(res.path)
                                        }

                                        override fun onCancel() {

                                        }
                                    })
                            }
                            1 -> {
                                //保存图片
                                lifecycleScope.launch {
                                    savePhoto()
                                }
                            }

                        }
                    }
                }
        mBottomDialog?.show(supportFragmentManager)
    }

    private suspend fun savePhoto() {
        //在 Environment.getExternalStoragePublicDirectory 下的图片才能被刷新到系统， getExternalFilesDir 不行
        val parentPath = getExternalFilesDir("avatar")!!.absolutePath + "/"
        val parentPath2 = Environment.getExternalStoragePublicDirectory("avatar").absolutePath + "/"
        val imgName = "avatar-${System.currentTimeMillis()}.png"
        val result = withContext(Dispatchers.IO) {
            val futureTarget = Glide.with(this@AvatarActivity)
                .asFile()
                .load(ChatLocalStorage.userAccount.avatarUrl)
                .submit()
            val file = futureTarget.get()
            saveImg(this@AvatarActivity, file, parentPath2, imgName)
        }
        if (result) {
            Toasts.show("保存成功, 路径 $parentPath2$imgName")
        } else {
            Toasts.show("保存失败")
        }
    }

    private fun uploadAvatar(filePath: String) {
        loge("filePath  $filePath")
        val file = File(filePath)
        if (!file.exists()) {
            Toasts.show("图片地址错误")
            return
        }
        viewModelGet().uploadAvatar(file)
    }
}
