package com.benyq.guochat.ui.discover

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.TestEntity
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.function.other.GlideEngine
import com.benyq.guochat.getViewModel
import com.benyq.guochat.model.vm.AddCircleViewModel
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.guochat.ui.common.CommonBottomDialog
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ext.textTrim
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_circle.*
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/6/20
 * @e-mail 1520063035@qq.com
 * @note  发布朋友圈
 */
@AndroidEntryPoint
class AddCircleActivity : LifecycleActivity<AddCircleViewModel>() {

    private var mBottomDialog: CommonBottomDialog? = null

    @Inject
    lateinit var mTestEntity: TestEntity

    override fun initVM(): AddCircleViewModel = getViewModel()

    override fun getLayoutId() = R.layout.activity_add_circle

    override fun initView() {
        headerView.run {
            setBackAction { finish() }
            setMenuBtnAction {
                //发布
                val content = etContent.textTrim()
                val images = nineGrid.getPhotoUrls()
                val intent = Intent()
                intent.putExtra(IntentExtra.addCircleContent, content)
                intent.putExtra(IntentExtra.addCircleImages, images.toTypedArray())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        nineGrid.setItemAction { view, list, position ->
            CirclePhotoViewPagerFragment.newInstance(position).show(supportFragmentManager)
        }
        nineGrid.setAddAction {
            showSelectPhotoBottomDialog()
        }
    }

    private fun showSelectPhotoBottomDialog() {
        loge("mTestEntity  ${mTestEntity.name} --- ${mTestEntity.age}")

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

    private fun takePicture() {
        PictureSelector.create(this@AddCircleActivity)
            .openCamera(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: List<LocalMedia>) {
                    viewModelGet().addCirclePhotoUrl(result.map { it.path })
                }

                override fun onCancel() {

                }
            })
    }

    private fun selectPhoto() {
        PictureSelector.create(this@AddCircleActivity)
            .openGallery(PictureMimeType.ofAll())
            .loadImageEngine(GlideEngine)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: List<LocalMedia>) {
                    viewModelGet().addCirclePhotoUrl(result.map { it.path })
                }

                override fun onCancel() {

                }
            })
    }

    override fun dataObserver() {
        viewModelGet().addCirclePhotoUrlData.observe(this, Observer {
            nineGrid.addItems(it)
        })
    }
}