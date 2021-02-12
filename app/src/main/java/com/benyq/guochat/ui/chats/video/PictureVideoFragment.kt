package com.benyq.guochat.ui.chats.video

import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.app.chatImgPath
import com.benyq.guochat.app.chatVideoPath
import com.benyq.guochat.function.video.CaptureController
import com.benyq.guochat.function.video.filter.BaseFilter
import com.benyq.guochat.function.video.filter.FilterFactory
import com.benyq.guochat.function.video.filter.FilterType
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.getCurrentDate
import com.benyq.module_base.ext.gone
import com.benyq.module_base.ext.visible
import com.benyq.module_base.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_picture_video.*

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class PictureVideoFragment : BaseFragment() {

    private val TAG = "PictureVideoFragment"

    private val pictureVideoViewModel: PictureVideoViewModel by activityViewModels()
    private lateinit var mCaptureController: CaptureController
    private val mFilterAdapter by lazy { FilterAdapter() }
    private var mCurrentFilter: BaseFilter? = null

    override fun getLayoutId() = R.layout.fragment_picture_video

    override fun initView() {
        super.initView()
        mCaptureController = CaptureController(requireActivity(), glSurfaceView)
        lifecycle.addObserver(mCaptureController)

        rvFilters.layoutManager = LinearLayoutManager(mContext)
        rvFilters.adapter = mFilterAdapter
        mFilterAdapter.setNewInstance(FilterType.provideFilters())
        mFilterAdapter.setOnItemClickListener { adapter, view, position ->
            mFilterAdapter.selectFilter(position)

            mCurrentFilter = FilterFactory.createFilter(mFilterAdapter.data[position].type)
            mCaptureController.switchFilter(mCurrentFilter)
        }
        mFilterAdapter.selectFilter(0)
    }

    override fun initListener() {

        ivClose.setOnClickListener {
            pictureVideoViewModel.clearAll()
        }

        ivCameraChange.setOnClickListener {
            mCaptureController.switchCamera()
        }

        with(captureView) {
            setPictureAction {
                val imgPath = requireActivity().chatImgPath() + getCurrentDate() + ".jpg"
                mCaptureController.takePicture(imgPath, { bitmap, path ->
                    Toasts.show(R.string.save_photo_success)
                    pictureVideoViewModel.showPictureConfirm(path)
                }, {
                    it.printStackTrace()
                })
            }

            setVideoStartAction {
                val videoFileName: String =
                    requireActivity().chatVideoPath() + "guoChat" + "_" + getCurrentDate() + ".mp4"
                mCaptureController.startRecording(videoFileName, mCaptureController.mCameraHeight,
                    mCaptureController.mCameraWidth, { path, duration ->
                        Toasts.show(R.string.save_video_success)
                        pictureVideoViewModel.showVideoConfirm(path, duration)
                    }, {
                        it.printStackTrace()
                    })
            }

            setVideoEndAction {
                mCaptureController.stopRecording()
            }
        }
        ivAddFilters.setOnClickListener {
            if (rvFilters.isGone) {
                rvFilters.visible()
            } else {
                rvFilters.gone()
            }
        }

    }

}