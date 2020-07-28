package com.benyq.guochat.ui.me

import android.os.Environment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.function.other.GlideEngine
import com.benyq.guochat.loadAvatar
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.model.vm.PersonalInfoViewModel
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.guochat.ui.common.CommonBottomDialog
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.loge
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ktx.immersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.dialog.PhotoItemSelectedDialog
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_avatar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import okio.source
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File
import kotlinx.coroutines.launch

/**
 * @author benyq
 * @time 2020/5/21
 * @e-mail 1520063035@qq.com
 * @note 头像显示与修改
 */
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
        LocalStorage.userAccount.run {
            loadAvatar(ivAvatar, avatarUrl, 0)
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
        mViewModel.uploadAvatarLiveData.observe(this, Observer {
            LocalStorage.updateUserAccount {
                avatarUrl = it
            }
            initView()
        })
    }

    private fun showBottomDialog() {
        mBottomDialog =
            mBottomDialog ?: CommonBottomDialog.newInstance(
                arrayOf(
                    getString(R.string.select_from_album),
                    getString(R.string.save_to_phone)
                )
            )
                .apply {
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
                                viewLifecycleOwner.lifecycleScope.launch {
                                    savePhoto()
                                }
                            }

                        }
                    }
                }
        mBottomDialog?.show(supportFragmentManager)
    }

    private suspend fun savePhoto() {
        withContext(Dispatchers.IO) {

            val futureTarget = Glide.with(this@AvatarActivity)
                .asFile()
                .load(LocalStorage.userAccount.avatarUrl)
                .submit()

            val file: File = futureTarget.get()
            val parentPath = File(getExternalFilesDir("avatar")!!.absolutePath + "/")
            if (!parentPath.exists()) {
                parentPath.mkdir()
            }

            val targetFile = File(parentPath, "ddddddd.png")

            val bufferedSource = file.source().buffer()
            val bufferedSink = targetFile.sink().buffer()
            bufferedSink.writeAll(bufferedSource)
            bufferedSink.close()
            bufferedSource.close()
            Toasts.show("存储成功")

//            val bitmap = ivAvatar.drawable.toBitmap()
//
//            val saveUri = contentResolver.insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                ContentValues().apply {
//                    put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis().toString())
//                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        put(MediaStore.MediaColumns.IS_PENDING, 1)
//                    }
//                }
//            ) ?: kotlin.run {
//                Toasts.show("存储失败")
//                return@withContext
//            }
//            contentResolver.openOutputStream(saveUri).use {
//                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)) {
//                    Toasts.show("存储成功")
//                } else {
//                    Toasts.show("存储失败")
//                }
//            }
        }
    }

    private fun uploadAvatar(filePath: String){
        loge("filePath  $filePath")
        val file = File(filePath)
        if (!file.exists()){
            Toasts.show("图片地址错误")
            return
        }
        mViewModel.uploadAvatar(file)
    }

    inline fun test(inlined: () -> Unit, crossinline action: (String)->Unit) {
        inlined.invoke()
        action.invoke("dddd")
    }
}
