package com.benyq.guochat.ui.discover

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.function.other.GlideEngine
import com.benyq.guochat.ui.base.BaseActivity
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.guochat.ui.common.CommonBottomDialog
import com.benyq.mvvm.SmartJump
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.getColorRef
import com.benyq.mvvm.ext.loge
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_add_circle.*

/**
 * @author benyq
 * @time 2020/6/20
 * @e-mail 1520063035@qq.com
 * @note  发布朋友圈
 */
class AddCircleActivity : BaseActivity() {

    private var mBottomDialog: CommonBottomDialog? = null

    override fun getLayoutId() = R.layout.activity_add_circle

    override fun initView() {
        headerView.run {
            setBackAction { finish() }
            setMenuBtnAction {
                //发布

            }
        }

        nineGrid.setItemAction { view, list, position ->
            SmartJump.from(this).startForResult(Intent(this, CirclePhotoViewPagerActivity::class.java).apply {
                putExtra(IntentExtra.circlePhotos, list.toTypedArray())
                putExtra(IntentExtra.circlePhotosIndex, position)
            }){ code, data ->
                data?.let {
                    val photots = it.getStringArrayExtra(IntentExtra.circlePhotos) ?: arrayOf<String>()
                    loge("photots $photots")
                    nineGrid.addItems(photots.toList())
                }

            }
            Toasts.show("当前位置 $position  圖片長度 ${list.size}")
        }
        nineGrid.setAddAction {
            showSelectPhotoBottomDialog()
        }
    }

    private fun showSelectPhotoBottomDialog(){
        mBottomDialog =
            mBottomDialog ?: CommonBottomDialog.newInstance(arrayOf("拍摄", "从相册选择"))
                .apply {
                    setOnMenuAction { _, index ->
                        when (index) {
                            0 -> {
                                takePicture()
                            }
                            1 -> {
                                selectPhoto()
                            }

                        }
                    }
                }
        mBottomDialog?.show(supportFragmentManager)
    }

    private fun takePicture(){
        PictureSelector.create(this@AddCircleActivity)
            .openCamera(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: List<LocalMedia>) {
                    nineGrid.addItems(result.map {
                        it.path
                    })
                }

                override fun onCancel() {

                }
            })
    }

    private fun selectPhoto(){
        PictureSelector.create(this@AddCircleActivity)
            .openGallery(PictureMimeType.ofAll())
            .loadImageEngine(GlideEngine)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: List<LocalMedia>) {
                    nineGrid.addItems(result.map {
                        it.path
                    })
                }

                override fun onCancel() {

                }
            })
    }
}