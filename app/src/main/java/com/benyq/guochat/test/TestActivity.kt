package com.benyq.guochat.test

import android.media.MediaMetadataRetriever
import android.os.Environment
import android.widget.ImageView
import androidx.core.view.children
import androidx.core.view.postDelayed
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.databinding.ActivityTestBinding
import com.benyq.guochat.loadImage
import com.benyq.imageviewer.ImagePreview
import com.benyq.imageviewer.PreviewPhoto
import com.benyq.imageviewer.PreviewTypeEnum
import com.benyq.mvvm.ext.binding
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ui.base.BaseActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : BaseActivity() {

    private val TAG = "TestActivity"
    private lateinit var mViewModel: TestViewModel

    private val mBinding: ActivityTestBinding by binding()

    private val mAdapter by lazy { ImageAdapter() }

    override fun getLayoutView() = mBinding.root

    override fun initView() {
        mViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        mBinding.rvPhoto.layoutManager = GridLayoutManager(this, 2)
        mBinding.rvPhoto.adapter = mAdapter
        mAdapter.setNewInstance(
            mutableListOf(
                PreviewPhoto(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + "video2.mp4",
                    PreviewTypeEnum.VIDEO
                ),
                PreviewPhoto(
                    "https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592643441654-assets/web-upload/771e09b0-aaf9-4308-bae0-cd5b3cb98817.jpeg",
                    PreviewTypeEnum.IMAGE
                ),
                PreviewPhoto(
                    "https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592643441654-assets/web-upload/771e09b0-aaf9-4308-bae0-cd5b3cb98817.jpeg",
                    PreviewTypeEnum.IMAGE
                ),
                PreviewPhoto(
                    "https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592643441654-assets/web-upload/771e09b0-aaf9-4308-bae0-cd5b3cb98817.jpeg",
                    PreviewTypeEnum.IMAGE
                ),
                PreviewPhoto(
                    "https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592643441557-assets/web-upload/94ed7774-2bed-4dbe-be54-080c2f8939a1.jpeg",
                    PreviewTypeEnum.IMAGE
                ),
                PreviewPhoto(
                    "https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592643441632-assets/web-upload/84d01b3f-7f28-4125-b3c7-8e5b5b15c0cb.jpeg",
                    PreviewTypeEnum.IMAGE
                ),
                PreviewPhoto(
                    "https://tva1.sinaimg.cn/large/007S8ZIlly1giy4k2flh3j31c00u0gs8.jpg",
                    PreviewTypeEnum.IMAGE
                ),
//                PreviewPhoto(
//                    "https://tva1.sinaimg.cn/large/007S8ZIlly1giy4m99esij30u01hcn1x.jpg",
//                    PreviewTypeEnum.IMAGE
//                ),
//                PreviewPhoto(
//                    "https://tva1.sinaimg.cn/large/007S8ZIlly1giy4onuhofj30sg1ek43l.jpg",
//                    PreviewTypeEnum.IMAGE
//                ),
                PreviewPhoto(
                    "https://tva1.sinaimg.cn/large/007S8ZIlly1giy4pi32qcj30qe110gob.jpg",
                    PreviewTypeEnum.IMAGE
                ),
                PreviewPhoto(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + "video1.mp4",
                    PreviewTypeEnum.VIDEO
                ),

            )
        )
    }

    override fun initListener() {
        mAdapter.setOnItemClickListener { adapter, view, position ->
            ImagePreview
                .setRecyclerView(mBinding.rvPhoto)
                .setThumbnailViewId(R.id.ivPhoto)
                .setFullScreen(false)
                .setData(mAdapter.data)
                .setCurPosition(position)
                .show(this)
        }
    }


    class ImageAdapter :
        BaseQuickAdapter<PreviewPhoto, BaseViewHolder>(R.layout.item_preview_photo) {
        override fun convert(holder: BaseViewHolder, item: PreviewPhoto) {
            val image = holder.getView<ImageView>(R.id.ivPhoto)
            image.loadImage(item.url)
        }
    }

}