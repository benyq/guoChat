package com.benyq.guochat.ui.discover

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.vm.AddCircleViewModel
import com.benyq.guochat.ui.base.BaseDialogFragment
import com.benyq.mvvm.ext.getScreenHeight
import com.benyq.mvvm.ext.getScreenWidth
import kotlinx.android.synthetic.main.dialog_circle_photo_view_pager.*
import java.util.*

/**
 * @author benyq
 * @time 2020/7/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class CirclePhotoViewPagerFragment : BaseDialogFragment() {

    companion object {
        fun newInstance(index: Int): CirclePhotoViewPagerFragment {
            return CirclePhotoViewPagerFragment().apply {
                arguments = bundleOf(IntentExtra.circlePhotosIndex to index)
            }
        }
    }

    private val mAdapter = CirclePhotoAdapter()

    private val mViewModel: AddCircleViewModel by activityViewModels()

    override fun getLayoutId() = R.layout.dialog_circle_photo_view_pager

    override fun initView() {
        headerView.run {
            setBackAction {
                dismiss()
            }
            setMenuAction {
                mAdapter.removeAt(viewPager.currentItem)
                headerView.setToolbarTitle("${viewPager.currentItem + 1} / ${mAdapter.data.size}")
            }
        }

        val photoUrls = mViewModel.addCirclePhotoUrlData.value ?: mutableListOf()
        val photoUrlsIndex = arguments?.getInt(IntentExtra.circlePhotosIndex, 0) ?: 0
        viewPager.adapter = mAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mAdapter.setNewInstance(photoUrls)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                headerView.setToolbarTitle("${position + 1} / ${mAdapter.data.size}")
            }
        })

        viewPager.setCurrentItem(photoUrlsIndex, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val lp = it.attributes
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            lp.width = mContext.getScreenWidth()
            lp.height = mContext.getScreenHeight()
            it.attributes = lp
            it.setDimAmount(0f)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mViewModel.addCirclePhotoUrlData.value = mAdapter.data
    }
}